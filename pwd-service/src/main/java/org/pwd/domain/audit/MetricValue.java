package org.pwd.domain.audit;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author bartosz.walacik
 */
public class MetricValue {
    private final String metricName;

    //null -> empty optional
    private final Integer value;

    public MetricValue(String metricName, int value) {
        this(metricName, Optional.of(value));
    }

    public MetricValue(String metricName, Optional<Integer> value) {
        checkArgument(!metricName.isEmpty());
        checkArgument(value != null);
        checkArgument(!value.isPresent() || (value.get() >= 0 && value.get() <= 100));

        this.metricName = metricName;
        this.value = value.orElseGet(() -> null);
    }

    public String getMetricName() {
        return metricName;
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
}
