package org.pwd.web.contact
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.junit.Rule
import spock.lang.Specification

import static com.github.tomakehurst.wiremock.client.WireMock.*
/**
 * @author S³awomir Mikulski
 */
class SendContactEmailTest extends Specification {
    @Rule
    WireMockRule wireMockRule = new WireMockRule(8089)

    def "should send email with details from 'Contact' page"(){
        given:
        def contactCtrl = new ContactController('dummy@mailbox.com','dummyApiKey','http://localhost:8089/email')

        wireMockRule.stubFor(post(urlPathEqualTo("/email"))
                .willReturn(aResponse()
                .withStatus(200)))

        when:
        def response = contactCtrl.sendEmail("dummyName","dummy@email.com","dummyNumber","http://dummmy.site.com","Test message")

        then:
        response == "email_sent"
        verify(postRequestedFor(urlMatching("/email"))
                .withRequestBody(matching(".*from.*"))
                .withRequestBody(matching(".*to.*"))
                .withRequestBody(matching(".*subject.*"))
                .withRequestBody(matching(".*text.*"))
        );
    }
}
