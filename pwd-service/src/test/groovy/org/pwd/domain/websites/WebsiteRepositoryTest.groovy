package org.pwd.domain.websites

import org.pwd.application.IntegrationTest
import org.pwd.domain.audit.AuditRepository
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

/**
 * @author bartosz.walacik
 */
class WebsiteRepositoryTest extends IntegrationTest {

    @Autowired
    WebsiteRepository websiteRepository

    def beforeSpec(){
        websiteRepository.deleteAll()
    }

    def "should find websites with full text search"(){
      given:
      websiteRepository.save(new Website(10, new URL("http://example.com/"), "Urząd Smoka Wawelskiego",
              new Address("Żyrardów", "Śląskie", "Powiat Duży")))

      websiteRepository.save(new Website(11, new URL("http://example.com/"), "Biuro Smoka Wawelskiego",
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