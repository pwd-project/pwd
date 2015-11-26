package org.pwd.web.contact

import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.junit.Rule
import org.pwd.application.IntegrationTest
import org.pwd.domain.contact.ContactRequestRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.Rollback
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

import javax.transaction.Transactional

import static com.github.tomakehurst.wiremock.client.WireMock.*

class SendContactEmailTest extends IntegrationTest {
    @Rule
    WireMockRule wireMockRule = new WireMockRule(8089)

    @Autowired
    ContactRequestRepository contactRequestRepository

    def setup() {
        contactRequestRepository.deleteAll()
    }

    def "should send email with details from 'Contact' page and save it to database"() {
        given:
        def restTemplate = new RestTemplate();

        MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
        form.add('name', 'name');
        form.add('administrativeEmail', 'mail@localhost');
        form.add('mobile', '0');
        form.add('site', 'site');
        form.add('message', 'message');

        wireMockRule.stubFor(post(urlPathEqualTo("/messages"))
                .willReturn(aResponse()
                .withStatus(200)))

        when:
        restTemplate.postForObject('http://localhost:8081/kontakt', form, String.class)

        then:
        contactRequestRepository.flush()
        contactRequestRepository.count() == 1
        with(contactRequestRepository.findAll()[0]) {
            name == "name"
            administrativeEmail == "mail@localhost"
            mobile == "0"
            site == "site"
            message == "message"
        }
    }
}
