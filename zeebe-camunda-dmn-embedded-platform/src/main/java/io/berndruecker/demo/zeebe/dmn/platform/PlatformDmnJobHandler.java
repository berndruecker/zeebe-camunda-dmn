package io.berndruecker.demo.zeebe.dmn.platform;

import java.util.Map;

import org.camunda.bpm.dmn.engine.DmnDecisionResult;
import org.camunda.bpm.engine.ProcessEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.zeebe.client.api.response.ActivatedJob;

@Component("dmnJobHandler")
public class PlatformDmnJobHandler extends AbstractDmnJobHandler {
  
  @Autowired
  private ProcessEngine camunda;  

  public DmnDecisionResult evaluateDecisionById(String decisionId, Map<String, Object> variables) {    
    return camunda.getDecisionService().evaluateDecisionByKey(decisionId)
          .variables(variables)
          .evaluate();       
  }
  
  public void addAdditionalMetadata(ActivatedJob job, Map<String, Object> result) {
    // Capture the Camunda HistoricDecisionInstanceId connected to the current job Id
    // to allow to connect both later in monitoring if required
    result.put(
        "historyDmnDecisionInstanceId_" + job.getKey(), 
        CaptureHistoryDmnHistoryEventProducer.getLatestHistoricDecisionInstanceId());
  }   

}
