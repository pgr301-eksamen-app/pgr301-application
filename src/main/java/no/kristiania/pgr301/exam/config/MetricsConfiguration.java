package no.kristiania.pgr301.exam.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Slf4j
@Profile("prod")
public class MetricsConfiguration {

  @Bean
  public MeterRegistry productionMeterRegistry() {
    return new SimpleMeterRegistry();
  }
}
