package io.berndruecker.demo.zeebe.dmn;

import java.util.Collections;
import java.util.Map;

import org.camunda.bpm.dmn.engine.DmnDecision;
import org.camunda.bpm.dmn.engine.DmnDecisionResult;
import org.camunda.bpm.dmn.engine.DmnEngine;

import io.zeebe.client.api.clients.JobClient;
import io.zeebe.client.api.response.ActivatedJob;
import io.zeebe.client.api.subscription.JobHandler;

public class DmnJobHandler implements JobHandler {
  
  private final DmnRepository repository;
  private final DmnEngine dmnEngine;

  public DmnJobHandler(DmnRepository repository, DmnEngine dmnEngine) {
    this.repository = repository;
    this.dmnEngine = dmnEngine;
  }

  @Override
  public void handle(JobClient client, ActivatedJob job) {
    DmnDecision decision = findDecisionForTask(job);

    DmnDecisionResult decisionResult = //
        dmnEngine.evaluateDecision(decision, job.getPayloadAsMap());
    Map<String, Object> result = unrwapResult(decisionResult, job);
    
    client.newCompleteCommand(job.getKey()) //
        .payload(result) //
        .send().join();
  }

  private DmnDecision findDecisionForTask(ActivatedJob job) {
    String decisionId = (String) job.getCustomHeaders().get(StandaloneDmnApplication.DECISION_ID_HEADER);
    if (decisionId == null || decisionId.isEmpty()) {
      throw new RuntimeException(String.format("Missing header: '%d'", StandaloneDmnApplication.DECISION_ID_HEADER));
    }

    DmnDecision decision = repository.findDecisionById(decisionId);
    if (decision == null) {
      throw new RuntimeException(String.format("No decision found with id: '%s'", decisionId));
    }
    return decision;
  }

  private Map<String, Object> unrwapResult(final DmnDecisionResult decisionResult, ActivatedJob job) {
    // TODO: Add support for this!
    String decisionResultMapping = (String) job.getCustomHeaders().get(StandaloneDmnApplication.DECISION_RESULT_MAPPING);
    if (decisionResultMapping == null || decisionResultMapping.isEmpty()) {
      decisionResultMapping = "singleEntry";
    }

    // TODO: Make result name configurable
    return Collections.singletonMap("dmn-result", decisionResult.getSingleEntry());
  }

}
