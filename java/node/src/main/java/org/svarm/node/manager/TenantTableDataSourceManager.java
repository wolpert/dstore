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

package org.svarm.node.manager;

import com.codeheadsystems.metrics.Metrics;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalNotification;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.svarm.node.engine.DatabaseEngine;
import org.svarm.node.engine.DatabaseInitializationEngine;
import org.svarm.node.model.TenantTable;
import org.svarm.node.utils.TagHelper;

/**
 * Provides datasources of type tenant. Responsible for generating and maintaining. This caches.
 */
@Singleton
public class TenantTableDataSourceManager {
  private static final Logger LOGGER = LoggerFactory.getLogger(TenantTableDataSourceManager.class);

  private final LoadingCache<TenantTable, DataSource> tenantDataSourceLoadingCache;
  private final DatabaseEngine databaseEngine;
  private final DatabaseInitializationEngine databaseInitializationEngine;
  private final Metrics metrics;

  /**
   * Default constructor for the DSM.
   *
   * @param databaseEngine               to get new data sources.
   * @param databaseInitializationEngine to initialize the database.
   * @param metrics                      to track.
   */
  @Inject
  public TenantTableDataSourceManager(final DatabaseEngine databaseEngine,
                                      final DatabaseInitializationEngine databaseInitializationEngine,
                                      final Metrics metrics) {
    LOGGER.info("TenantTableDataSourceManager({},{})", databaseEngine, databaseInitializationEngine);
    this.metrics = metrics;
    this.databaseEngine = databaseEngine;
    this.databaseInitializationEngine = databaseInitializationEngine;
    this.tenantDataSourceLoadingCache = CacheBuilder.newBuilder()
        .maximumSize(1000)
        .removalListener(this::onRemoval)
        .build(CacheLoader.from(this::generate));
  }

  /**
   * Gets the data source for the tenant.
   *
   * @param tenantTable to get the source for.
   * @return the source.
   */
  public DataSource getDataSource(final TenantTable tenantTable) {
    LOGGER.trace("getDataSource({})", tenantTable);
    metrics.counter("TenantTableDataSourceManager.getDataSource", TagHelper.from(tenantTable)).increment();
    return tenantDataSourceLoadingCache.getUnchecked(tenantTable);
  }

  /**
   * Returns the current map. Useful for health checks.
   *
   * @return map of the tenants.
   */
  public Map<TenantTable, DataSource> allValues() {
    return tenantDataSourceLoadingCache.asMap();
  }

  /**
   * Removes tenant from the cache.
   *
   * @param tenantTable to remove.
   */
  public void evictTenant(final TenantTable tenantTable) {
    LOGGER.trace("evictTenant({})", tenantTable);
    metrics.counter("TenantTableDataSourceManager.evictTenant", TagHelper.from(tenantTable)).increment();
    tenantDataSourceLoadingCache.invalidate(tenantTable);
  }

  private void onRemoval(RemovalNotification<TenantTable, DataSource> notification) {
    LOGGER.debug("onRemoval({},{})", notification.getKey(), notification.getCause());
    metrics.counter("TenantTableDataSourceManager.onRemoval", TagHelper.from(notification.getKey())).increment();
  }

  /**
   * Generate a new data source for the tenant table. This is not cached.
   *
   * @param tenantTable the tenant table to use.
   * @return the data source.
   */
  private DataSource generate(final TenantTable tenantTable) {
    LOGGER.debug("dataSource({})", tenantTable);
    final DataSource dataSource = databaseEngine.tenantDataSource(tenantTable);
    try {
      LOGGER.trace("Getting connection");
      final Connection connection = dataSource.getConnection();
      databaseInitializationEngine.initialize(connection, tenantTable.tableVersion());
      return dataSource;
    } catch (SQLException e) {
      throw new IllegalArgumentException("Unable to get tenant initialized connection", e);
    }
  }

  /**
   * Invalidates the cache entry, deletes the database.
   *
   * @param tenantTable to delete.
   */
  public void deleteEverything(final TenantTable tenantTable) {
    LOGGER.info("deleteEverything({})", tenantTable.identifier());
    metrics.counter("TenantTableDataSourceManager.deleteEverything", TagHelper.from(tenantTable)).increment();
    evictTenant(tenantTable);
    databaseEngine.deleteTenantDataStoreLocation(tenantTable);
  }
}