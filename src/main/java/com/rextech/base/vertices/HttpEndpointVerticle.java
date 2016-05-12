package com.rextech.base.vertices;

import com.rextech.base.Env;
import com.rextech.base.utils.ConfigurableRtxHandler;
import com.rextech.base.utils.Utils;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.LoggerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpEndpointVerticle extends AbstractVerticle {

    private static Logger LOG = LoggerFactory.getLogger("HttpEndpoint");

    private HttpServer mServer;

    private Router mRouter;

    @Override
    public void start(Future<Void> startFuture) throws Exception {

        LOG.info("Initializing");
        mServer = Env.vertx().createHttpServer(
                new HttpServerOptions()
                        .setPort(config().getInteger("port"))
                        .setUsePooledBuffers(true)
                        .setTcpNoDelay(true)
                        .setCompressionSupported(true)
        );

        mRouter = Router.router(Env.vertx());
        mRouter.route().handler(BodyHandler.create());
        mRouter.route().handler(LoggerHandler.create());

        LOG.info("Loading protector");
        ConfigurableRtxHandler protector = Utils.<ConfigurableRtxHandler> getInstance(
                config().getJsonObject("protector").getString("className"),
                null
        );
        protector.configure(config().getJsonObject("protector").getJsonObject("config"));
        mRouter.route().handler(protector);
        LOG.info("Protector - (" + config().getJsonObject("protector").getString("className") + ") loaded");

        LOG.info("Loading handlers");
        config().getJsonArray("handlers").forEach(
                obj -> {
                    JsonObject handlerDescriptor = (JsonObject) obj;
                    String path = handlerDescriptor.getString("path");
                    LOG.info(
                            "Loading handler:->(" +
                                    handlerDescriptor.getString("className") +
                                    ") for path:->(" +
                                    path +
                                    ")"
                    );
                    ConfigurableRtxHandler handler = Utils.<ConfigurableRtxHandler> getInstance(
                            handlerDescriptor.getString("className"),
                            null
                    );
                    handler.configure(handlerDescriptor.getJsonObject("config"));
                    switch (handlerDescriptor.getString("pathType")) {
                        case "PLAIN" :
                            mRouter.route(path).handler(handler);
                            break;
                        case "REGEXP" :
                            mRouter.routeWithRegex(path).handler(handler);
                            break;
                        default:
                            LOG.error(
                                    "Handler cannot be loaded -> " +
                                    "Incorrect configuration parameter -> " +
                                    "-pathType must have only PLAIN or REGEXP value"
                            );
                            break;
                    }

                }
        );
        mServer.requestHandler(mRouter::accept);
        mServer.listen();
        LOG.info("Initialized");
        startFuture.complete();
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        LOG.info("Stopping");
        mRouter.clear();
        mServer.close();
        LOG.info("Stopped");
        stopFuture.complete();
    }

}
