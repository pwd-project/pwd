package org.pwd.web;

import org.pwd.web.download.CmsTemplateHashErrorException;
import org.pwd.web.download.CmsTemplateHashExpiredException;
import org.pwd.web.download.CmsTemplateNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class DownloadExceptionControllerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(DownloadExceptionControllerAdvice.class);

    @ExceptionHandler(CmsTemplateHashExpiredException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleCmsTemplateHashExpired(CmsTemplateHashExpiredException ex) {
        logger.error(ex.getMessage());
        return "error_expired";
    }

    @ExceptionHandler(CmsTemplateNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleCmsTemplateNotFound(CmsTemplateNotFoundException ex) {
        logger.error(ex.getMessage());
        return "error_404";
    }

    @ExceptionHandler(CmsTemplateHashErrorException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleCmsTemplateHashError(CmsTemplateHashErrorException ex) {
        logger.error(ex.getMessage());
        return "error_404";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentError(IllegalArgumentException ex) {
        logger.error(ex.getMessage());
        return "error";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleError(Exception ex) {
        logger.error(ex.getMessage());
        return "error";
    }
}
