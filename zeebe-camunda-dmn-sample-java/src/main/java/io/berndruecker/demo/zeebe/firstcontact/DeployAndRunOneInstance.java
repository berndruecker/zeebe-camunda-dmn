package io.berndruecker.demo.zeebe.firstcontact;

import java.time.Duration;
import java.util.Collections;

import io.zeebe.client.ZeebeClient;
import io.zeebe.client.api.clients.JobClient;
import io.zeebe.client.api.events.DeploymentEvent;
import io.zeebe.client.api.events.WorkflowInstanceEvent;
import io.zeebe.client.api.response.ActivatedJob;
import io.zeebe.client.api.subscription.JobHandler;
import io.zeebe.client.api.subscription.JobWorker;

public class DeployAndRunOneInstance {

  public static void main(String[] args) {    
    ZeebeClient zeebe = ZeebeClient.newClient();
    
    DeploymentEvent deploymentEvent = zeebe.newDeployCommand() // 
        .addResourceFromClasspath("dmn-example.bpmn") // 
        .send().join();
    
    System.out.println("Deployed " + deploymentEvent);
    
    JobWorker worker = zeebe.newWorker() //
        .jobType("sysout") //
        .handler(new JobHandler() {
          
          @Override
          public void handle(JobClient client, ActivatedJob job) {
            System.out.println(
                job.getCustomHeaders().get("prefix") 
                + " # " 
                + job.getPayloadAsMap().get("someText")
                + " | "
                + job.getPayloadAsMap()
            );
            client.newCompleteCommand(job.getKey()).send().join();
          }
        })
        .timeout(Duration.ofSeconds(5))
        .open();
    
    System.out.println("Sysout worker started");
    
    WorkflowInstanceEvent workflowInstanceEvent = zeebe.newCreateInstanceCommand() //
      .bpmnProcessId("dmn-example") //
      .latestVersion() //
      .variables(Collections.singletonMap("someText", "Camunda"))
      .send().join();
    
    System.out.println("Started workflow instance " + workflowInstanceEvent);


    WorkflowInstanceEvent workflowInstanceEvent2 = zeebe.newCreateInstanceCommand() //
        .bpmnProcessId("dmn-example") //
        .latestVersion() //
        .variables(Collections.singletonMap("someText", "Not Camunda"))
        .send().join();
      
      System.out.println("Started workflow instance " + workflowInstanceEvent2);
}

}
