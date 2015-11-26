package org.pwd.web.download;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DownloadException extends RuntimeException {

    private static final Logger logger = LoggerFactory.getLogger(DownloadException.class);

    @ExceptionHandler(CmsTemplateHashExpiredException.class)
    public String handleCmsTemplateHashExpired(CmsTemplateHashExpiredException ex) {
        logger.error(ex.getMessage());
        return "error_404";
    }

    @ExceptionHandler(CmsTemplateNotFoundException.class)
    public String handleCmsTemplateNotFound(CmsTemplateNotFoundException ex) {
        logger.error(ex.getMessage());
        return "error_404";
    }
}
