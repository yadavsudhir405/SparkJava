package com.musigma.ird.sparkjava.core;

/**
 * @author sudhir
 *         Date:19/9/16
 *         Time:12:44 PM
 *         Project:SparkJava
 */
public enum Type {
    STRING("String"),INTEGR("int"),LONG("long"),DOBLE("double"),BOOLEAN("boolean");

    private String javaTypeStringRepresentation;

    private Type(final String javaTypeStringRepresentation){
        this.javaTypeStringRepresentation=javaTypeStringRepresentation;
    }

    @Override
    public String toString() {
        return javaTypeStringRepresentation;
    }

}
