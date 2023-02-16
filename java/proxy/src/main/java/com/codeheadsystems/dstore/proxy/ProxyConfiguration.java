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

package com.codeheadsystems.dstore.proxy;

import com.codeheadsystems.dstore.common.config.EtcdConfiguration;
import io.dropwizard.Configuration;

/**
 * Configuration for the proxy.
 */
public class ProxyConfiguration extends Configuration {
  private EtcdConfiguration etcdConfiguration;

  /**
   * Getter.
   *
   * @return value.
   */
  public EtcdConfiguration getEtcdConfiguration() {
    return etcdConfiguration;
  }

  /**
   * Setter.
   *
   * @param etcdConfiguration to set.
   */
  public void setEtcdConfiguration(final EtcdConfiguration etcdConfiguration) {
    this.etcdConfiguration = etcdConfiguration;
  }
}