package org.pwd.interfaces.analysis;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import groovy.json.JsonSlurper;
import java.util.Map;


/**
 * @author bartosz.walacik
 */
@Service
public class AnalysisRestClient {
    private static final Logger logger = LoggerFactory.getLogger(AnalysisRestClient.class);

    private final RestTemplate restTemplate = new RestTemplate();
    private final HttpHeaders headers;
    private final String analysisEndpointUrl;
    private final JsonSlurper jsonSlurper = new JsonSlurper();

    @Autowired
    public AnalysisRestClient(@Value("${analysis.endpoint}") String analysisEndpointUrl) {
        Preconditions.checkArgument(!analysisEndpointUrl.isEmpty());

        this.analysisEndpointUrl = analysisEndpointUrl;
        this.headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "*/*");
    }

    public Map getAnalysis(String websiteUrl){

        String result = restTemplate.getForObject(analysisEndpointUrl + "?url={websiteUrl}", String.class, websiteUrl);

        logger.info("analysis response: \n" + result);

        Map parsed = (Map)jsonSlurper.parseText(result);

        return parsed;
    }
}
