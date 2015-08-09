package org.pwd.application

import org.pwd.domain.audit.MetricValue
import org.pwd.domain.audit.WebsiteAudit
import org.pwd.domain.audit.WebsiteAuditReport
import org.pwd.domain.audit.WebsiteAuditRepository
import org.pwd.domain.websites.Website
import org.pwd.domain.websites.WebsiteRepository
import org.springframework.beans.factory.annotation.Autowired

import javax.transaction.Transactional
import java.time.LocalDateTime

/**
 * @author bartosz.walacik
 */
class SmokeTest extends IntegrationTest {

    @Autowired
    WebsiteRepository websiteRepository

    @Autowired
    WebsiteAuditRepository websiteAuditRepository

    @Transactional
    def "should run the App and connect to database"() {
        given:
        def website = new Website(1, new URL("http://example.com/"))

        when:
        websiteRepository.save(website)
        def persistedWebsite = websiteRepository.getOne(1)

        then:
        persistedWebsite.url ==  new URL("http://example.com/")
    }

    @Transactional
    def "should persist Documents as Postgres JSON types"(){
      given:
      def report = new WebsiteAuditReport([new MetricValue("metricA", 0), new MetricValue("metricB", 100)],
                                          LocalDateTime.now())
      def websiteAudit = new WebsiteAudit(report)

      when:
      websiteAuditRepository.save(websiteAudit)

      then:
      true
    }
}
