package org.pwd.domain.audit;

import org.pwd.hibernate.PostgresJsonUserType;

/**
 * @author bartosz.walacik
 */
public class WebsiteAuditReportUserType extends PostgresJsonUserType<WebsiteAuditReport> {

    @Override
    public Class<WebsiteAuditReport> returnedClass() {
        return WebsiteAuditReport.class;
    }
}
