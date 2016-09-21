package com.musigma.ird.test.socket;

import com.musigma.ird.sparkjava.core.Field;
import com.musigma.ird.sparkjava.core.SocketCommands;

import java.util.List;

/**
 * @author sudhir
 *         Date:20/9/16
 *         Time:5:23 PM
 *         Project:SparkJava
 */
public class SparkDTO {
    private SocketCommands socketCommand;
    private List<Field> fields;
    private  String query;
    private   String filepath;
    private  String tableName;

    public SocketCommands getSocketCommand() {
        return socketCommand;
    }

    public void setSocketCommand(SocketCommands socketCommand) {
        this.socketCommand = socketCommand;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
