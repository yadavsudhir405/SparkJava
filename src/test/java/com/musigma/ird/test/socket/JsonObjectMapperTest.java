package com.musigma.ird.test.socket;

import com.musigma.ird.exception.JsonToObjectConvertionException;
import com.musigma.ird.sparkjava.core.SocketDTO;
import com.musigma.ird.utils.JsonObjectMapper;

/**
 * @author sudhir
 *         Date:20/9/16
 *         Time:11:41 AM
 *         Project:SparkJava
 */
public class JsonObjectMapperTest {
    public static void main(String[] args) {
        //String inputJson="{\"socketCommand\":\"LOAD_DATA_FRAME\",\"fields\":[{\"type\":\"INTEGR\",\"name\":\"id\"},{\"type\":\"STRING\",\"name\":\"name\"},{\"type\":\"STRING\",\"name\":\"email\"}],\"query\":\"select name,email,id from User limit 10\",\"filepath\":\"hdfs://mastodon1.musigma.com:8020/user/musigma/vcldemo/users.json\",\"tableName\":\"User\"}";
        String inputJson="{\n" +
                "  \"socketCommand\" : \"INVALID\",\n" +
                "  \"fields\" : [ {\n" +
                "    \"type\" : \"String\",\n" +
                "    \"name\" : \"name\"\n" +
                "  } ],\n" +
                "  \"query\" : null,\n" +
                "  \"filepath\" : \"safsda\",\n" +
                "  \"tableName\" : null\n" +
                "}";
        try{
            SocketDTO socketDTO= JsonObjectMapper.getInstanceFromJson(inputJson,SocketDTO.class);
        }catch(JsonToObjectConvertionException e){
            e.printStackTrace();
        }
       /* SocketDTO dto=new SocketDTO();
        dto.setFilepath("safsda");
        dto.setSocketCommand(SocketCommands.INVALID);
        List<Field>fields=new ArrayList<>();
        Field filed=new Field(Type.STRING,"name");
        fields.add(filed);
        dto.setFields(fields);
        System.out.println(JsonObjectMapper.getJsonStringFromJavaObject(dto));*/

    }
}
