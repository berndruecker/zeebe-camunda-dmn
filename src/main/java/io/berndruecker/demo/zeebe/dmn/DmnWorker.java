package io.berndruecker.demo.zeebe.dmn;

import java.time.Duration;

import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.dmn.engine.DmnEngineConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.zeebe.client.ZeebeClient;
import io.zeebe.client.api.subscription.JobWorker;

public class DmnWorker {

  private static final Logger LOG = LoggerFactory.getLogger(DmnWorker.class);

  private final String dmnFilesDirectory;

  private ZeebeClient zeebeClient;

  private JobWorker jobWorker;

  public DmnWorker(String dmnFilesDirectory) {
    this.dmnFilesDirectory = dmnFilesDirectory;
  }

  public void start() {
    // TODO Add properties to connect to some port!!!
    zeebeClient = ZeebeClient.newClientBuilder().defaultJobPollInterval(Duration.ofMillis(50)).build();

    LOG.debug("Connected.");

    DmnEngine dmnEngine = buildDmnEngine();
    DmnRepository repository = new DmnRepository(dmnFilesDirectory, dmnEngine);

    DmnJobHandler jobHandler = new DmnJobHandler(repository, dmnEngine);

    jobWorker = zeebeClient.jobClient().newWorker() //
          .jobType("DMN") //
          .handler(jobHandler) //
          .name("camunda-dmn") //
          .timeout(Duration.ofSeconds(10)) //
          .open();
    
    LOG.debug("Started.");
  }

  private DmnEngine buildDmnEngine() {
    return DmnEngineConfiguration.createDefaultDmnEngineConfiguration().buildEngine();
  }

  public void close() {
    if (jobWorker != null && jobWorker.isOpen()) {
      jobWorker.close();
    }
    zeebeClient.close();

    LOG.debug("Closed.");
  }

}
