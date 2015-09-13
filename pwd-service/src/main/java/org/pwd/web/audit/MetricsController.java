package org.pwd.web.audit;

import org.pwd.domain.audit.Metric;
import org.pwd.web.ResourceNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Arrays;

/**
 * @author bartosz.walacik
 */
@Controller
@RequestMapping("/metryki")
public class MetricsController {

    @RequestMapping(method = RequestMethod.GET)
    public String showMetrics(Model model) {
        model.addAttribute("metrics", Arrays.asList(Metric.values()));
        return "metrics/metrics";
    }

    @RequestMapping(value = "/{metricName}", method = RequestMethod.GET)
    public String showMetric(@PathVariable String metricName, Model model) {

        if (!Metric.exists(metricName)) {
            throw new ResourceNotFoundException();
        }

        model.addAttribute("metric", Metric.valueOf(metricName));

        return "metrics/" + metricName;
    }
}
