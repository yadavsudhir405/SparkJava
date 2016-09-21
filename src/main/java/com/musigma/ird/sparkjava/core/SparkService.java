package com.musigma.ird.sparkjava.core;

import com.musigma.ird.utils.JsonObjectMapper;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author sudhir
 *         Date:19/9/16
 *         Time:4:19 PM
 *         Project:SparkJava
 */
public class SparkService implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER= LoggerFactory.getLogger(SparkService.class);
    private static final String CLASSFILES_DIRECTORY="/tmp/classFiles";
    private static final String JAR_NAME="required.jar";
    private static volatile SparkService sparkService;

    private transient JavaSparkContext sparkContext;
    private transient final  SQLContext sqlContext;
    private final Map<String,DataFrame> tableDataFrameMapper;
    private SparkService(String applicationName,String sparkNode){
        if(sparkService!=null){
            throw new AssertionError("Multiple instance of SparkService is not allowed");
        }
        sparkContext=new JavaSparkContext(new SparkConf().setAppName(applicationName).setMaster(sparkNode));
        sqlContext=new SQLContext(sparkContext);
        tableDataFrameMapper=new ConcurrentHashMap<String,DataFrame>();
        sparkContext.addJar("/home/sudhir/yadavsudhir405/SparkApps/SparkJava/target/sparkJava-1.0-SNAPSHOT.jar");
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
            object= JavaAssistClassManager.INSTANCE.getObjectFromClassName(tableName,fields);
            loadjar(tableName+".jar");
        }
        return executeQuery(query,object,fields);
    }
    private String executeQuery(String query,Object obj,final List<Field> fields){
        DataFrame dataFrame=sqlContext.sql(query);
        dataFrame.show();
        final Class cl = obj.getClass();
        List<Object> rows=dataFrame.javaRDD().map(new Function<Row, Object>() {

            private static final long serialVersionUID = 1L;
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
        dataFrame.cache();
        dataFrame.registerTempTable(tableName);
        tableDataFrameMapper.put(tableName,dataFrame);
        return tableName+" is registered as dataframe";
    }
    private void loadjar(String jarname){
        if (isjarNotLoadedToSparkContext(jarname)){
            LOGGER.info(jarname+" is missing to the sparkContext,Adding Jar");
            sparkContext.addJar(CLASSFILES_DIRECTORY+"/"+jarname);
        }else {
            LOGGER.info(jarname+" is already into the spark contextJar,Skippin  loading  "+jarname+" to SparkContext");
        }

    }
    private boolean isjarNotLoadedToSparkContext(final String jarName){
        List<String> loadedjarList=sparkContext.jars();
        for(String jar:loadedjarList){
            LOGGER.info(jar);
        }
        return loadedjarList.contains(jarName)?false:true;
    }

}
