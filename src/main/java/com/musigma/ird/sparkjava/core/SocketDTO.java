package com.musigma.ird.sparkjava.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sudhir
 *         Date:19/9/16
 *         Time:1:00 PM
 *         Project:SparkJava
 */
public class SocketDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private  SocketCommands socketCommand;
    private  List<Field> fields;
    private  String query;
    private   String filepath;
    private  String tableName;
    public SocketDTO(){

    }
    SocketDTO(SocketDTOBuilder builder){
        this.socketCommand=builder.socketCommand;
        this.fields=builder.fields;
        this.filepath=builder.filepath;
        this.query=builder.query;
        this.tableName=builder.tableName;
    }

    /**
     * The {@code SocketDTOBuilder} is the socketDto builder class ,builds socketDTOObject.
     */
    public static class SocketDTOBuilder{
        private  final SocketCommands socketCommand;
        private  final List<Field> fields;
        private  String query;
        private   String filepath;
        private String tableName;

        public SocketDTOBuilder(SocketCommands socketCommand, List<Field> fields) {
            this.socketCommand = socketCommand;
            this.fields = fields;
        }
        public SocketDTOBuilder addHdfcFilePath(String filepath){
            this.filepath=filepath;
            return this;
        }
        public SocketDTOBuilder addSqlQery(String sqlQuery){
            this.query=sqlQuery;
            return this;
        }
        public SocketDTOBuilder addTableName(String tableName){
            this.tableName=tableName;
            return this;
        }
        public SocketDTO build(){
            return new SocketDTO(this);
        }
    }

    public SocketCommands getSocketCommand() {
        return socketCommand;
    }

    public List<Field> getFields() {
        return new ArrayList<>(fields);
    }

    public String getFilepath() {
        return filepath;
    }

    public String getQuery() {
        return query;
    }

    public String getTableName() {
        return tableName;
    }

    public void setSocketCommand(SocketCommands socketCommand) {
        this.socketCommand = socketCommand;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
