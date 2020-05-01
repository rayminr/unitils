/*
 * Copyright (c) Smals
 */
package org.unitils.spring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.unitils.spring.enums.LoadTime;

/**
 * @author wiw
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface LoadOn {
    LoadTime load() default LoadTime.METHOD;

}
