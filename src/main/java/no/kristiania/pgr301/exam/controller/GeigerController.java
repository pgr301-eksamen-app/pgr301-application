package no.kristiania.pgr301.exam.controller;

import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.kristiania.pgr301.exam.converter.GeigerCounterConverter;
import no.kristiania.pgr301.exam.converter.RadiationReadingConverter;
import no.kristiania.pgr301.exam.dto.GeigerCounterDto;
import no.kristiania.pgr301.exam.dto.RadiationReadingDto;
import no.kristiania.pgr301.exam.enums.DeviceType;
import no.kristiania.pgr301.exam.model.GeigerCounter;
import no.kristiania.pgr301.exam.model.RadiationReading;
import no.kristiania.pgr301.exam.repository.GeigerCounterRepository;
import no.kristiania.pgr301.exam.repository.RadiationReadingRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.SerializationUtils;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/devices")
public class GeigerController {

  private final GeigerCounterRepository geigerCounterRepository;
  private final RadiationReadingRepository radiationReadingRepository;
  private final GeigerCounterConverter geigerCounterConverter;
  private final RadiationReadingConverter radiationReadingConverter;
  private final MeterRegistry meterRegistry;

  @Timed(
      value = "request.time",
      extraTags = {"devices", "create"})
  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Counted(value = "request.count.devices.create")
  public ResponseEntity createGeigerCounter(
      @RequestParam(required = false) String deviceName,
      @RequestParam(required = false) String deviceType) {

    GeigerCounter entity = new GeigerCounter();
    entity.setName(deviceName);

    if (deviceType != null) {

      boolean isDeviceType =
          Arrays.stream(DeviceType.values())
              .anyMatch(device -> device.toString().equalsIgnoreCase(deviceType));

      if (!isDeviceType) {
        log.warn("Attempted creating device with unknown device type: {}", deviceType);
        return ResponseEntity.badRequest().build();
      }

      Counter specifiedDeviceCounter =
          Counter.builder("database.calls")
              .description("indicated how many times a device has gotten a specified device type")
              .tag("device", "type")
              .register(meterRegistry);
      specifiedDeviceCounter.increment();

      entity.setType(DeviceType.valueOf(deviceType));
    }

    geigerCounterRepository.save(entity);
    GeigerCounterDto dto = geigerCounterConverter.createFromEntity(entity);

    return ResponseEntity.status(201).body(dto);
  }

  @Timed(
      value = "request.time",
      extraTags = {"devices", "all"},
      longTask = true)
  @Counted(value = "request.count.devices.all")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity getAllDevices() {
    DistributionSummary distributionSummary =
        DistributionSummary.builder("device.request.info")
            .description("Measures the byte size of the response.")
            .tag("response", "byte-size")
            .register(meterRegistry);

    List<GeigerCounter> geigerCounters = geigerCounterRepository.findAll();
    Gauge.builder("devices.current.amount", geigerCounters, List::size).register(meterRegistry);

    ResponseEntity response =
        ResponseEntity.ok(geigerCounterConverter.createFromEntities(geigerCounters));
    try {
      Thread.sleep(5000);
      log.info("Sleeping thread to fake a long task.");
    } catch (InterruptedException e) {
      log.error("Issues sleeping thread", e);
    }

    if (response.getBody() instanceof Serializable) {
      byte[] responseInBytes = SerializationUtils.serialize(response.getBody());

      if (responseInBytes != null) {
        distributionSummary.record(responseInBytes.length);
      }
    }

    return response;
  }

  @PostMapping(value = "/{deviceId}/measurements", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity addRadiationReading(
      @PathVariable("deviceId") Long deviceId,
      @RequestBody RadiationReadingDto radiationReadingDto) {

    Counter radiationReadings =
        Counter.builder("database.calls")
            .description("Indicates how many measurements has been made")
            .tag("device", "measurements")
            .register(meterRegistry);

    if (deviceId == null) {
      log.warn("deviceId was null");
      return ResponseEntity.notFound().build();
    }

    Optional<GeigerCounter> geigerCounter = geigerCounterRepository.findById(deviceId);
    if (geigerCounter.isEmpty()) {
      log.info("Found no device for deviceId: {}", deviceId);
      return ResponseEntity.notFound().build();
    }

    radiationReadings.increment();

    RadiationReading radiationReading =
        radiationReadingConverter.createFromDto(radiationReadingDto);
    radiationReading.setDevice(geigerCounter.get());

    radiationReadingRepository.save(radiationReading);

    return ResponseEntity.status(201).build();
  }

  @GetMapping(value = "/{deviceId}/measurements", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity getReadings(@PathVariable("deviceId") Long deviceId) {

    Optional<GeigerCounter> geigerCounter = geigerCounterRepository.findById(deviceId);

    if (geigerCounter.isEmpty()) {
      log.info("Could not find any measurements for device with id: {}", deviceId);
      return ResponseEntity.notFound().build();
    }

    List<RadiationReading> radiationReadings = geigerCounter.get().getRadiationReadings();

    return ResponseEntity.ok(radiationReadingConverter.createFromEntities(radiationReadings));
  }
}
