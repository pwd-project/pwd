package org.pwd.domain.audit;

import org.pwd.hibernate.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author bartosz.walacik
 */
public class WebsiteAuditReport extends Document{

    private final List<MetricValue> metrics;

    public WebsiteAuditReport(List<MetricValue> metrics) {
        checkArgument(metrics != null);

        this.metrics = new ArrayList<>(metrics);
    }

    public List<MetricValue> getMetrics() {
        return Collections.unmodifiableList(metrics);
    }

}
