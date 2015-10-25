package org.pwd.domain.audit;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.pwd.hibernate.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author bartosz.walacik
 */
public class WebsiteAuditReport extends Document {
    private static final Logger logger = LoggerFactory.getLogger(WebsiteAuditReport.class);

    private final int httpStatusCode;
    private final double score;
    private final String cms;
    private final List<MetricValue> metrics;

    public WebsiteAuditReport(JsonObject analysisResponse) {
        checkArgument(analysisResponse != null);
        this.httpStatusCode = statusCodeFromJson(analysisResponse);
        this.metrics = metricsFromJson(analysisResponse);
        this.score = metricsWeightedAvg();
        this.cms = cmsFromJson(analysisResponse);
    }

    public WebsiteAuditReport(int httpStatusCode, List<MetricValue> metrics) {
        checkArgument(metrics != null);

        this.httpStatusCode = httpStatusCode;
        this.metrics = new ArrayList<>(metrics);
        this.score = metricsWeightedAvg();
        this.cms = "X";
    }

    public MetricValue getMetric(String metricName) {
        if (!hasMetric(metricName)) {
            return new MetricValue(Metric.alt,0);
        }
        return metrics
                .stream()
                .filter(it -> it.getMetricName().equals(metricName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("no such metric: " + metricName));
    }

    public boolean hasMetric(String metricName) {
        return metrics
                .stream()
                .filter(it -> it.getMetricName().equals(metricName)).count() > 0;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public double score() {
        return score;
    }

    public String cms() {
        return cms;
    }

    private double metricsWeightedAvg() {

        List<MetricValue> nonEmptyMetrics = metrics.stream().filter(it -> it.getValue().isPresent()).collect(Collectors.toList());

        if (nonEmptyMetrics.size() == 0) {
            return 0;
        }

        int valueSum = 0;
        int weightSum = 0;
        for (MetricValue it : nonEmptyMetrics) {
            valueSum += it.getValue().get() * it.getWeight();
            weightSum += it.getWeight();
        }

        return 1.0 * valueSum / weightSum;
    }

    public List<MetricValue> getMetrics() {
        List<MetricValue> sortedCopy = new ArrayList<>(metrics);
        Collections.sort(sortedCopy);
        return sortedCopy;
    }

    private int statusCodeFromJson(JsonObject analysisResponse) {
        return analysisResponse.getAsJsonObject("status").getAsJsonPrimitive("responseCode").getAsInt();
    }

    private String cmsFromJson(JsonObject analysisResponse) {
        JsonObject obj = analysisResponse.getAsJsonObject("analysis");
        if (obj != null) {
            obj = obj.getAsJsonObject("cms");
            if (obj != null) {
                JsonPrimitive objp = obj.getAsJsonPrimitive("cms");
                if (objp != null) {
                    return objp.getAsString();
                }
            }
        }
        return "";
    }

    private List<MetricValue> metricsFromJson(JsonObject analysisResponse) {

        List<MetricValue> metrics = new ArrayList<>();
        JsonObject analysisElement = analysisResponse.getAsJsonObject("analysis");

        for (Map.Entry<String, JsonElement> metricEntry : analysisElement.entrySet()) {
            String metricName = metricEntry.getKey();

            if ((!Metric.exists(metricName)) && (metricName != "cms")) {
                logger.warn("ignoring unknown metric {}", metricName);
                continue;
            }
            Metric metric = Metric.valueOf(metricName);

            JsonObject metricElement = metricEntry.getValue().getAsJsonObject();
            JsonElement scoreElement = metricElement.get("score");

            Optional<Integer> score;
            if (!scoreElement.isJsonNull()) {
                score = Optional.of(scoreElement.getAsInt());
            } else {
                score = Optional.empty();
            }
            MetricValue metricValue = metric.create(score);

            metrics.add(metricValue);
        }

        return metrics;
    }
}
