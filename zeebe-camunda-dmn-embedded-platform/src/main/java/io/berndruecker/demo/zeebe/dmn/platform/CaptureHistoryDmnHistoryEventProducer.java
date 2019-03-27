package io.berndruecker.demo.zeebe.dmn.platform;

import org.camunda.bpm.dmn.engine.delegate.DmnDecisionLogicEvaluationEvent;
import org.camunda.bpm.engine.impl.history.event.HistoricDecisionInstanceEntity;
import org.camunda.bpm.engine.impl.history.producer.DefaultDmnHistoryEventProducer;

public class CaptureHistoryDmnHistoryEventProducer extends DefaultDmnHistoryEventProducer {
  
  private static ThreadLocal<HistoricDecisionInstanceEntity> latestHistoricDecisionInstance = new ThreadLocal<>();

  protected HistoricDecisionInstanceEntity createDecisionEvaluatedEvt(DmnDecisionLogicEvaluationEvent evaluationEvent, HistoricDecisionInstanceEntity rootDecisionInstance) {
    // This only works for evaluations not happening in the context of a Camunda BPMN/CMMN
    HistoricDecisionInstanceEntity historicDecisionInstanceEntity = super.createDecisionEvaluatedEvt(evaluationEvent, rootDecisionInstance);
    latestHistoricDecisionInstance.set(historicDecisionInstanceEntity);
    return historicDecisionInstanceEntity;

  }
  
  public static String getLatestHistoricDecisionInstanceId() {
    return latestHistoricDecisionInstance.get().getId();
  }

}
