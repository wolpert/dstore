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

package org.svarm.server.component;

import com.codahale.metrics.health.HealthCheck;
import io.dropwizard.lifecycle.Managed;
import java.util.Set;
import org.svarm.server.initializer.HealthCheckInitializer;
import org.svarm.server.initializer.JerseyResourceInitializer;
import org.svarm.server.initializer.ManagedObjectInitializer;
import org.svarm.server.resource.JerseyResource;

/**
 * Expected drop wizard sets needed for initialization.
 */
public interface DropWizardComponent {

  /**
   * Returns the resources initializer for the application.
   *
   * @return initializer.
   */
  JerseyResourceInitializer jerseyResourceInitializer();

  /**
   * Returns the health check initializer for the application.
   *
   * @return the health checks initializer.
   */
  HealthCheckInitializer healthCheckInitializer();

  /**
   * Initializer for pbjects that need their lifecycle managed.
   *
   * @return initializer.
   */
  ManagedObjectInitializer managedInitializer();

}
