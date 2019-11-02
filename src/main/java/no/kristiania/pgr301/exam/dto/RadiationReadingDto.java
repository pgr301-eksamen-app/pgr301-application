package no.kristiania.pgr301.exam.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class RadiationReadingDto implements Serializable {

  private Long id;
  @NotNull private Double latitude;
  @NotNull private Double longitude;
  @NotNull private Double sievert;
  @NotNull private LocalDateTime created;
}
