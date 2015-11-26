package org.pwd.web.download

import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.junit.Rule
import org.pwd.application.IntegrationTest
import org.pwd.domain.download.DownloadRequestRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.Rollback
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import spock.lang.Shared

import javax.transaction.Transactional

import static com.github.tomakehurst.wiremock.client.WireMock.*

class SendDownloadEmailTest extends IntegrationTest {
    @Rule
    WireMockRule wireMockRule = new WireMockRule(8089)

    @Autowired
    DownloadRequestRepository downloadRequestRepository;

    @Shared
    TestRestTemplate testRestTemplate = new TestRestTemplate();

    def setup() {
        downloadRequestRepository.deleteAll()
    }

    def "should send email with template from 'Download' page and save it to database"() {
        given:
        testRestTemplate.getForObject('http://localhost:8081/pobierz/{template}/{cms}', String.class, "T11", "WORDPRESS")

        MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();

        form.add('name', 'name');
        form.add('administrativeEmail', 'mail@localhost');

        wireMockRule.stubFor(post(urlPathEqualTo("/messages"))
                .willReturn(aResponse()
                .withStatus(200)))

        when:
        testRestTemplate.postForObject('http://localhost:8081/pobierz/{template}/{cms}', form, String.class, "T11", "WORDPRESS")

        then:
        downloadRequestRepository.flush()
        downloadRequestRepository.count() == 1
        with(downloadRequestRepository.findAll()[0]) {
            name == "name"
            administrativeEmail == "mail@localhost"
        }
    }

    def "do not send email with template from 'Download' page for bad template "() {
        when:
        def response = testRestTemplate.getForEntity('http://localhost:8081/pobierz/{template}/{cms}', String.class, "bad_temp", "WORDPRESS")

        then:
        response.statusCode == HttpStatus.BAD_REQUEST;
        downloadRequestRepository.flush()
        downloadRequestRepository.count() == 0
    }

    def "do not send email with template from 'Download' page for bad cms "() {
        when:
        def response = testRestTemplate.getForEntity('http://localhost:8081/pobierz/{template}/{cms}', String.class, "T11", "bad_cms")

        then:
        response.statusCode == HttpStatus.BAD_REQUEST;
        downloadRequestRepository.flush()
        downloadRequestRepository.count() == 0
    }
}
