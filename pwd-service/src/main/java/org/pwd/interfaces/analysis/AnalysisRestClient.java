package org.pwd.interfaces.analysis;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.pwd.domain.audit.WebsiteAuditReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.Optional;


/**
 * @author bartosz.walacik
 */
@Service
public class AnalysisRestClient {
    private static final Logger logger = LoggerFactory.getLogger(AnalysisRestClient.class);

    private final RestTemplate restTemplate = new RestTemplate();
    private final RetryTemplate retryTemplate;
    private final String analysisEndpointUrl;
    private final Gson gson = new GsonBuilder().create();

    @Autowired
    public AnalysisRestClient(@Value("${analysis.endpoint}") String analysisEndpointUrl, RetryTemplate retryTemplate) {
        Preconditions.checkArgument(!analysisEndpointUrl.isEmpty());
        this.retryTemplate = retryTemplate;
        this.analysisEndpointUrl = analysisEndpointUrl;
    }

    public Optional<WebsiteAuditReport> getAnalysis(URL websiteUrl) {
        String response = retryTemplate.execute(request -> restTemplate.getForObject(analysisEndpointUrl + "?url={websiteUrl}", String.class, websiteUrl));
        logger.info("analysis: \n" + response);
        return Optional.ofNullable(new WebsiteAuditReport(gson.fromJson(response, JsonElement.class).getAsJsonObject()));
    }
}