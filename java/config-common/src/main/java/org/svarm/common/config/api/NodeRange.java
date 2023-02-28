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
 * For a node availability with a tenant resource, Has range, inclusive start, exclusive end.
 */
@Value.Immutable
@JsonSerialize(as = ImmutableNodeRange.class)
@JsonDeserialize(builder = ImmutableNodeRange.Builder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface NodeRange {

  /**
   * The uuid.
   *
   * @return value.
   */
  @JsonProperty("uuid")
  String uuid();

  /**
   * The url.
   *
   * @return value.
   */
  @JsonProperty("uri")
  String uri();

  /**
   * The low value of the hash range, inclusive.
   *
   * @return value.
   */
  @JsonProperty("hash")
  Integer hash();

}