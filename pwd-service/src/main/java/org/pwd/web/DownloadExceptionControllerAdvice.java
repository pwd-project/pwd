package org.pwd.web;

import org.pwd.web.download.CmsTemplateHashErrorException;
import org.pwd.web.download.CmsTemplateHashExpiredException;
import org.pwd.web.download.CmsTemplateNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DownloadExceptionControllerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(DownloadExceptionControllerAdvice.class);

    @ExceptionHandler(CmsTemplateHashExpiredException.class)
    public String handleCmsTemplateHashExpired(CmsTemplateHashExpiredException ex) {
        logger.error(ex.getMessage());
        return "error_expired";
    }

    @ExceptionHandler(CmsTemplateNotFoundException.class)
    public String handleCmsTemplateNotFound(CmsTemplateNotFoundException ex) {
        logger.error(ex.getMessage());
        return "error_404";
    }

    @ExceptionHandler(CmsTemplateHashErrorException.class)
    public String handleCmsTemplateHashError(CmsTemplateHashErrorException ex) {
        logger.error(ex.getMessage());
        return "error_404";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentError(IllegalArgumentException ex) {
        logger.error(ex.getMessage());
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleError(Exception ex) {
        logger.error(ex.getMessage());
        return "error";
    }
}
