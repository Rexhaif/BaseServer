package com.rextech.base.utils;

import com.rextech.base.Env;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Verticle;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Internal utilities
 */
public class Utils {

    private static Logger LOG = LoggerFactory.getLogger("Utils Object");

    public static <T> T getInstance(String className, T def) {
        try {
            return (T) Class.forName(className).newInstance();
        } catch (InstantiationException |
                ClassNotFoundException |
                IllegalAccessException e) {
            LOG.error("Cannot instantiate object for class: " + className + ", retrieving default", e);
            return def;
        }
    }

    public static JsonObject getFileAsJsonObject(String fileName) {
        try {
            return new JsonObject(
                    new String(
                            Files.readAllBytes(Paths.get(ClassLoader.getSystemResource(fileName).toURI())),
                            StandardCharsets.UTF_8
                    )
            );
        } catch (IOException | URISyntaxException e) {
            LOG.error("Cannot read file, retrieving empty jsonObject", e);
            return new JsonObject();
        }
    }

    public static void deployVertices(JsonArray descriptor) {
        descriptor.forEach(obj -> {
            JsonObject vrtConf = (JsonObject) obj;
            Env.vertx().deployVerticle(
                    Utils.<Verticle> getInstance(vrtConf.getString("className"), null),
                    new DeploymentOptions().setConfig(vrtConf.getJsonObject("config"))
            );
        });
    }

}
