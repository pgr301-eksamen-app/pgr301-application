package no.kristiania.pgr301.exam.converter;

import lombok.RequiredArgsConstructor;
import no.kristiania.pgr301.exam.dto.GeigerCounterDto;
import no.kristiania.pgr301.exam.dto.RadiationReadingDto;
import no.kristiania.pgr301.exam.model.GeigerCounter;
import no.kristiania.pgr301.exam.model.RadiationReading;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GeigerCounterConverter {

  private final RadiationReadingConverter radiationReadingConverter;

  public GeigerCounterDto createFromEntity(final GeigerCounter geigerCounter) {
    GeigerCounterDto geigerCounterDto = new GeigerCounterDto();
    geigerCounterDto.setDeviceId(geigerCounter.getDeviceId());
    geigerCounterDto.setName(geigerCounter.getName());
    geigerCounterDto.setType(geigerCounter.getType());

    if (geigerCounter.getRadiationReadings() != null) {
      List<RadiationReadingDto> radiationReadingDtos =
          radiationReadingConverter.createFromEntities(geigerCounter.getRadiationReadings());
      geigerCounterDto.setRadiationReadings(radiationReadingDtos);
    }

    return geigerCounterDto;
  }

  public GeigerCounter createFromDto(final GeigerCounterDto geigerCounterDto) {
    GeigerCounter geigerCounter = new GeigerCounter();
    geigerCounter.setDeviceId(geigerCounterDto.getDeviceId());
    geigerCounter.setName(geigerCounter.getName());
    geigerCounter.setType(geigerCounterDto.getType());

    if (geigerCounter.getRadiationReadings() != null) {
      List<RadiationReading> radiationReading =
          radiationReadingConverter.createFromDtos(geigerCounterDto.getRadiationReadings());
      geigerCounter.setRadiationReadings(radiationReading);
    }
    return geigerCounter;
  }

  public List<GeigerCounterDto> createFromEntities(List<GeigerCounter> geigerCounters) {
    return geigerCounters.stream().map(this::createFromEntity).collect(Collectors.toList());
  }
}
