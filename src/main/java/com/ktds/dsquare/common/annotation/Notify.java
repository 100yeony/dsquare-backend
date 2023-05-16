package com.ktds.dsquare.common.annotation;

import com.ktds.dsquare.common.enums.NotifType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Notify {

    /**
     * Specify notification Type
     * @return NotifType[] (List of notification types)
     */
    NotifType[] value();

    /**
     * Specify the type of return value of annotated method
     * @return Class<?> of return value
     */
    Class<?> type() default Object.class;

}
