package com.example.shortenurl.common.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class HttpValidator implements ConstraintValidator<HttpConstraint, String> {
    private Logger logger = LoggerFactory.getLogger(HttpValidator.class);
    private static final String URL_REGEX = "^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$";
    private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);
    @Override
    public void initialize(HttpConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Matcher m = URL_PATTERN.matcher(value);
        return m.matches();
    }
}
