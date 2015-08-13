package org.pwd.domain.audit;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.pwd.hibernate.Document;

import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author bartosz.walacik
 */
public class WebsiteAuditReport extends Document{

    private final List<MetricValue> metrics;

    /**
     * from pwd-analysis REST response
     */
    public WebsiteAuditReport(JsonObject analysisResponse) {
        checkArgument(analysisResponse != null);

        this.metrics = metricsFromJson(analysisResponse);
    }

    public WebsiteAuditReport(List<MetricValue> metrics) {
        checkArgument(metrics != null);

        this.metrics = new ArrayList<>(metrics);
    }

    public MetricValue getMetric(String metricName){
        return metrics
               .stream()
               .filter(it -> it.getMetricName().equals(metricName))
               .findFirst()
               .orElseThrow(() -> new RuntimeException("no such metric: " + metricName));
    }

    public int score(){
        return (int) metricsAvg();
    }

    private double metricsAvg(){
        return metrics.stream().mapToInt(it -> it.getValue()).summaryStatistics().getAverage();
    }

    public List<MetricValue> getMetrics() {
        return Collections.unmodifiableList(metrics);
    }

    private List<MetricValue> metricsFromJson(JsonObject analysisResponse) {
        List<MetricValue> metrics = new ArrayList<>();

        for (Map.Entry<String, JsonElement> metricEntry : analysisResponse.entrySet()) {
            String metricName = metricEntry.getKey();
            JsonObject metricObject = metricEntry.getValue().getAsJsonObject();

            MetricValue metricValue = new MetricValue(metricName, metricObject.get("score").getAsInt());

            metrics.add(metricValue);
        }

        return metrics;
    }
}