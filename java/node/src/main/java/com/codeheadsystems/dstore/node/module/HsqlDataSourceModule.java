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

package com.codeheadsystems.dstore.node.module;

import com.codeheadsystems.dstore.node.engine.DatabaseEngine;
import com.codeheadsystems.dstore.node.engine.impl.HsqlDatabaseEngine;
import dagger.Binds;
import dagger.Module;
import javax.inject.Singleton;

/**
 * Use this module if you want HSQL support.
 */
@Module
public interface HsqlDataSourceModule {

  /**
   * Enables HSQL Database Engine for us to use.
   *
   * @param engine the database engine.
   * @return as a database engine instance.
   */
  @Binds
  @Singleton
  DatabaseEngine hsqlDatabaseEngine(HsqlDatabaseEngine engine);

}
