package com.example.shortenurl.common.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Documented
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = HttpValidator.class)
public @interface HttpConstraint {
    String message() default "Access is denied";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
