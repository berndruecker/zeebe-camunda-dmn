package io.berndruecker.demo.zeebe.dmn.zeebe;

import java.time.Duration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.berndruecker.demo.zeebe.dmn.platform.AbstractDmnJobHandler;
import io.zeebe.client.ZeebeClient;
import io.zeebe.client.api.subscription.JobWorker;

@Component
public class ZeebeWorkerDmn {

  private static final String DMN_JOB_TYPE = "DMN";

  private static final Logger LOG = LoggerFactory.getLogger(ZeebeWorkerDmn.class);
    
  @Value("${zeebe.bufferSize:32}")
  private int bufferSize = 32;  
  
  @Autowired
  private AbstractDmnJobHandler jobHandler;

  @Autowired
  private ZeebeClient zeebeClient;

  private JobWorker jobWorker;

  @PostConstruct
  public void start() {
    jobWorker = zeebeClient.newWorker() //
        .jobType(DMN_JOB_TYPE) //
        .handler(jobHandler) //
        .name("camunda-dmn") //
        .timeout(Duration.ofSeconds(10)) //
        .bufferSize(bufferSize) //
        .open();

    LOG.info("Started Zeebe DMN worker.");
  }

  @PreDestroy
  public void close() {
    if (jobWorker != null && jobWorker.isOpen()) {
      jobWorker.close();
    }
    zeebeClient.close();

    LOG.info("Zeebe worker closed.");
  }

}
