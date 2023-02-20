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

package org.svarm.node.converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.svarm.node.model.TenantTableIdentifier;

@ExtendWith(MockitoExtension.class)
class TenantTableInfoConverterTest {

  private static final String TABLENAME = "tablename";
  @Mock private TenantTableIdentifier tenantTableIdentifier;

  @InjectMocks private TenantTableInfoConverter converter;

  @Test
  void from() {
    when(tenantTableIdentifier.tableName()).thenReturn(TABLENAME);
    assertThat(converter.from(tenantTableIdentifier))
        .isNotNull()
        .hasFieldOrPropertyWithValue("id", TABLENAME);
  }

}