package org.pwd.domain.audit;

import org.pwd.hibernate.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author bartosz.walacik
 */
public class WebsiteAuditReport extends Document{

    private final List<MetricValue> metrics;

    private final LocalDateTime created;

    public WebsiteAuditReport(List<MetricValue> metrics, LocalDateTime created) {
        checkArgument(metrics != null);
        checkArgument(created != null);

        this.metrics = new ArrayList<>(metrics);
        this.created = created;
    }

    public List<MetricValue> getMetrics() {
        return Collections.unmodifiableList(metrics);
    }

    public LocalDateTime getCreated() {
        return created;
    }
}
