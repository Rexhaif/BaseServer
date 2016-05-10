package com.rextech.base;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rextech.base.utils.Utils;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main Environment
 */
public class Env {

    public static final String CONF_FILENAME = "config.json";

    private static Logger LOG = LoggerFactory.getLogger("ENV");

    private static ObjectMapper objectMapper = new ObjectMapper(new JsonFactory());

    private static Vertx vertx;

    private static JsonObject mainConfig;

    public static void init() {

        LOG.info("Initializing");

        LOG.info("Config - loading...");
        mainConfig = Utils.getFileAsJsonObject(CONF_FILENAME);
        LOG.info("Config - loaded");

        LOG.info("Vertx - loading...");
        vertx = Vertx.vertx(
                new VertxOptions()
                        .setMetricsOptions(
                                new DropwizardMetricsOptions()
                                        .setEnabled(true)
                                        .setRegistryName(
                                                configSection("envConfig")
                                                        .getString("metricsRegName")
                                        )
                                        .setJmxEnabled(true)
                        )
        );
        Runtime.getRuntime().addShutdownHook(new Thread(vertx::close));
        LOG.info("Vertx - loaded");

        LOG.info("Vertices - loading...");
        Utils.deployVertices(configSection("verticesConfig").getJsonArray("vertices"));
        LOG.info("Vertices - loaded");

        LOG.info("Initialized");
    }

    public static Vertx vertx() {
        return vertx;
    }

    public static ObjectMapper objectMapper() {
        return objectMapper;
    }

    public static JsonObject configSection(String name) {
        return mainConfig.getJsonObject(name);
    }


}
