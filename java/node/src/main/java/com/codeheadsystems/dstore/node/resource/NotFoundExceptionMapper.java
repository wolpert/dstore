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

package com.codeheadsystems.dstore.node.resource;

import static com.codahale.metrics.MetricRegistry.name;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codeheadsystems.server.exception.NotFoundException;
import com.codeheadsystems.server.resource.JerseyResource;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Mapper for NotFoundExceptions.
 */
@Singleton
public class NotFoundExceptionMapper implements JerseyResource, ExceptionMapper<NotFoundException> {

  private final Meter exceptions;

  /**
   * Constructor.
   *
   * @param registry doing this the dropwizard way.
   */
  @Inject
  public NotFoundExceptionMapper(final MetricRegistry registry) {
    exceptions = registry.meter(name(getClass(), "exceptions"));
  }

  /**
   * Convert the exception to a response.
   *
   * @param exception the exception to map to a response.
   * @return the 404 response.
   */
  @Override
  public Response toResponse(final NotFoundException exception) {
    exceptions.mark();
    return Response.status(Response.Status.NOT_FOUND).build();
  }
}
