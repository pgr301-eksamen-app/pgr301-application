package no.kristiania.pgr301.exam.controller;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@ActiveProfiles(profiles = "test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PingControllerTest {

  @LocalServerPort private int port;

  @Before
  public void setUp() throws Exception {
    RestAssured.baseURI = "http://localhost";
    RestAssured.port = port;
    RestAssured.basePath = "/ping";
  }

  @Test
  public void testPing_shouldReturnPong() {
    String result = given().get().then().statusCode(200).extract().body().asString();
    assertThat(result, is(equalTo("pong")));
  }
}
