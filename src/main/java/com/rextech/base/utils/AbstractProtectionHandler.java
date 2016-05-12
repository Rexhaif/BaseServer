package com.rextech.base.utils;

import io.vertx.ext.web.RoutingContext;

/**
 * Created by Даниил on 11.05.2016.
 */
public abstract class AbstractProtectionHandler implements ConfigurableRtxHandler {

    @Override
    public void handle(RoutingContext context) {
        if (validateRequest(context.request().path(), context.request().method().toString(), context)) {
            context.next();
        } else {
            context.response().setStatusCode(403).end();
        }
    }

    protected abstract boolean validateRequest(String path, String method, RoutingContext rtx);
}
