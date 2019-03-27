package io.berndruecker.demo.zeebe.dmn.stateless;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import org.camunda.bpm.dmn.engine.DmnDecision;
import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.model.dmn.Dmn;
import org.camunda.bpm.model.dmn.DmnModelInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
public class DirectoryBackedDmnRepository {
  
  private static final Logger LOG = LoggerFactory.getLogger(DirectoryBackedDmnRepository.class);

  @Value("${dmn.files.dir:#{null}}")
  private String dmnFilesDirectory;

  @Autowired
  private DmnEngine dmnEngine;

  private final Map<String, DmnDecision> decisionsById = new HashMap<>();

  public DmnDecision findDecisionById(String decisionId) {
    if (!decisionsById.containsKey(decisionId)) {
      scanDirectory(dmnFilesDirectory);
    }

    return decisionsById.get(decisionId);
  }

  private void scanDirectory(String directory) {
    LOG.debug("Scan directory: {}", directory);

    try {
      Path path = Paths.get(directory);
      LOG.info("Scan path: {}", path.toAbsolutePath());
      Files.walk(path).filter(isDmnFile()).forEach(this::readDmnFile);
    } catch (IOException e) {
      LOG.warn("Fail to scan directory: {}", directory, e);
    }
  }

  private void readDmnFile(Path dmnFile) {
    final String fileName = dmnFile.getFileName().toString();

    LOG.info("Reading DMN file: {}", dmnFile);

    try {
      final DmnModelInstance dmnModel = Dmn.readModelFromFile(dmnFile.toFile());

      dmnEngine.parseDecisions(dmnModel).forEach(decision -> {
        LOG.debug("Found decision with id '{}' in file: {}", decision.getKey(), fileName);

        decisionsById.put(decision.getKey(), decision);
      });
    } catch (Throwable t) {
      LOG.warn("Failed to parse decision: {}", fileName, t);
    }
  }

  private Predicate<Path> isDmnFile() {
    return p -> p.getFileName().toString().endsWith(".dmn");
  }

}
