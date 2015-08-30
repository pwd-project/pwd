package org.pwd.domain.audit

import spock.lang.Specification

import static org.pwd.domain.audit.Metric.*

/**
 * @author bartosz.walacik
 */
class WebsiteAuditReportTest extends Specification {

    def "should calc score as weighted average"(){
      expect:
      new WebsiteAuditReport(200, [anyTitle.create(30), alt.create(0)]).score() == 15
      new WebsiteAuditReport(200, [anyTitle.create(20), alt.create(50)]).score() == 35
      new WebsiteAuditReport(200, [anyTitle.create(20), alt.create(40), htmlLang.create(20)]).score() == 26
      new WebsiteAuditReport(200, []).score() == 0
      new WebsiteAuditReport(200, [anyTitle.create(100), alt.create(Optional.empty())]).score() == 100
    }

    def "should sort metric by enum ordering"() {
        when:
        def report = new WebsiteAuditReport(200, [alt.create(1), anyTitle.create(1)])

        then:
        report.metrics[0].metric == alt
        report.metrics[1].metric == anyTitle
    }
}
