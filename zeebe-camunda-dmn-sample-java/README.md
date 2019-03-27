# Zeebe Workflow with a DMN decision task

This small Java example:

* Deploys a workflow onto Zeebe that has a DMN decision task as [described in the overview](../../../)
* Starts a worker to do sysouts (so that we can see the DMN got evaluated correctly)
* Starts two workflow instances to get the ball rolling

Build with Maven

`mvn clean install`

Execute the JAR file via

`java -jar target/zeebe-camunda-dmn-sample.jar`

You should see some output like this (if the Camunda DMN was started properly before that, see [alternatives](../../../)).

```
Deployed DeploymentEventImpl{key=66346, workflows=[WorkflowImpl{workflowKey=66345, bpmnProcessId='dmn-example', version=19, resourceName='dmn-example.bpmn'}]}
Sysout worker started
Started workflow instance CreateWorkflowInstanceResponseImpl{workflowKey=66345, bpmnProcessId='dmn-example', version=19, workflowInstanceKey=66349}
Started workflow instance CreateWorkflowInstanceResponseImpl{workflowKey=66345, bpmnProcessId='dmn-example', version=19, workflowInstanceKey=66356}
yes # Camunda | {approved=true, someText=Camunda, historyDmnDecisionInstanceId_66355=4a7f4660-5088-11e9-afbd-30243246dd0d}
no # Not Camunda | {approved=false, someText=Not Camunda, historyDmnDecisionInstanceId_66362=4a83da44-5088-11e9-afbd-30243246dd0d}

```

