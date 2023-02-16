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

import com.codeheadsystems.dstore.common.config.EtcdConfiguration;
import com.codeheadsystems.dstore.proxy.ProxyConfiguration;
import dagger.Module;
import dagger.Provides;
import io.dropwizard.Configuration;
import javax.inject.Singleton;

/**
 * For setting up the configuration for the service.
 */
@Module
public class ProxyConfigurationModule {

  /**
   * Converts the configuration to a proxy configuration.
   *
   * @param configuration from dropwizard.
   * @return our type.
   */
  @Provides
  @Singleton
  public ProxyConfiguration configuration(final Configuration configuration) {
    return (ProxyConfiguration) configuration;
  }

  /**
   * Provider for the etcd configuration.
   *
   * @param configuration from us.
   * @return etcd config.
   */
  @Provides
  @Singleton
  public EtcdConfiguration etcdConfiguration(final ProxyConfiguration configuration) {
    return configuration.getEtcdConfiguration();
  }

}