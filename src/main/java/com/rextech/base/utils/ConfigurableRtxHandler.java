package com.rextech.base.utils;

import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public interface ConfigurableRtxHandler extends Handler<RoutingContext> {

    /**
     * Sets up configuration
     * @param config json configuration
     */
    void configure(JsonObject config);

}
