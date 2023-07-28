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

package org.svarm.common.config.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

/**
 * Identifies the tenant with it's resource.
 */
@Value.Immutable
@JsonSerialize(as = ImmutableTenantResource.class)
@JsonDeserialize(builder = ImmutableTenantResource.Builder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface TenantResource {

  /**
   * The tenant.
   *
   * @return value. string
   */
  @JsonProperty("tenant")
  String tenant();

  /**
   * The resource.
   *
   * @return value. string
   */
  @JsonProperty("resource")
  String resource();

}
