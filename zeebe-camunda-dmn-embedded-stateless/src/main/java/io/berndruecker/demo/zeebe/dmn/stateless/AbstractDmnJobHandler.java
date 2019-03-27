package io.berndruecker.demo.zeebe.dmn.stateless;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.dmn.engine.DmnDecisionResult;

import io.zeebe.client.api.clients.JobClient;
import io.zeebe.client.api.response.ActivatedJob;
import io.zeebe.client.api.subscription.JobHandler;

public abstract class AbstractDmnJobHandler implements JobHandler {

  public static String DECISION_ID_HEADER = "decisionRef";
  public static String DECISION_RESULT_MAPPING = "decisionResultMapping";
  public static String DECISION_RESULT_VARIABLE = "decisionResultVariable";

  @Override
  public void handle(JobClient client, ActivatedJob job) {
    String decisionId = readHeader(job, DECISION_ID_HEADER);

    // always add the workflowInstanceId (to allow for capturing it in the history)
    Map<String, Object> variables = new HashMap<>();
    variables.put("workflowInstanceKey", String.valueOf(job.getHeaders().getWorkflowInstanceKey()));
    variables.put("workflowJobKey", String.valueOf(job.getKey()));
    // and add complete payload
    variables.putAll(job.getPayloadAsMap());
    
    DmnDecisionResult decisionResult = evaluateDecisionById(decisionId, variables);
    
    Map<String, Object> result = new HashMap<>();
    result.putAll(unwrapResult(decisionResult, job));    
    addAdditionalMetadata(job, result);
    
    client.newCompleteCommand(job.getKey()) //
        .payload(result) //
        .send().join();
  }

  public void addAdditionalMetadata(ActivatedJob job, Map<String, Object> result) {
    // empty default implementation
  }

  public abstract DmnDecisionResult evaluateDecisionById(String decisionId, Map<String, Object> variables);

  public Map<String, Object> unwrapResult(final DmnDecisionResult decisionResult, ActivatedJob job) {
    // TODO: Add support for DECISION_RESULT_MAPPING!
    // Currently this is the only supported mapping
    // decisionResultMapping = "singleEntry";

    String decisionResultVariable = readHeader(job, DECISION_RESULT_VARIABLE);

    return Collections.singletonMap(decisionResultVariable, decisionResult.getSingleEntry());
  }

  private String readHeader(ActivatedJob job, String customHeaderName) {
    String headerValue = (String) job.getCustomHeaders().get(customHeaderName);
    if (headerValue == null || headerValue.isEmpty()) {
      throw new RuntimeException(String.format("Missing header: '%d'", customHeaderName));
    }
    return headerValue;
  }

}
