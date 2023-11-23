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

package org.svarm.control.common.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

/**
 * Provides details on the node info.
 */
@Value.Immutable
@JsonSerialize(as = ImmutableNodeMetaData.class)
@JsonDeserialize(builder = ImmutableNodeMetaData.Builder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface NodeMetaData {

  /**
   * Host name.
   *
   * @return the value.
   */
  String host();

  /**
   * Port.
   *
   * @return the value.
   */
  Integer port();

  /**
   * Uri string.
   *
   * @return the string
   */
  String uri();

}
