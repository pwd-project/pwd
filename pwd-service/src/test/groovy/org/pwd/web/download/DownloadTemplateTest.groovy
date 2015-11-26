package org.pwd.web.download

import org.pwd.application.IntegrationTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.TestRestTemplate
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import spock.lang.Shared

import java.time.LocalDateTime
import java.time.ZoneOffset

/**
 * @author smikulsk
 */
class DownloadTemplateTest  extends IntegrationTest {

    @Autowired
    DownloadRequestParamsHasher downloadRequestParamsHasher;

    @Shared
    TestRestTemplate testRestTemplate = new TestRestTemplate();

    def "should download template for proper hashcode"() {
        given:
        def timestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        def hashSend = downloadRequestParamsHasher.getHash('T11','WORDPRESS',timestamp);

        when:
        def response = testRestTemplate.getForEntity('http://localhost:8081/pobierz/{template}/{cms}/{hash}/{timestamp}', byte[], 'T11', 'WORDPRESS', hashSend, timestamp)

        then:
        response.statusCode == HttpStatus.OK;
        response.headers.get(HttpHeaders.CONTENT_TYPE).contains(MediaType.APPLICATION_OCTET_STREAM_VALUE+";charset=UTF-8");
    }

    def "should not download template for expired hashcode"() {
        given:
        def timestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)-4000;
        def hashSend = downloadRequestParamsHasher.getHash('T11','WORDPRESS',timestamp);

        when:
        def response = testRestTemplate.getForEntity('http://localhost:8081/pobierz/{template}/{cms}/{hash}/{timestamp}', String.class, 'T11', 'WORDPRESS', hashSend, timestamp);

        then:
        response.statusCode == HttpStatus.BAD_REQUEST;
    }

    def "should not download template for bad template"() {
        given:
        def timestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        def hashSend = downloadRequestParamsHasher.getHash('bad_template','WORDPRESS',timestamp);

        when:
        def response = testRestTemplate.getForEntity('http://localhost:8081/pobierz/{template}/{cms}/{hash}/{timestamp}', String.class, 'bad_template', 'WORDPRESS', hashSend, timestamp);

        then:
        response.statusCode == HttpStatus.NOT_FOUND;
    }

    def "should not download template for bad cms"() {
        given:
        def timestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        def hashSend = downloadRequestParamsHasher.getHash('T11','bad_cms',timestamp);

        when:
        def response = testRestTemplate.getForEntity('http://localhost:8081/pobierz/{template}/{cms}/{hash}/{timestamp}', String.class, 'T11', 'bad_cms', hashSend, timestamp);

        then:
        response.statusCode == HttpStatus.NOT_FOUND;
    }
}
