package org.pwd.web;

import com.lyncode.jtwig.mvc.JtwigViewResolver;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author bartosz.walacik
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/error_404").setViewName("error_404");
    }

    @Bean
    public ViewResolver viewResolver() {
        JtwigViewResolver viewResolver = new JtwigViewResolver();
        viewResolver.setPrefix("classpath:templates/");
        viewResolver.setSuffix(".twig");
        viewResolver.setEncoding("UTF-8");
        return viewResolver;
    }

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer(){
        return container -> container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/error_404"));
    }
}
