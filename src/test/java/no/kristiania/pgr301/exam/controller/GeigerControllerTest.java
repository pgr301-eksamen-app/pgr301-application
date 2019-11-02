package no.kristiania.pgr301.exam.controller;

import io.restassured.RestAssured;
import no.kristiania.pgr301.exam.dto.GeigerCounterDto;
import no.kristiania.pgr301.exam.dto.RadiationReadingDto;
import no.kristiania.pgr301.exam.enums.DeviceType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@ActiveProfiles(profiles = "test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GeigerControllerTest {

  @LocalServerPort private int port;

  @Before
  public void setUp() throws Exception {
    RestAssured.baseURI = "http://localhost";
    RestAssured.port = port;
    RestAssured.basePath = "/devices";
  }

  @Test
  public void testCreatingGeigerCounter_withInvalidDeviceType_shouldReturn400() {
    String invalidDeviceType = "MONKEY";

    Arrays.stream(DeviceType.values())
        .forEach(deviceType -> assertThat(deviceType.name(), is(not(equalTo(invalidDeviceType)))));

    given().queryParam("deviceType", invalidDeviceType).post("/").then().statusCode(400);
  }

  @Test
  public void testCreatingGeigerCounter_withValidDeviceType_shouldReturn201() {
    Arrays.stream(DeviceType.values())
        .forEach(e -> given().queryParam("deviceType", e).post("/").then().statusCode(201));
  }

  @Test
  public void testCreatingGeigerCounter_withoutDeviceType_shouldReturn201() {
    given().post("/").then().statusCode(201);
  }

  @Test
  public void testGettingAllGeigerCounters_shouldReturnAllAddedGeigerCounters() {
    int expectedSize = 10;

    createGeigerCounters(expectedSize);

    GeigerCounterDto[] result =
        given().get("/").then().statusCode(200).and().extract().as(GeigerCounterDto[].class);

    assertThat(result.length, equalTo(expectedSize));
  }

  @Test
  public void testAddingRadiationReading_withoutInvalidDeviceId_shouldReturn404() {
    given().get("/-1/measurements").then().statusCode(404);
  }

  @Test
  public void testAddingRadiationReading_withValidDeviceId_shouldReturn201() throws Exception {
    GeigerCounterDto geigerCounter = createGeigerCounter();

    RadiationReadingDto radiationReading = createRadiationReadingDto();

    given()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(radiationReading)
        .post("/" + geigerCounter.getDeviceId() + "/measurements")
        .then()
        .statusCode(201);
  }

  @Test
  public void testGettingRadiationReadings_withValidDeviceId_shouldReturn200() {
    GeigerCounterDto geigerCounter = createGeigerCounter();

    RadiationReadingDto radiationReading = createRadiationReadingDto();

    given()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(radiationReading)
        .post("/" + geigerCounter.getDeviceId() + "/measurements")
        .then()
        .statusCode(201);

    RadiationReadingDto[] result =
        given()
            .get("/" + geigerCounter.getDeviceId() + "/measurements")
            .then()
            .statusCode(200)
            .and()
            .extract()
            .as(RadiationReadingDto[].class);

    assertThat(result.length, equalTo(1));
  }

  private void createGeigerCounters(int amountOfCounters) {
    for (int i = 0; i < amountOfCounters; i++) {
      createGeigerCounter();
    }
  }

  private GeigerCounterDto createGeigerCounter() {
    return given().post("/").then().statusCode(201).extract().as(GeigerCounterDto.class);
  }

  private RadiationReadingDto createRadiationReadingDto() {
    RadiationReadingDto radiationReading = new RadiationReadingDto();
    radiationReading.setSievert(10.0);
    radiationReading.setLatitude(56.5);
    radiationReading.setLongitude(56.5);
    return radiationReading;
  }
}
