/*
 * Copyright © 2017 camunda services GmbH (info@camunda.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.berndruecker.demo.zeebe.dmn;

import java.io.*;
import java.util.Properties;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StandaloneDmnApplication {
  
  private static final Logger LOG = LoggerFactory.getLogger(StandaloneDmnApplication.class);

  public static final String PROP_FILE = "application.properties";

  public static final String DMN_MODELS_DIR_PROP = "dmn.models.dir";
  private static final String DEFAULT_DMN_MODELS_DIR = "models";

  static final String DECISION_ID_HEADER = "decisionRef";

  public static final Object DECISION_RESULT_MAPPING = "decisionResultMapping";

  public static void main(String[] args) {
    Properties properties = loadProperties();

    // TODO Add properties to connect to some host/port for Zeebe!!!
    String repoDir = properties.getProperty(DMN_MODELS_DIR_PROP, DEFAULT_DMN_MODELS_DIR);

    DmnWorker application = new DmnWorker(repoDir);

    application.start();
    waitUntilClose();
    application.close();
  }

  private static Properties loadProperties() {
    final Properties properties = new Properties();

    try {
      final File propertyFile = new File(PROP_FILE);
      if (propertyFile.exists()) {
        properties.load(new FileInputStream(propertyFile));
      } else {
        LOG.debug("No configuration found '{}'. Use default values.", PROP_FILE);
      }
    } catch (IOException e) {
      LOG.warn("Failed to read properties '{}'.", PROP_FILE, e);
    }
    return properties;
  }

  private static void waitUntilClose() {
    try (Scanner scanner = new Scanner(System.in)) {
      while (scanner.hasNextLine()) {
        final String nextLine = scanner.nextLine();
        if (nextLine.contains("close")) {
          return;
        }
      }
    }
  }

}
