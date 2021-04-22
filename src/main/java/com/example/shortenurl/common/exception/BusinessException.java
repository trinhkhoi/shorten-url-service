package com.example.shortenurl.common.exception;

import com.example.shortenurl.common.util.MessageUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.util.List;

public class BusinessException extends Exception {
    private static final Logger logger = LoggerFactory.getLogger(BusinessException.class);
    private static final long serialVersionUID = -8373980649169847082L;
    private HttpStatus status;
    private String message;
    private String messageCode;

    public BusinessException(HttpStatus status, String message) {
        super();
        this.status = status;
        this.message = message;
    }

    public BusinessException(String messageCode, List<String> params) {
        this.status = HttpStatus.BAD_REQUEST;
        setMessageCode(messageCode, params);
    }

    public BusinessException(HttpStatus status, String messageCode, String defaultMessage) {
        super();
        this.status = status;
        setMessageCode(messageCode, defaultMessage);
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageCode() {
        return messageCode;
    }

    private void setMessageCode(String messageCode, List<String> params) {
        this.messageCode = messageCode;
        try {
            this.message = !StringUtils.isBlank(messageCode) && params != null && params.size() > 0
                    ? MessageUtil.get(messageCode, params.toArray())
                    : MessageUtil.get(messageCode);
        } catch (Exception ex) {
            logger.error("messageCode was not found or invalid: " + messageCode);
            message = messageCode;
        }
    }

    public void setMessageCode(String messageCode, String defaultMessage) {
        this.messageCode = messageCode;
        try {
            message = MessageUtil.get(messageCode);
        } catch (Exception ex) {
            logger.error("messageCode was not found: " + messageCode);
            message = defaultMessage;
        }
    }

    @Override
    public String getMessage() {
        return message;
    }
}
