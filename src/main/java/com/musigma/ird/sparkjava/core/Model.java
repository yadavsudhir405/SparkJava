package com.musigma.ird.sparkjava.core;

import java.io.Serializable;

/**
 * @author sudhir
 *         Date:20/9/16
 *         Time:2:35 PM
 *         Project:SparkJava
 */
public class Model implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String email;
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
