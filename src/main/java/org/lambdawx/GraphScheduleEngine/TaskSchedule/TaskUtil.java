package org.lambdawx.GraphScheduleEngine.TaskSchedule;

import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.lambdawx.GraphScheduleEngine.DoHIveTask.model.HiveTaskStatic;
import org.lambdawx.GraphScheduleEngine.connection.TablePool;
import org.lambdawx.GraphScheduleEngine.model.ConfigStatic;
import org.lambdawx.GraphScheduleEngine.model.TaskConfig;
import org.lambdawx.GraphScheduleEngine.model.TaskLog;
import org.lambdawx.GraphScheduleEngine.util.DateTool;

import java.io.IOException;
import java.util.*;

/**
 * Created by L on 2016/12/9.
 */
public class TaskUtil {
    /**
     * TaskConfig转换为hbase put
     * @param taskConfig
     * @return
     */
    public static Put taskConfigToPut(TaskConfig taskConfig){
        Put put = new Put((taskConfig.getTaskID()).getBytes());
        put.add(HiveTaskStatic.FAMILY, "taskID".getBytes(), taskConfig.getTaskID().getBytes());
        if(null != taskConfig.getTaskID())
            put.add(HiveTaskStatic.FAMILY, "taskName".getBytes(), Bytes.toBytes(taskConfig.getTaskID()));

        if(null != taskConfig.getTaskType())
            put.add(HiveTaskStatic.FAMILY, "taskType".getBytes(), taskConfig.getTaskType().getBytes());

        put.add(HiveTaskStatic.FAMILY, "taskStatus".getBytes(), Bytes.toBytes(taskConfig.isTaskStatus()));

        put.add(HiveTaskStatic.FAMILY, "taskAuth".getBytes(), Bytes.toBytes(taskConfig.isTaskAuth()));

        if(null != taskConfig.getContent())
            put.add(HiveTaskStatic.FAMILY, "ignoreFail".getBytes(), Bytes.toBytes((taskConfig.isIgnoreFail())));

        if(null != taskConfig.getDependIDs())
            put.add(HiveTaskStatic.FAMILY, "dependIDs".getBytes(), Bytes.toBytes(listToString(taskConfig.getDependIDs())));

        if(null != taskConfig.getContent())
            put.add(HiveTaskStatic.FAMILY, "content".getBytes(), Bytes.toBytes(taskConfig.getContent()));

        if(null != taskConfig.getTaskStartTime())
            put.add(HiveTaskStatic.FAMILY, "taskStartTime".getBytes(),  Bytes.toBytes(taskConfig.getTaskStartTime()));
        return put;
    }
    /**TaskLog
     * hbase记录转换为
     * @param re
     * @return
     */
    public static TaskConfig resToTaskConfigBean(Result re) {
        if (null == re) {
            return null;
        }
        TaskConfig bean = new TaskConfig();
        bean.setTaskID(null != re.getValue(ConfigStatic.FAMILY, Bytes.toBytes(ConfigStatic.TaskID)) ?
                Bytes.toString(re.getValue(ConfigStatic.FAMILY, Bytes.toBytes(ConfigStatic.TaskID))) : "");

        bean.setTaskName(null != re.getValue(ConfigStatic.FAMILY, Bytes.toBytes(ConfigStatic.TaskName)) ?
                Bytes.toString(re.getValue(ConfigStatic.FAMILY, Bytes.toBytes(ConfigStatic.TaskName))) : "");

        bean.setTaskType(null != re.getValue(ConfigStatic.FAMILY, Bytes.toBytes(ConfigStatic.TaskType)) ?
                Bytes.toString(re.getValue(ConfigStatic.FAMILY, Bytes.toBytes(ConfigStatic.TaskType))) : "");

        bean.setTaskStatus(null != re.getValue(ConfigStatic.FAMILY, Bytes.toBytes(ConfigStatic.TaskStatus)) ?
                Bytes.toBoolean(re.getValue(ConfigStatic.FAMILY, Bytes.toBytes(ConfigStatic.TaskStatus))) : false);

        bean.setTaskAuth(null != re.getValue(ConfigStatic.FAMILY, Bytes.toBytes(ConfigStatic.TaskAuth)) ?
                Bytes.toBoolean(re.getValue(ConfigStatic.FAMILY, Bytes.toBytes(ConfigStatic.TaskAuth))) : false);

        bean.setIgnoreFail(null != re.getValue(ConfigStatic.FAMILY, Bytes.toBytes(ConfigStatic.IgnoreFail)) ?
                Bytes.toBoolean(re.getValue(ConfigStatic.FAMILY, Bytes.toBytes(ConfigStatic.IgnoreFail))) : false);

        bean.setDependIDs(null != re.getValue(ConfigStatic.FAMILY, Bytes.toBytes(ConfigStatic.DependIDs)) ?
                Arrays.asList(
                        (Bytes.toString(re.getValue(ConfigStatic.FAMILY, Bytes.toBytes(ConfigStatic.DependIDs)))).split(",")): null);

        bean.setContent(null != re.getValue(ConfigStatic.FAMILY, Bytes.toBytes(ConfigStatic.Content)) ?
                Bytes.toString(re.getValue(ConfigStatic.FAMILY, Bytes.toBytes(ConfigStatic.Content))) : "");

        bean.setTaskStartTime(null != re.getValue(ConfigStatic.FAMILY, Bytes.toBytes(ConfigStatic.TaskStartTime)) ?
                Bytes.toString(re.getValue(ConfigStatic.FAMILY, Bytes.toBytes(ConfigStatic.TaskStartTime))) : "");

        return bean;
    }



