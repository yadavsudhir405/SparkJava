package com.musigma.ird.test.socket;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author sudhir
 *         Date:20/9/16
 *         Time:12:15 AM
 *         Project:SparkJava
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Exceptintest {
    Class<? extends Exception> value();
}
