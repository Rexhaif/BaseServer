package com.rextech.base.handlers;

import com.rextech.base.utils.AbstractProtectionHandler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

/**
 * Created by Даниил on 11.05.2016.
 */
public class ValidAllProtectionHandler extends AbstractProtectionHandler {
    @Override
    protected boolean validateRequest(String path, String method, RoutingContext rtx) {
        return true;
    }

    @Override
    public void configure(JsonObject config) {

    }
}
