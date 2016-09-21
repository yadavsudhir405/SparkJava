package com.musigma.ird.sparkjava.core;

import java.io.Serializable;

/**
 * @author sudhir
 *         Date:19/9/16
 *         Time:12:50 PM
 *         Project:SparkJava
 */
public final  class Field implements Serializable {
    private static final long serialVersionUID = 1L;
    private  Type type;
    private  String name;
    public Field(){

    }

    public Field(Type type, String name) {
        this.type = type;
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Field{" +
                "type=" + type +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Field field = (Field) o;

        if (type != field.type) return false;
        return name.equals(field.name);

    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
