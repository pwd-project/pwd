package org.pwd.interfaces.analysis;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.pwd.domain.audit.WebsiteAuditReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URL;


/**
 * @author bartosz.walacik
 */
@Service
public class AnalysisRestClient {
    private static final Logger logger = LoggerFactory.getLogger(AnalysisRestClient.class);

    private final RestTemplate restTemplate = new RestTemplate();
    private final HttpHeaders headers;
    private final String analysisEndpointUrl;
    private final Gson gson = new GsonBuilder().create();

    @Autowired
    public AnalysisRestClient(@Value("${analysis.endpoint}") String analysisEndpointUrl) {
        Preconditions.checkArgument(!analysisEndpointUrl.isEmpty());

        this.analysisEndpointUrl = analysisEndpointUrl;
        this.headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "*/*");
    }

    public WebsiteAuditReport getAnalysis(URL websiteUrl) {

        String response = restTemplate.getForObject(analysisEndpointUrl + "?url={websiteUrl}", String.class, websiteUrl);

        logger.info("analysis: \n" + response);

        JsonObject json = gson.fromJson(response, JsonElement.class).getAsJsonObject();

        return new WebsiteAuditReport(json);
    }

}