package org.pwd.domain.websites

import org.pwd.application.IntegrationTest
import org.pwd.domain.audit.WebsiteAudit
import org.pwd.domain.audit.WebsiteAuditRepository
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author bartosz.walacik
 */
class WebsiteRepositoryTest extends IntegrationTest {

    @Autowired
    WebsiteRepository websiteRepository

    @Autowired
    WebsiteAuditRepository websiteAuditRepository

    def setup(){
        websiteAuditRepository.deleteAll()
        websiteRepository.deleteAll()
    }

    def "should find websites with full text search"(){
        given:
        websiteRepository.save(new Website(10, 111, new URL("http://example.com/"), "P",
                "Urząd Smoka Wawelskiego", "Smok", "kris@gnail.com",
                new Address("Żyrardów", "Śląskie", "Powiat Duży")))

        websiteRepository.save(new Website(11, 222, new URL("http://example.com/"), "W",
                "Biuro Smoka Wawelskiego", "Smok", "kris@gnail.com",
                new Address("Gniezno", "Mazowieckie", "Powiat Mały")))

        expect:
        //query by name
        websiteRepository.search("urzad").size() == 1
        websiteRepository.search("Urząd").size() == 1
        websiteRepository.search("Smoka").size() == 2

        //query by city
        websiteRepository.search("zyrardow").size() == 1
        //query by voivodeship
        websiteRepository.search("Mazowieckie").size() == 1

        //query by county
        websiteRepository.search("duzy").size() == 1
        websiteRepository.search("powiat").size() == 2

    }
}