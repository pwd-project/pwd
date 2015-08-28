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
      websiteRepository.save(new Website(10, 111, new URL("http://example.com/"), "Urząd Smoka Wawelskiego",
              new Address("Żyrardówek", "Śląskie", "Powiatek przyduży")))

      websiteRepository.save(new Website(11, 222, new URL("http://example.com/"), "Biuro Smoka Wawelskiego",
              new Address("Gniezno", "Mazowieckiem", "Powiatek Mały")))

      expect:
      //query by name
      websiteRepository.search("Maly").size() == 1
      websiteRepository.search("smoka").size() == 2
      websiteRepository.search("Smoka").size() == 2

      //query by city
      websiteRepository.search("zyrardowek").size() == 1
      //query by voivodeship
      websiteRepository.search("Mazowieckiem").size() == 1

      //query by county
      websiteRepository.search("przyduzy").size() == 1
      websiteRepository.search("powiatek").size() == 2

    }
}
