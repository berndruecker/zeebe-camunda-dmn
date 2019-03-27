package io.berndruecker.demo.zeebe.dmn.platform;

import org.camunda.bpm.dmn.engine.delegate.DmnDecisionEvaluationEvent;
import org.camunda.bpm.dmn.engine.delegate.DmnDecisionLogicEvaluationEvent;
import org.camunda.bpm.engine.impl.history.event.HistoricDecisionInstanceEntity;
import org.camunda.bpm.engine.impl.history.event.HistoryEvent;
import org.camunda.bpm.engine.impl.history.producer.DefaultDmnHistoryEventProducer;

public class CaptureHistoryDmnHistoryEventProducer extends DefaultDmnHistoryEventProducer {
  
  private static ThreadLocal<HistoricDecisionInstanceEntity> latestHistoricDecisionInstance = new ThreadLocal<>();

  public HistoryEvent createDecisionEvaluatedEvt(final DmnDecisionEvaluationEvent evaluationEvent) {
    return createHistoryEvent(evaluationEvent, new HistoricDecisionInstanceSupplier() {

      public HistoricDecisionInstanceEntity createHistoricDecisionInstance(DmnDecisionLogicEvaluationEvent evaluationEvent, HistoricDecisionInstanceEntity rootDecisionInstance) {
         HistoricDecisionInstanceEntity historicDecisionInstanceEntity = createDecisionEvaluatedEvt(evaluationEvent, rootDecisionInstance);
         latestHistoricDecisionInstance.set(historicDecisionInstanceEntity);         
         return historicDecisionInstanceEntity;
      }
    });
  }
  
  public static String getLatestHistoricDecisionInstanceId() {
    return latestHistoricDecisionInstance.get().getId();
  }

}
