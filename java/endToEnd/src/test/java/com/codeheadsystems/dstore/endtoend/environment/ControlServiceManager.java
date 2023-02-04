/*
 * Copyright (c) 2023. Ned Wolpert
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

package com.codeheadsystems.dstore.endtoend.environment;


import com.codeheadsystems.dstore.control.Control;
import com.codeheadsystems.dstore.control.ControlConfiguration;
import com.codeheadsystems.dstore.endtoend.EnvironmentConfiguration;
import io.dropwizard.testing.DropwizardTestSupport;
import io.dropwizard.testing.ResourceHelpers;

public class ControlServiceManager implements ServiceManager {

  private static DropwizardTestSupport<ControlConfiguration> SUPPORT;

  @Override
  public void startup(EnvironmentConfiguration configuration) {
    SUPPORT = new DropwizardTestSupport<>(
        Control.class,
        ResourceHelpers.resourceFilePath("control-config.yaml")
        // TODO: put in the cluster endpoints into the config
    );
    try {
      SUPPORT.before();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    configuration.setControlConnectionUrl("http://localhost:" + SUPPORT.getLocalPort() + "/");
  }

  @Override
  public void shutdown(EnvironmentConfiguration configuration) {
    SUPPORT.after();
    configuration.setControlConnectionUrl(null);
  }
}
