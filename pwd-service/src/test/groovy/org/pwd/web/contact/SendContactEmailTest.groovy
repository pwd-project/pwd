package org.pwd.web.contact
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.junit.Rule
import org.pwd.interfaces.mailgun.MailgunClient
import spock.lang.Specification

import static com.github.tomakehurst.wiremock.client.WireMock.*
/**
 * @author Sławomir Mikulski
 */
class SendContactEmailTest extends Specification {
    @Rule
    WireMockRule wireMockRule = new WireMockRule(8089)

    def "should send email with details from 'Contact' page"(){
        given:
        def mailgunClient = new MailgunClient('dummy@mailbox.com','dummyApiKey','http://localhost:8089/')
        def contactCtrl = new ContactController(mailgunClient,'dummy@mailbox.com')

        wireMockRule.stubFor(post(urlPathEqualTo("/messages"))
                .willReturn(aResponse()
                .withStatus(200)))

        when:
        def response = contactCtrl.sendEmail("dummyName","dummy@email.com","dummyNumber","http://dummmy.site.com","Test message")

        then:
        response == "email_sent"
        verify(postRequestedFor(urlMatching("/messages"))
                .withRequestBody(matching(".*from.*"))
                .withRequestBody(matching(".*to.*"))
                .withRequestBody(matching(".*subject.*"))
                .withRequestBody(matching(".*text.*"))
        );
    }
}
