package org.pwd.domain.audit;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author bartosz.walacik
 */
public class MetricValue implements Comparable<MetricValue> {
    private final Metric metric;

    //null -> empty optional
    private final Integer value;

    MetricValue(Metric metric, int value) {
        this(metric, Optional.of(value));
    }

    MetricValue(Metric metric, Optional<Integer> value) {
        checkArgument(metric != null);
        checkArgument(value != null);
        checkArgument(!value.isPresent() || (value.get() >= 0 && value.get() <= 100));

        this.metric = metric;
        this.value = value.orElseGet(() -> null);
    }

    public String getMetricName() {
        return metric.name();
    }

    public Metric getMetric() {
        return metric;
    }

    public int getWeight() {
        return metric.getWeight();
    }

    /**
     * Empty means that metric is not available.
     * e.g. "images" metric has no value when page contains no images
     */
    public Optional<Integer> getValue() {
        if (value == null) {
            return Optional.empty();
        }
        return Optional.of(value);
    }

    public String getValueAsString() {
        return value == null ? "N/A" : value + "";
    }

    @Override
    public int compareTo(MetricValue that) {
        return this.metric.compareTo(that.metric);
    }
}
