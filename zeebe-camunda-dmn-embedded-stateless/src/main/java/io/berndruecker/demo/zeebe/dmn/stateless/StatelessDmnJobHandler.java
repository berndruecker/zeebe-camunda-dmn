package io.berndruecker.demo.zeebe.dmn.stateless;

import java.util.Map;

import org.camunda.bpm.dmn.engine.DmnDecision;
import org.camunda.bpm.dmn.engine.DmnDecisionResult;
import org.camunda.bpm.dmn.engine.DmnEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("dmnJobHandler")
public class StatelessDmnJobHandler extends AbstractDmnJobHandler {
  
  @Autowired
  private DirectoryBackedDmnRepository repository;  
  
  @Autowired
  private DmnEngine dmnEngine;  

  public DmnDecisionResult evaluateDecisionById(String decisionId, Map<String, Object> variables) {
    DmnDecision decision = repository.findDecisionById(decisionId);
    if (decision == null) {
      throw new RuntimeException(String.format("No decision found with id: '%s'", decisionId));
    }
    DmnDecisionResult decisionResult = //
        dmnEngine.evaluateDecision(decision, variables);
    return decisionResult;
  }

}
