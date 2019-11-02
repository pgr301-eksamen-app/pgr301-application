package no.kristiania.pgr301.exam.dto;

import lombok.Data;
import no.kristiania.pgr301.exam.enums.DeviceType;
import no.kristiania.pgr301.exam.model.GeigerCounter;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
public class GeigerCounterDto implements Serializable {

  private Long deviceId;
  private String name;
  private DeviceType type;
  private List<RadiationReadingDto> radiationReadings;


}
