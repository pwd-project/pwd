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
                new Person("Krzysiek", "Adamowicz", "Burmistrz"), 0, 0)
        def website2 = new Website(11, 222, new URL("http://example.com/"),
                "W", "Biuro Smoka Wawelskiego", "kris@gnail.com",
                new Address("Gniezno", "Mazowieckie", "Powiat Mały"),
                new Person("Krzysiek", "Adamowicz", "Burmistrz"), 0, 0)
        def website3 = new Website(13, 333, new URL("http://example.com/"),
                "W", "Biuro Świnki Peppy", "kris@gnail.com",
                new Address("Łódź", "łódzkie", "Gmina"),
                new Person("Krzysiek", "Adamowicz", "Burmistrz"), 0, 0)

        websiteRepository.save([website1,website2,website3])

        websiteAuditRepository.save(new WebsiteAudit(website1, audit, new WebsiteAuditReport(200, []),20))
        websiteAuditRepository.save(new WebsiteAudit(website2, audit, new WebsiteAuditReport(200, []),20))
        websiteAuditRepository.save(new WebsiteAudit(website3, audit, new WebsiteAuditReport(200, []),20))

        audit.done()
        auditRepository.save(audit)

        expect:
        //query by name
        websiteAuditRepository.search("urzad").size() == 1
        websiteAuditRepository.search("Urząd").size() == 1
        websiteAuditRepository.search("Smoka").size() == 2
        websiteAuditRepository.search("powiat&maly").size() == 1

        //query by city
        websiteAuditRepository.search("zyrardow").size() == 1
        websiteAuditRepository.search("łódz").size() == 1
        //query by voivodeship
        websiteAuditRepository.search("Mazowieckie").size() == 1
        //query by county
        websiteAuditRepository.search("duzy").size() == 1
        websiteAuditRepository.search("powiat").size() == 2
        //query by http
        websiteAuditRepository.search("http:").size() == 0
    }
}