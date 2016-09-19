package com.musigma.ird.sparkjava.core;

import com.musigma.ird.utils.ClassGenerator;
import com.musigma.ird.utils.FileZipUtil;
import com.musigma.ird.utils.JsonObjectMapper;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author sudhir
 *         Date:19/9/16
 *         Time:4:19 PM
 *         Project:SparkJava
 */
public class SparkService {
    private static final Logger LOGGER= LoggerFactory.getLogger(SparkService.class);
    private static final String CLASSFILES_DIRECTORY="/tmp/classFiles";
    private static final String JAR_NAME="required.jar";
    private static final String JAR_FILE=CLASSFILES_DIRECTORY+"/"+JAR_NAME;
    private static volatile SparkService sparkService;
    private final JavaSparkContext sparkContext;
    private final  SQLContext sqlContext;
    private final Map<String,DataFrame> tableDataFrameMapper;
    private SparkService(String applicationName,String sparkNode){
        if(sparkService!=null){
            throw new AssertionError("Multiple instance of SparkService is not allowed");
        }
        sparkContext=new JavaSparkContext(new SparkConf().setAppName(applicationName).setMaster(sparkNode));
        sqlContext=new SQLContext(sparkContext);
        tableDataFrameMapper=new ConcurrentHashMap<String,DataFrame>();
    }

    public static SparkService intializeSparkContext(String applicationName,String sparkNode){
        if(sparkService==null){
            synchronized (SparkService.class){
                if(sparkService==null){
                    sparkService=new SparkService(applicationName, sparkNode);
                }
            }
        }
        return sparkService;
    }

    public static SparkService getSparkService(){
        return sparkService;
    }

    public String performSelectQuery(String query,String tableName,List<Field> fields){
        DataFrame dataFrame=tableDataFrameMapper.get(tableName);
        if(dataFrame==null){
            LOGGER.info("No dataframe found, with table name "+tableName);
            return "No dataframe found, with table name "+tableName;
        }
        Object object=null;
        synchronized (sparkService){
            ClassGenerator.getObjectWithFields(tableName, fields);
            FileZipUtil.createjarFile(CLASSFILES_DIRECTORY,JAR_NAME);
            sparkContext.addJar(JAR_FILE);
        }
        return executeQuery(query,object,fields);
    }
    private String executeQuery(String query,Object obj,List<Field> fields){
        DataFrame dataFrame=sqlContext.sql(query);
        final Class cl = obj.getClass();
        List<Object> rows=dataFrame.javaRDD().map(new Function<Row, Object>() {
            @Override
            public Object call(Row row) throws Exception {
                Object obj1 = cl.newInstance();
                Class tt = obj1.getClass();
                for (int i = 0; i < fields.size(); i++) {

                    tt.getField(fields.get(i).getName()).set(obj1, String.valueOf(row.get(i)));

                }
                return obj1;
            }
        }).collect();
        return JsonObjectMapper.getJsonStringFromJavaObject(rows);
    }

    public String loadDataFrame(String hdfsFilePath,List<Field> fields,String tableName){
        DataFrame dataFrame=sqlContext.read().json(hdfsFilePath);
        dataFrame.registerTempTable(tableName);
        tableDataFrameMapper.putIfAbsent(tableName,dataFrame);
        return "tableName is registered as dataframe";
    }


}
