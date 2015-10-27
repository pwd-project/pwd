package org.pwd.domain.websites

import org.pwd.application.IntegrationTest
import org.pwd.domain.audit.*
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author bartosz.walacik
 */
class WebsiteAuditRepositoryTest extends IntegrationTest {

    @Autowired
    WebsiteRepository websiteRepository

    @Autowired
    WebsiteAuditRepository websiteAuditRepository

    @Autowired
    AuditRepository auditRepository

    def setup() {
        websiteAuditRepository.deleteAll()
        websiteRepository.deleteAll()
        auditRepository.deleteAll()
    }

    def "should find websites with full text search"() {
        given:
        def audit = new Audit().start();
        auditRepository.save(audit);

        def website1 = new Website(10, 111, new URL("http://example.com/"),
                "P", "Urząd Smoka Wawelskiego", "kris@gnail.com",
                new Address("Żyrardów", "Śląskie", "Powiat Duży"),
                new Person("Krzysiek", "Adamowicz", "Burmistrz"))
        def website2 = new Website(11, 222, new URL("http://example.com/"),
                "W", "Biuro Smoka Wawelskiego", "kris@gnail.com",
                new Address("Gniezno", "Mazowieckie", "Powiat Mały"),
                new Person("Krzysiek", "Adamowicz", "Burmistrz"))

        websiteRepository.save([website1,website2])

        websiteAuditRepository.save(new WebsiteAudit(website1, audit, new WebsiteAuditReport(200, [])))
        websiteAuditRepository.save(new WebsiteAudit(website2, audit, new WebsiteAuditReport(200, [])))

        audit.done()
        auditRepository.save(audit)

        expect:
        //query by name
        websiteAuditRepository.search("urzad").size() == 1
        websiteAuditRepository.search("Urząd").size() == 1
        websiteAuditRepository.search("Smoka").size() == 2
        websiteAuditRepository.search("powiat maly").size() == 1

        //query by city
        websiteAuditRepository.search("zyrardow").size() == 1
        //query by voivodeship
        websiteAuditRepository.search("Mazowieckie").size() == 1

        //query by county
        websiteAuditRepository.search("duzy").size() == 1
        websiteAuditRepository.search("powiat").size() == 2

    }
}