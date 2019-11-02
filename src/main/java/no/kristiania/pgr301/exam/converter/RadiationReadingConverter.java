package no.kristiania.pgr301.exam.converter;

import no.kristiania.pgr301.exam.dto.RadiationReadingDto;
import no.kristiania.pgr301.exam.model.RadiationReading;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RadiationReadingConverter {

  public RadiationReadingDto createFromEntity(final RadiationReading radiationReading) {
    RadiationReadingDto dto = new RadiationReadingDto();
    dto.setId(radiationReading.getId());
    dto.setLatitude(radiationReading.getLatitude());
    dto.setLongitude(radiationReading.getLongitude());
    dto.setSievert(radiationReading.getSievert());
    dto.setCreated(radiationReading.getCreated());
    return dto;
  }

  public RadiationReading createFromDto(final RadiationReadingDto radiationReadingDto) {
    RadiationReading radiationReading = new RadiationReading();
    radiationReading.setLongitude(radiationReadingDto.getLongitude());
    radiationReading.setLatitude(radiationReadingDto.getLatitude());
    radiationReading.setSievert(radiationReadingDto.getSievert());

    return radiationReading;
  }

  public List<RadiationReadingDto> createFromEntities(
      final List<RadiationReading> radiationReadings) {
    return radiationReadings.stream().map(this::createFromEntity).collect(Collectors.toList());
  }

  public List<RadiationReading> createFromDtos(
      final List<RadiationReadingDto> radiationReadingDtos) {
    return radiationReadingDtos.stream().map(this::createFromDto).collect(Collectors.toList());
  }
}
