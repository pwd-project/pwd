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

    private final int httpStatusCode;
    private final List<MetricValue> metrics;

    /**
     * from pwd-analysis REST response
     */
    public WebsiteAuditReport(JsonObject analysisResponse) {
        checkArgument(analysisResponse != null);

        this.httpStatusCode = statusCodeFromJson(analysisResponse);
        this.metrics = metricsFromJson(analysisResponse);
    }

    public WebsiteAuditReport(int httpStatusCode, List<MetricValue> metrics) {
        checkArgument(metrics != null);

        this.httpStatusCode = httpStatusCode;
        this.metrics = new ArrayList<>(metrics);
    }

    public MetricValue getMetric(String metricName){
        return metrics
               .stream()
               .filter(it -> it.getMetricName().equals(metricName))
               .findFirst()
               .orElseThrow(() -> new RuntimeException("no such metric: " + metricName));
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public int score(){
        return (int) metricsAvg();
    }

    private double metricsAvg(){
        return metrics.stream().filter(it -> it.getValue().isPresent())
                .mapToInt(it -> it.getValue().get()).summaryStatistics().getAverage();
    }

    public List<MetricValue> getMetrics() {
        return Collections.unmodifiableList(metrics);
    }

    private int statusCodeFromJson(JsonObject analysisResponse) {
        return analysisResponse.getAsJsonObject("status").getAsJsonPrimitive("responseCode").getAsInt();
    }

    private List<MetricValue> metricsFromJson(JsonObject analysisResponse) {

        List<MetricValue> metrics = new ArrayList<>();
        JsonObject analysisElement = analysisResponse.getAsJsonObject("analysis");

        for (Map.Entry<String, JsonElement> metricEntry : analysisElement.entrySet()) {
            String metricName = metricEntry.getKey();
            JsonObject metricObject = metricEntry.getValue().getAsJsonObject();

            Optional<Integer> score;
            if (metricObject.has("score")){
                score = Optional.of(metricObject.get("score").getAsInt());
            }
            else{
                score = Optional.empty();
            }
            MetricValue metricValue = new MetricValue(metricName, score);

            metrics.add(metricValue);
        }

        return metrics;
    }
}
