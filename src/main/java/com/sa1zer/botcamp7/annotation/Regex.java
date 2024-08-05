package com.sa1zer.botcamp7.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE, ElementType.RECORD_COMPONENT, ElementType.PARAMETER})
public @interface Regex {

    String regex() default "";
}
