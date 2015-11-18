package org.pwd.web.download

import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.junit.Rule
import org.pwd.application.IntegrationTest
import org.pwd.domain.download.DownloadRequestRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.Rollback
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

import javax.transaction.Transactional

import static com.github.tomakehurst.wiremock.client.WireMock.*

class SendDownloadEmailTest extends IntegrationTest {
    @Rule
    WireMockRule wireMockRule = new WireMockRule(8089)

    @Autowired
    DownloadRequestRepository downloadRequestRepository;

    def setup() {
        downloadRequestRepository.deleteAll()
    }

    @Transactional
    @Rollback(false)
    def "should send email with template from 'Download' page and save it to database"() {
        given:
        def restTemplate = new RestTemplate();

        MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
        form.add('templateName', 'templateName');
        form.add('cms', 'WordPress');
        form.add('file', 'file');
        form.add('name', 'name');
        form.add('administrativeEmail', 'mail@localhost');

        wireMockRule.stubFor(post(urlPathEqualTo("/messages"))
                .willReturn(aResponse()
                .withStatus(200)))

        when:
        restTemplate.postForObject('http://localhost:8081/pobierz', form, String.class)

        then:
        downloadRequestRepository.flush()
        downloadRequestRepository.count() == 1
        with(downloadRequestRepository.findAll()[0]) {
            templateName == "templateName"
            cms == "WordPress"
            file == "file"
            name == "name"
            administrativeEmail == "mail@localhost"
        }
    }
}
