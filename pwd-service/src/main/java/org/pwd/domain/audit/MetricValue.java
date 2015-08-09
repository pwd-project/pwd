package org.pwd.domain.audit;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author bartosz.walacik
 */
public class MetricValue {
    private final String metricName;
    private final int value;

    public MetricValue(String metricName, int value) {
        checkArgument(!metricName.isEmpty());
        checkArgument(value >= 0);
        checkArgument(value <= 100);

        this.metricName = metricName;
        this.value = value;
    }

    public String getMetricName() {
        return metricName;
    }

    public int getValue() {
        return value;
    }
}
