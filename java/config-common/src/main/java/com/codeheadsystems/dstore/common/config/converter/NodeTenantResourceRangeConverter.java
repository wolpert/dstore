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

package com.codeheadsystems.dstore.common.config.converter;

import static org.slf4j.LoggerFactory.getLogger;

import com.codeheadsystems.dstore.common.config.api.ImmutableNodeTenantResource;
import com.codeheadsystems.dstore.common.config.api.ImmutableNodeTenantResourceRange;
import com.codeheadsystems.dstore.common.config.api.ImmutableTenantResource;
import com.codeheadsystems.dstore.common.config.api.NodeTenantResource;
import com.codeheadsystems.dstore.common.config.api.NodeTenantResourceRange;
import com.codeheadsystems.dstore.common.config.api.Range;
import com.codeheadsystems.dstore.common.config.api.TenantResource;
import com.codeheadsystems.dstore.common.engine.JsonEngine;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.slf4j.Logger;

/**
 * Converter for the node tenant resource range.
 */
@Singleton
public class NodeTenantResourceRangeConverter {

  private static final Logger LOGGER = getLogger(NodeTenantResourceRangeConverter.class);

  private final JsonEngine jsonEngine;

  /**
   * Constructor.
   *
   * @param jsonEngine for conversion.
   */
  @Inject
  public NodeTenantResourceRangeConverter(final JsonEngine jsonEngine) {
    this.jsonEngine = jsonEngine;
    LOGGER.info("NodeTenantResourceRangeConverter({})", jsonEngine);
  }

  /**
   * Converter for the map you get from etcd to the list of node tenant resource ranges.
   *
   * @param configurationMap map.
   * @return list.
   */
  public List<NodeTenantResourceRange> from(final Map<String, String> configurationMap) {
    LOGGER.trace("from({})", configurationMap);
    return configurationMap.entrySet().stream()
        .map(e -> fromKeyValue(e.getKey(), e.getValue()))
        .collect(Collectors.toList());
  }

  /**
   * Converter.
   *
   * @param key   in format: node/{uuid}/id/{tenant}/{tenantResource}
   * @param value in format: {"lowHash":0,"highHash":32767}
   * @return a note tenant resource range.
   */
  public NodeTenantResourceRange fromKeyValue(final String key, final String value) {
    LOGGER.trace("toNodeTenantResourceRange({},{})", key, value);
    final String[] tokens = key.split("/");
    final Range range = jsonEngine.readValue(value, Range.class);
    final TenantResource tenantResource = ImmutableTenantResource.builder()
        .tenant(tokens[3])
        .resource(tokens[4])
        .build();
    final NodeTenantResource nodeTenantResource = ImmutableNodeTenantResource.builder()
        .tenantResource(tenantResource)
        .uuid(tokens[1])
        .build();
    return ImmutableNodeTenantResourceRange.builder()
        .range(range)
        .nodeTenantResource(nodeTenantResource)
        .build();
  }

}