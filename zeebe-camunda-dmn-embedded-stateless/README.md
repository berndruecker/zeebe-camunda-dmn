# Zeebe Job Worker + Stateless Camunda DMN Engine as Spring Boot App

This sample application combines a Zeebe [Job Worker](https://docs.zeebe.io/basics/job-workers.html) with a stateless Camunda DMN engine evaluate decisions as part of BPMN workflows. See [repository root](../../../) for a general introduction.

DMN models are read from a local directory, that can be configured in `application.properties`

```
dmn.files.dir=./
```

In this `application.properties` you can also configure the Zeebe endpoint:

```
zeebe.brokerContactPoint=localhost:26500
```


## How to run

Build with Maven

`mvn clean install`

Execute the JAR file via

`java -jar target/zeebe-camunda-dmn-embedded-stateless.jar`

Now you can [run a workflow in Zeebe](../../../zeebe-camunda-dmn-sample-java) and see the DMN being evaluated.