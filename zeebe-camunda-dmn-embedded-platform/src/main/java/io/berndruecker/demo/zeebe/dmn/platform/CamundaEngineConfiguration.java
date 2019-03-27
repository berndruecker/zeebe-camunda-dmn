package io.berndruecker.demo.zeebe.dmn.platform;


import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.camunda.bpm.spring.boot.starter.configuration.impl.AbstractCamundaConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CamundaEngineConfiguration extends AbstractCamundaConfiguration {

  @Override
  public void preInit(SpringProcessEngineConfiguration configuration) {
    configuration.setDmnHistoryEventProducer(new CaptureHistoryDmnHistoryEventProducer());
  }
}
