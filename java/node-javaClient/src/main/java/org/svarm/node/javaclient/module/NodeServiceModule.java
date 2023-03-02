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

package org.svarm.node.javaclient.module;

import dagger.BindsOptionalOf;
import dagger.Module;
import dagger.Provides;
import feign.Feign;
import java.util.Optional;
import javax.inject.Named;
import javax.inject.Singleton;
import org.svarm.node.api.NodeTenantService;
import org.svarm.node.api.NodeTenantTableEntryService;
import org.svarm.node.api.NodeTenantTableService;

/**
 * Module for the node service.
 */
@Module(includes = {NodeServiceModule.Binder.class})
public class NodeServiceModule {

  /**
   * The client's node.
   */
  public static final String NODE_SERVICE_CONNECTION_URL = "NODE_SERVICE_CONNECTION_URL";
  private static final String INTERNAL_NODE_SERVICE_CONNECTION_URL = "INTERNAL_NODE_SERVICE_CONNECTION_URL";
  private final String connectionUrl;

  /**
   * Default constructor to use if you want to derive the connection url.
   */
  public NodeServiceModule() {
    this(null);
  }

  /**
   * Constructor.
   *
   * @param connectionUrl used to connect to node.
   */
  public NodeServiceModule(final String connectionUrl) {
    this.connectionUrl = connectionUrl;
  }

  /**
   * The real connection url.
   *
   * @param url the url.
   * @return the value.
   */
  @Provides
  @Singleton
  @Named(INTERNAL_NODE_SERVICE_CONNECTION_URL)
  public String internalControlServiceConnectionUrl(
      @Named(NODE_SERVICE_CONNECTION_URL) final Optional<String> url) {
    return url.orElse(connectionUrl);
  }

  /**
   * Get a usable node service.
   *
   * @param builder feign builder to use.
   * @param url     the url.
   * @return a node service.
   */
  @Provides
  @Singleton
  public NodeTenantService nodeTenantService(
      final Feign.Builder builder,
      final @Named(INTERNAL_NODE_SERVICE_CONNECTION_URL) String url) {
    return builder.target(NodeTenantService.class, url);
  }

  /**
   * Get a usable node service.
   *
   * @param builder feign builder to use.
   * @param url     the url.
   * @return a node service.
   */
  @Provides
  @Singleton
  public NodeTenantTableService nodeTenantTableService(
      final Feign.Builder builder,
      final @Named(INTERNAL_NODE_SERVICE_CONNECTION_URL) String url) {
    return builder.target(NodeTenantTableService.class, url);
  }

  /**
   * Get a usable node service.
   *
   * @param builder feign builder to use.
   * @param url     the url.
   * @return a node service.
   */
  @Provides
  @Singleton
  public NodeTenantTableEntryService nodeTenantTableEntryService(
      final Feign.Builder builder,
      final @Named(INTERNAL_NODE_SERVICE_CONNECTION_URL) String url) {
    return builder.target(NodeTenantTableEntryService.class, url);
  }

  /**
   * Binder so clients can do their own connection url.
   */
  @Module
  interface Binder {

    /**
     * Optional connection url declared by the clients.
     *
     * @return value.
     */
    @BindsOptionalOf
    @Named(NODE_SERVICE_CONNECTION_URL)
    String nodeServiceConnectionUrl();
  }
}
