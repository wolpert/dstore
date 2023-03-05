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

package org.svarm.endtoend;

import static org.svarm.control.javaclient.module.ControlServiceModule.CONTROL_SERVICE_CONNECTION_URL;

import com.codeheadsystems.metrics.Metrics;
import com.fasterxml.jackson.databind.ObjectMapper;
import dagger.Component;
import dagger.Module;
import dagger.Provides;
import feign.FeignException;
import io.etcd.jetcd.Client;
import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.micrometer.tagged.TaggedRetryMetrics;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import javax.inject.Named;
import javax.inject.Singleton;
import org.svarm.common.config.EtcdConfiguration;
import org.svarm.common.config.ImmutableEtcdConfiguration;
import org.svarm.common.config.accessor.EtcdAccessor;
import org.svarm.common.config.module.EtcdModule;
import org.svarm.common.engine.TraceUuidEngine;
import org.svarm.common.javaclient.JavaClientModule;
import org.svarm.common.module.JsonModule;
import org.svarm.control.common.api.ControlNodeService;
import org.svarm.control.common.api.ControlTenantResourceService;
import org.svarm.control.javaclient.module.ControlServiceModule;
import org.svarm.proxy.common.api.ProxyService;
import org.svarm.proxy.javaclient.module.ProxyServiceModule;

@Component(modules = {
    SvarmComponent.Configuration.class,
    EtcdModule.class,
    JavaClientModule.class,
    JsonModule.class,
    ControlServiceModule.class,
    ProxyServiceModule.class
})
@Singleton
public interface SvarmComponent {

  ControlNodeService controlNodeService();

  ControlTenantResourceService controlTenantResourceService();

  ProxyService proxyService();

  Client client();

  TraceUuidEngine traceUuidEngine();

  EtcdAccessor etcdAccessor();

  ObjectMapper objectMapper();

  Retry retry();

  @Module
  class Configuration {
    @Provides
    @Singleton
    public EtcdConfiguration etcdConfiguration() {
      return ImmutableEtcdConfiguration.builder().target("ip:///localhost:2379").build();
    }


    @Provides
    @Singleton
    @Named(CONTROL_SERVICE_CONNECTION_URL)
    String controlServiceConnectionUrl() {
      return "http://localhost:9090/";
    }


    @Provides
    @Singleton
    @Named(ProxyServiceModule.PROXY_SERVICE_CONNECTION_URL)
    String proxyServiceConnectionUrl() {
      return "http://localhost:8180/";
    }

    @Provides
    @Singleton
    Retry retry(/* final Metrics metrics*/) {
      final RetryConfig config = RetryConfig.custom()
          .maxAttempts(3)
          .retryExceptions(FeignException.FeignClientException.class)
          .intervalFunction(IntervalFunction.ofExponentialBackoff(100, 2))
          .failAfterMaxAttempts(true)
          .build();
      final RetryRegistry registry = RetryRegistry.of(config);
//      TaggedRetryMetrics.ofRetryRegistry(registry)
//          .bindTo(metrics.registry());
      return registry.retry("DEFAULT");
    }
  }

}
