package io.berndruecker.demo.zeebe.dmn.zeebe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.berndruecker.demo.zeebe.dmn.ZeebeWorkerDmnCamundaPlatformApplication;
import io.zeebe.client.ZeebeClient;

@Configuration
public class ZeebeInit {
  
  private static final Logger LOG = LoggerFactory.getLogger(ZeebeWorkerDmnCamundaPlatformApplication.class);
  
  @Value("${zeebe.brokerContactPoint}")
  private String zeebeBrokerContactPoint;

  @Value("${zeebe.numberOfWorkerThreads:1}")
  private int numberOfWorkerThreads = 1;
    
  @Bean
  public ZeebeClient zeebe() throws Exception {
    LOG.info("Connect to Zeebe at '" + zeebeBrokerContactPoint + "'...");

    ZeebeClient zeebeClient = ZeebeClient.newClientBuilder() //
        .brokerContactPoint(zeebeBrokerContactPoint) //
        .numJobWorkerExecutionThreads(numberOfWorkerThreads) //
        .build();

    LOG.info("...connected.");

    return zeebeClient;
  }
}
