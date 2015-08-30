package org.pwd.domain.audit;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.pwd.hibernate.Document;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author bartosz.walacik
 */
public class WebsiteAuditReport extends Document{
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(WebsiteAuditReport.class);


    private final int httpStatusCode;
    private final int score;
    private final List<MetricValue> metrics;

    /**
     * from pwd-analysis REST response
     */
    public WebsiteAuditReport(JsonObject analysisResponse) {
        checkArgument(analysisResponse != null);

        this.httpStatusCode = statusCodeFromJson(analysisResponse);
        this.metrics = metricsFromJson(analysisResponse);
        this.score = metricsWeightedAvg();
    }

    public WebsiteAuditReport(int httpStatusCode, List<MetricValue> metrics) {
        checkArgument(metrics != null);

        this.httpStatusCode = httpStatusCode;
        this.metrics = new ArrayList<>(metrics);
        this.score = metricsWeightedAvg();
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
        return score;
    }

    private int metricsWeightedAvg() {

        List<MetricValue> nonEmptyMetrics =
                metrics.stream().filter(it -> it.getValue().isPresent()).collect(Collectors.toList());

        if (nonEmptyMetrics.size() == 0){
            return 0;
        }

        int valueSum = 0;
        int weightSum = 0;
        for (MetricValue it : nonEmptyMetrics){
            valueSum += it.getValue().get() * it.getWeight();
            weightSum += it.getWeight();
        }

        return valueSum / weightSum;
    }

    public List<MetricValue> getMetrics() {
        List<MetricValue> sortedCopy = new ArrayList<>(metrics);
        Collections.sort(sortedCopy);
        return sortedCopy;
    }

    private int statusCodeFromJson(JsonObject analysisResponse) {
        return analysisResponse.getAsJsonObject("status").getAsJsonPrimitive("responseCode").getAsInt();
    }

    private List<MetricValue> metricsFromJson(JsonObject analysisResponse) {

        List<MetricValue> metrics = new ArrayList<>();
        JsonObject analysisElement = analysisResponse.getAsJsonObject("analysis");

        for (Map.Entry<String, JsonElement> metricEntry : analysisElement.entrySet()) {
            String metricName = metricEntry.getKey();

            if (!Metric.exists(metricName)){
                logger.warn("ignoring unknown metric {}", metricName);
                continue;
            }
            Metric metric = Metric.valueOf(metricName);

            JsonObject metricElement = metricEntry.getValue().getAsJsonObject();
            JsonElement scoreElement = metricElement.get("score");

            Optional<Integer> score;
            if (!scoreElement.isJsonNull()){
                score = Optional.of(scoreElement.getAsInt());
            }
            else{
                score = Optional.empty();
            }
            MetricValue metricValue = metric.create(score);

            metrics.add(metricValue);
        }

        return metrics;
    }
}
