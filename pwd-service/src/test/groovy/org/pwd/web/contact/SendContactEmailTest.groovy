package org.pwd.web.contact
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.junit.Rule
import org.pwd.domain.contact.ContactRequestRepository
import org.pwd.interfaces.mailgun.MailgunClient
import org.springframework.beans.factory.annotation.Autowired
import org.pwd.application.IntegrationTest
import org.springframework.test.annotation.Rollback

import javax.transaction.Transactional

import static com.github.tomakehurst.wiremock.client.WireMock.*
/**
 * @author Sławomir Mikulski
 */
class SendContactEmailTest extends IntegrationTest {
    @Rule
    WireMockRule wireMockRule = new WireMockRule(8089)

    @Autowired
    ContactRequestRepository contactRequestRepository

    def setup() {
        contactRequestRepository.deleteAll()
    }

    @Transactional
    @Rollback(false)
    def "should send email with details from 'Contact' page and save it to database"(){
        given:
        def mailgunClient = new MailgunClient('dummy@mailbox.com','dummyApiKey','http://localhost:8089/')
        def contactCtrl = new ContactController(mailgunClient,'dummy@mailbox.com',contactRequestRepository)

        wireMockRule.stubFor(post(urlPathEqualTo("/messages"))
                .willReturn(aResponse()
                .withStatus(200)))

        when:
        def response = contactCtrl.sendEmail("dummyName","dummy@email.com","dummyNumber","http://dummmy.site.com","Test message","")

        then:
        response == "email_sent"
        verify(postRequestedFor(urlMatching("/messages"))
                .withRequestBody(matching(".*from.*"))
                .withRequestBody(matching(".*to.*"))
                .withRequestBody(matching(".*subject.*"))
                .withRequestBody(matching(".*text.*"))
        );
        contactRequestRepository.flush()
        contactRequestRepository.count()==1
        with(contactRequestRepository.findAll()[0]){
            name == "dummyName"
            administrativeEmail == "dummy@email.com"
            mobile == "dummyNumber"
            site == "http://dummmy.site.com"
            message == "Test message"
        }
    }
}
