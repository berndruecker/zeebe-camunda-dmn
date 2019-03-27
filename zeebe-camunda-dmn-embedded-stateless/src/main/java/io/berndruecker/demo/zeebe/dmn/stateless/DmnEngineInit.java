package io.berndruecker.demo.zeebe.dmn.stateless;

import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.dmn.engine.DmnEngineConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DmnEngineInit {
  
  @Bean
  public DmnEngine initDmnEngine() {
    return DmnEngineConfiguration.createDefaultDmnEngineConfiguration().buildEngine();
  }
  
}
