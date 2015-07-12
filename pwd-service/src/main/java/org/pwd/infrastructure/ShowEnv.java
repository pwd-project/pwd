package org.pwd.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @author bartosz.walacik
 */
@Service
public class ShowEnv {
    private static final Logger logger = LoggerFactory.getLogger(ShowEnv.class);

    private final Environment environment;

    @Autowired
    public ShowEnv(Environment environment) {
        this.environment = environment;


        logger.info("-- ShowEnv --");

        Arrays.asList(environment.getActiveProfiles()).forEach(name -> logger.info("active.profile:" + name));
        logger.info("database.url: "+environment.getProperty("spring.datasource.url"));

    }
}