    /**
     * TaskLog转换为hbase put
     * @param taskLog
     * @return
     */
    public static Put taskLogToPut(TaskLog taskLog){
        Put put = new Put((DateTool.getDayStamp() + ConfigStatic.REGX + taskLog.getTaskID()).getBytes());
        put.add(HiveTaskStatic.FAMILY, "taskID".getBytes(), taskLog.getTaskID().getBytes());
        if(null != taskLog.getTaskStartTime())
            put.add(HiveTaskStatic.FAMILY, "taskStartTime".getBytes(), Bytes.toBytes(taskLog.getTaskStartTime()));
        if(null != taskLog.getTaskEndTime())
            put.add(HiveTaskStatic.FAMILY, "taskEndTime".getBytes(), Bytes.toBytes(taskLog.getTaskEndTime()));
        if(null != taskLog.getTaskStatus())
            put.add(HiveTaskStatic.FAMILY, "taskStatus".getBytes(), Bytes.toBytes(taskLog.getTaskStatus()));
        if(null != taskLog.getErrorInfo())
            put.add(HiveTaskStatic.FAMILY, "errorInfo".getBytes(), Bytes.toBytes(taskLog.getErrorInfo()));
        if(null != taskLog.getContent())
            put.add(HiveTaskStatic.FAMILY, "content".getBytes(), Bytes.toBytes(taskLog.getContent()));
        return put;
    }

    /**TaskLog
     * hbase记录转换为
     * @param re
     * @return
     */
    public static TaskLog resToTaskLogBean(Result re) {
        if (null == re) {
            return null;
        }
        TaskLog bean = new TaskLog();
        bean.setTaskID(null != re.getValue(ConfigStatic.FAMILY, Bytes.toBytes(ConfigStatic.TaskID)) ?
                Bytes.toString(re.getValue(ConfigStatic.FAMILY, Bytes.toBytes(ConfigStatic.TaskID))) : "");
        bean.setTaskStartTime(null != re.getValue(ConfigStatic.FAMILY, Bytes.toBytes(ConfigStatic.TaskStartTime)) ?
                Bytes.toString(re.getValue(ConfigStatic.FAMILY, Bytes.toBytes(ConfigStatic.TaskStartTime))) : "");
        bean.setTaskEndTime(null != re.getValue(ConfigStatic.FAMILY, Bytes.toBytes(ConfigStatic.TaskEndTime)) ?
                Bytes.toString(re.getValue(ConfigStatic.FAMILY, Bytes.toBytes(ConfigStatic.TaskEndTime))) : "");
        bean.setTaskStatus(null != re.getValue(ConfigStatic.FAMILY, Bytes.toBytes(ConfigStatic.TaskStatus)) ?
                Bytes.toString(re.getValue(ConfigStatic.FAMILY, Bytes.toBytes(ConfigStatic.TaskStatus))) : "");
        bean.setErrorInfo(null != re.getValue(ConfigStatic.FAMILY, Bytes.toBytes(ConfigStatic.ErrorInfo)) ?
                Bytes.toString(re.getValue(ConfigStatic.FAMILY, Bytes.toBytes(ConfigStatic.ErrorInfo))) : "");
        bean.setContent(null != re.getValue(ConfigStatic.FAMILY, Bytes.toBytes(ConfigStatic.Content)) ?
                Bytes.toString(re.getValue(ConfigStatic.FAMILY, Bytes.toBytes(ConfigStatic.Content))) : "");
        return bean;
    }

    public static String listToString(List<String> stringList){
        if (stringList==null) {
            return null;
        }
        StringBuilder result=new StringBuilder();
        boolean flag=true;
        for (String string : stringList) {
            if (flag) {
                result.append(",");
            }else {
                flag=true;
            }
            result.append(string);
        }
        String res = result.toString().replaceFirst(",","");
        return res;
    }


    public static void TaskConfigIn(String taskName, String taskID, String taskType, boolean taskStatus, boolean taskAuth, List<String> dependIDs,
                                    String content, boolean ignoreFail, String taskStartTime){
        TaskConfig taskConfig = new TaskConfig( taskName,  taskID,  taskType,  taskStatus,  taskAuth,  dependIDs,
                content,  ignoreFail,  taskStartTime);
        Put put = TaskUtil.taskConfigToPut(taskConfig);
        try {
            HTableInterface htable = TablePool.getHTable(ConfigStatic.TaskConfigHTable);
            htable.put(put);
            try {
                htable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static List<TaskConfig> getTaskConfig(){
        List<TaskConfig> list = new ArrayList<TaskConfig>();
        HTableInterface htable = TablePool.getHTable(ConfigStatic.TaskConfigHTable);
        try{
            ResultScanner rs = htable.getScanner(new Scan());

            for (Result re:rs) {
                TaskConfig taskConfig = TaskUtil.resToTaskConfigBean(re);
                list.add(taskConfig);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                htable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public static Map<String, TaskConfig> getTaskConfigMap(){
        Map<String, TaskConfig> map = new HashMap<String, TaskConfig>();
        HTableInterface htable = TablePool.getHTable(ConfigStatic.TaskConfigHTable);
        try{
            ResultScanner rs = htable.getScanner(new Scan());

            for (Result re:rs) {
                TaskConfig taskConfig = TaskUtil.resToTaskConfigBean(re);
                map.put(taskConfig.getTaskID(), taskConfig);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                htable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

}
