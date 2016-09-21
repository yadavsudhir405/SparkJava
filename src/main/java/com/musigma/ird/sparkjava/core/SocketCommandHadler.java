package com.musigma.ird.sparkjava.core;

import com.musigma.ird.exception.JsonToObjectConvertionException;
import com.musigma.ird.utils.JsonObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @author sudhir
 *         Date:19/9/16
 *         Time:11:24 AM
 *         Project:SparkJava
 */
public class SocketCommandHadler implements CommandHandler,Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER= LoggerFactory.getLogger(CommandHandler.class);
    private String socketInputString;
    private SocketDTO socketDTO;

    public SocketCommandHadler(String socketInputString) {
        this.socketInputString = socketInputString;
    }

    @Override
    public String handleCommand() {
        LOGGER.info("####################### start handle method ##########################");
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
                LOGGER.info("########################## Handling Load DataFrame Commad ###################################");
                return  SparkService.getSparkService().loadDataFrame(socketDTO.getFilepath(),socketDTO.getFields(),socketDTO.getTableName());
            }
            case PERFORM_SQL_QUERY:{
                LOGGER.info("############################# Handling SQL_QUERY Command ##########################################");
                return SparkService.getSparkService().performSelectQuery(socketDTO.getQuery(),socketDTO.getTableName(),socketDTO.getFields());
            }
            case INVALID:{
                LOGGER.info("##################### INVALID COMMAND RECEIVED ##############################");
                result="Invalid Command received by Server";
            }
        }
        return result;
    }
}
