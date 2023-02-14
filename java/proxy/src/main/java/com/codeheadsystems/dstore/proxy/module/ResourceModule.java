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

package com.codeheadsystems.dstore.proxy.module;

import com.codeheadsystems.dstore.proxy.resource.ProxyResource;
import com.codeheadsystems.server.resource.JerseyResource;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoSet;

/**
 * Resources.
 */
@Module
public interface ResourceModule {

  /**
   * The proxy resource.
   *
   * @param resource to bind.
   * @return a jersey resource.
   */
  @Binds
  @IntoSet
  JerseyResource proxyResource(final ProxyResource resource);

}
