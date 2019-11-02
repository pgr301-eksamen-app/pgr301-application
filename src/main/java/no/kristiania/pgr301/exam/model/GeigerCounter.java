package no.kristiania.pgr301.exam.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.kristiania.pgr301.exam.enums.DeviceType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GeigerCounter implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long deviceId;

  private String name;

  @Enumerated(value = EnumType.STRING)
  private DeviceType type;

  @OneToMany(mappedBy = "device")
  private List<RadiationReading> radiationReadings;

  @CreationTimestamp private LocalDateTime timestamp;
  @UpdateTimestamp private LocalDateTime lastUpdated;
}
