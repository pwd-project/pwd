package org.pwd.application

import org.pwd.domain.audit.MetricValue
import org.pwd.domain.audit.WebsiteAudit
import org.pwd.domain.audit.WebsiteAuditReport
import org.pwd.domain.audit.WebsiteAuditRepository
import org.pwd.domain.websites.Website
import org.pwd.domain.websites.WebsiteRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.Rollback

import javax.transaction.Transactional
import java.time.LocalDateTime

/**
 * @author bartosz.walacik
 */
class SmokeTest extends IntegrationTest {

    @Autowired
    WebsiteRepository websiteRepository

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

}
