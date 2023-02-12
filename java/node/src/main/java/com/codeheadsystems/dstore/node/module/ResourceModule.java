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

package com.codeheadsystems.dstore.node.module;

import com.codeheadsystems.dstore.node.manager.ControlPlaneWatcherManager;
import com.codeheadsystems.dstore.node.resource.TenantResource;
import com.codeheadsystems.dstore.node.resource.TenantTableEntryResource;
import com.codeheadsystems.dstore.node.resource.TenantTableResource;
import com.codeheadsystems.server.resource.JerseyResource;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoSet;
import io.dropwizard.lifecycle.Managed;

/**
 * List of resource implementations we support.
 */
@Module
public interface ResourceModule {

  /**
   * Tenant resource.
   *
   * @param resource resource
   * @return JerseyResource.
   */
  @Binds
  @IntoSet
  JerseyResource tenantResource(TenantResource resource);

  /**
   * Tenant table resource.
   *
   * @param resource resource
   * @return JerseyResource.
   */
  @Binds
  @IntoSet
  JerseyResource tenantTableResource(TenantTableResource resource);

  /**
   * Tenant table entry resource.
   *
   * @param resource resource
   * @return JerseyResource.
   */
  @Binds
  @IntoSet
  JerseyResource tenantTableEntryResource(TenantTableEntryResource resource);

  /**
   * Managed resource: control plane.
   *
   * @param resource control plane watcher.
   * @return managed.
   */
  @Binds
  @IntoSet
  Managed controlPlaneWatcherManager(ControlPlaneWatcherManager resource);

}
