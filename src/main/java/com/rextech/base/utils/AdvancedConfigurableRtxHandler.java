package com.rextech.base.utils;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public abstract class AdvancedConfigurableRtxHandler implements ConfigurableRtxHandler {

    protected JsonObject config;

    @Override
    public void configure(JsonObject config) {
        this.config = config;
    }

    @Override
    public void handle(RoutingContext context) {
        HttpMethod method = context.request().method();
        switch (method) {
            case GET:
                handleGet(context);
                break;
            case POST:
                handlePost(context);
                break;
            case PUT:
                handlePut(context);
                break;
            case DELETE:
                handleDelete(context);
                break;
            case HEAD:
                handleHead(context);
                break;
            case OPTIONS:
                handleOptions(context);
                break;
            case PATCH:
                handlePatch(context);
                break;
            case CONNECT:
                handleConnect(context);
                break;
            case TRACE:
                handleTrace(context);
                break;
            default:
                context.response().setStatusCode(418).end();
                break;
        }
    }

    protected void handleGet(RoutingContext rtx) {
        rtx.response().setStatusCode(418).end();
    }

    protected void handlePost(RoutingContext rtx) {
        rtx.response().setStatusCode(418).end();
    }

    protected void handlePut(RoutingContext rtx) {
        rtx.response().setStatusCode(418).end();
    }

    protected void handleDelete(RoutingContext rtx) {
        rtx.response().setStatusCode(418).end();
    }

    protected void handleHead(RoutingContext rtx) {
        rtx.response().setStatusCode(418).end();
    }

    protected void handleOptions(RoutingContext rtx) {
        rtx.response().setStatusCode(418).end();
    }

    protected void handlePatch(RoutingContext rtx) {
        rtx.response().setStatusCode(418).end();
    }

    protected void handleConnect(RoutingContext rtx) {
        rtx.response().setStatusCode(418).end();
    }

    protected void handleTrace(RoutingContext rtx) {
        rtx.response().setStatusCode(418).end();
    }
}
