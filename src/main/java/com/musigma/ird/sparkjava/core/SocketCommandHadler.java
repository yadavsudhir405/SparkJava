package com.musigma.ird.sparkjava.core;

import com.musigma.ird.exception.JsonToObjectConvertionException;
import com.musigma.ird.utils.JsonObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author sudhir
 *         Date:19/9/16
 *         Time:11:24 AM
 *         Project:SparkJava
 */
public class SocketCommandHadler implements CommandHandler {
    private static final Logger LOGGER= LoggerFactory.getLogger(CommandHandler.class);
    private String socketInputString;
    private SocketDTO socketDTO;

    public SocketCommandHadler(String socketInputString) {
        this.socketInputString = socketInputString;
    }

    @Override
    public String handleCommand() {
        SocketCommands receivedCommand=extractCommandFromJson();
        String responseTobeSent= handleThisCommad(receivedCommand);
        return responseTobeSent;
    }
    private SocketCommands extractCommandFromJson(){
        try{
            SocketDTO socketDTO1=JsonObjectMapper.getInstanceFromJson(socketInputString,SocketDTO.class);
            socketDTO=socketDTO1;
            return socketDTO.getSocketCommand();
        }catch(JsonToObjectConvertionException e){
            return SocketCommands.INVALID;
        }
    }
    private String handleThisCommad(SocketCommands command){
        String result=null;
        switch (command){
            case LOAD_DATA_FRAME:{
                return  SparkService.getSparkService().loadDataFrame(socketDTO.getFilepath(),socketDTO.getFields(),socketDTO.getTableName());
            }
            case PERFORM_SQL_QUERY:{
                return SparkService.getSparkService().performSelectQuery(socketDTO.getQuery(),socketDTO.getTableName(),socketDTO.getFields());
            }
            case INVALID:{
                result="Invalid Command received by Server";
            }
        }
        return result;
    }
}
