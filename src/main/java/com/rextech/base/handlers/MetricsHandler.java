package com.rextech.base.handlers;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;
import com.rextech.base.Env;
import com.rextech.base.utils.AdvancedConfigurableRtxHandler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

/**
 * handles requests to metrics of current app
 */
public class MetricsHandler extends AdvancedConfigurableRtxHandler {
    @Override
    protected void handleGet(RoutingContext rtx) {
        MetricRegistry registry = SharedMetricRegistries
                .getOrCreate(
                        Env.configSection("envConfig")
                            .getString("metricsRegName")
                );
        JsonObject rsp = new JsonObject();
        JsonObject counters = new JsonObject();
        registry.getCounters().forEach((k, v) -> counters.put(k, v.getCount()));
        JsonObject gauges = new JsonObject();
        registry.getGauges().forEach((k ,v) -> gauges.put(k, v.getValue()));
        JsonObject histograms = new JsonObject();
        registry.getHistograms().forEach((k, v) -> histograms.put(k, v.getCount()));
        JsonObject meters = new JsonObject();
        registry.getMeters().forEach((k, v) -> meters.put(k, v.getMeanRate()));
        JsonObject timers = new JsonObject();
        registry.getTimers().forEach((k, v) -> timers.put(k, v.getMeanRate()));
        rsp.put("counters", counters)
                .put("gauges", gauges)
                .put("histograms", histograms)
                .put("meters", meters)
                .put("timers", timers);
        rtx.response()
                .setStatusCode(200)
                .setChunked(true)
                .putHeader("Content-Type", "application/json")
                .write(rsp.encodePrettily())
                .end();
    }
}
