package org.pwd.domain.audit

import spock.lang.Specification

/**
 * @author bartosz.walacik
 */
class WebsiteAuditReportTest extends Specification {

    def "should calc score as unweighted average"(){
      expect:
      new WebsiteAuditReport(200, [new MetricValue("a", 100), new MetricValue("b", 0)]).score() == 50
      new WebsiteAuditReport(200, []).score() == 0
      new WebsiteAuditReport(200, [new MetricValue("a", 100), new MetricValue("b", Optional.empty())]).score() == 100
    }
}
