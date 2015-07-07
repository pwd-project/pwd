package org.pwd.application

import org.pwd.PwdService
import org.pwd.websites.Website
import org.pwd.websites.WebsiteRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

import javax.transaction.Transactional

/**
 * @author bartosz.walacik
 */
@ActiveProfiles("integration")
@WebIntegrationTest
@SpringApplicationConfiguration(classes = PwdService)
class SmokeTest extends Specification {

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
