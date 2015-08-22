package org.pwd.domain.audit;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author bartosz.walacik
 */
public class MetricValue {
    private final String metricName;

    //empty Optional means that metric is not available.
    //e.g. "images" metric has no value when page contains no images
    private final Optional<Integer> value;

    public MetricValue(String metricName, Optional<Integer> value) {
        checkArgument(!metricName.isEmpty());
        checkArgument(value != null);
        checkArgument(!value.isPresent() || (value.get() >= 0 && value.get() <= 100));

        this.metricName = metricName;
        this.value = value;
    }

    public String getMetricName() {
        return metricName;
    }

    public Optional<Integer> getValue() {
        return value;
    }

    public String getValueAsString() {
        return value.map(it -> it.toString()).orElseGet(() -> "N/A");
    }
}
