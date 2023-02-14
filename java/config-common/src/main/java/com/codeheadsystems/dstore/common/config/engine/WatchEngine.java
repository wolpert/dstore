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

package com.codeheadsystems.dstore.common.config.engine;

import static org.slf4j.LoggerFactory.getLogger;

import com.codeheadsystems.dstore.common.config.accessor.EtcdAccessor;
import com.codeheadsystems.dstore.common.config.factory.WatchEngineFactory;
import com.codeheadsystems.metrics.Metrics;
import com.google.common.annotations.VisibleForTesting;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;
import io.etcd.jetcd.Watch;
import io.etcd.jetcd.watch.WatchResponse;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import org.slf4j.Logger;

/**
 * A watch engine so we are notified of what's going on.
 */
public class WatchEngine {

  private static final Logger LOGGER = getLogger(WatchEngine.class);

  private final String namespace;
  private final String key;
  private final Consumer<Event> eventConsumer;
  private final Metrics metrics;
  private final Watch.Watcher watcher;
  private final String tag;
  private final ExecutorService executorService;
  private final LinkedBlockingQueue<Event> queue;

  /**
   * Constructor.
   *
   * @param accessor      for talking to etcd.
   * @param namespace     the namespace.
   * @param key           the key.
   * @param eventConsumer who will get the events.
   * @param metrics       to track what's going on.
   */
  @AssistedInject
  public WatchEngine(final EtcdAccessor accessor,
                     final Metrics metrics,
                     @Assisted(WatchEngineFactory.NAMESPACE) final String namespace,
                     @Assisted(WatchEngineFactory.KEY) final String key,
                     @Assisted final Consumer<Event> eventConsumer) {
    this.metrics = metrics;
    this.tag = namespace + "/" + key;
    this.namespace = namespace;
    this.key = key;
    this.eventConsumer = eventConsumer;
    LOGGER.info("WatchEngine({},{},{})", namespace, key, eventConsumer);
    executorService = Executors.newSingleThreadExecutor();
    queue = new LinkedBlockingQueue<>();
    executorService.submit(this::handleEvent);
    watcher = accessor.watch(
        namespace,
        key,
        Watch.listener(this::watchResponse, this::error, this::complete));
  }

  private void handleEvent() {
    LOGGER.trace("{}:handleEvent()", tag);
    metrics.time("WatchEngine.handleEvent", () -> {
      try {
        final Event event = queue.take();
        LOGGER.trace("{}:handleEvent(): {}", tag, event);
        eventConsumer.accept(event);
      } catch (InterruptedException e) {
        LOGGER.warn("{}:handleEvent() : Interrupted", tag);
      }
      return null;
    });
  }

  /**
   * Closes the watcher and releases all resources.
   */
  public void close() {
    LOGGER.trace("{}:close()", tag);
    metrics.time("WatchEngine.close", () -> {
      watcher.close();
      executorService.shutdown();
      LOGGER.info("{}: Shutdown started, queue size: {}", tag, queue.size());
      return null;
    });
  }

  @VisibleForTesting
  int queueSize() {
    return queue.size();
  }

  @VisibleForTesting
  void watchResponse(final WatchResponse watchResponse) {
    LOGGER.trace("{}:watchResponse({})", tag, watchResponse);
    metrics.time("WatchEngine.watchResponse", () -> {
      watchResponse.getEvents().forEach(e -> {
        metrics.registry().counter("WatchEngine.watchResponse.event",
            "tag", tag, "type", e.getEventType().name()).increment();
        try {
          switch (e.getEventType()) {
            case PUT:
              queue.put(ImmutableEvent.builder()
                  .key(e.getKeyValue().getKey().toString())
                  .value(e.getKeyValue().getValue().toString())
                  .type(Event.Type.PUT)
                  .build());
              break;
            case DELETE:
              queue.put(ImmutableEvent.builder()
                  .key(e.getKeyValue().getKey().toString())
                  .value(e.getKeyValue().getValue().toString())
                  .type(Event.Type.DELETE)
                  .build());
              break;
            default:
              LOGGER.warn("{}: Unknown event: {}", tag, e);
          }
        } catch (InterruptedException ex) {
          LOGGER.error("{}: Unable to put event {}", tag, e, ex);
        }
      });
      return null;
    });
  }


  @VisibleForTesting
  void error(final Throwable throwable) {
    LOGGER.error("{}:error({})", tag, throwable.getMessage(), throwable);
    metrics.registry().counter("WatchEngine.error", "tag", tag).increment();
  }


  @VisibleForTesting
  void complete() {
    LOGGER.info("{}: Shutdown complete, queue size: {}", tag, queue.size());
    metrics.registry().counter("WatchEngine.complete", "tag", tag).increment();
    queue.clear();
  }

}