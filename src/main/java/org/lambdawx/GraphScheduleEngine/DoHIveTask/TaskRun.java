package org.lambdawx.GraphScheduleEngine.DoHIveTask;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.log4j.Logger;
import org.lambdawx.GraphScheduleEngine.TaskSchedule.TaskUtil;
import org.lambdawx.GraphScheduleEngine.connection.ActiveMQConnect;
import org.lambdawx.GraphScheduleEngine.connection.HiveJdbcCli;
import org.lambdawx.GraphScheduleEngine.connection.TablePool;
import org.lambdawx.GraphScheduleEngine.model.ConfigStatic;
import org.lambdawx.GraphScheduleEngine.DoHIveTask.model.HiveTaskConfig;
import org.lambdawx.GraphScheduleEngine.model.TaskConfig;
import org.lambdawx.GraphScheduleEngine.model.TaskLog;
import org.lambdawx.GraphScheduleEngine.model.TsakStatus;
import org.lambdawx.GraphScheduleEngine.util.DateTool;

import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.io.IOException;

/**
 * Created by L on 2016/12/9.
 */
public class TaskRun implements Runnable{
    private TaskConfig taskConfig;
    private HiveTaskConfig hiveTaskConfig;
    private TaskLog taskLog;
    private Session session;
    private MessageProducer producer;
    private String destination;
    private static Logger LOG = Logger.getLogger(TaskRun.class);
    public TaskRun(TaskConfig taskConfig, String destination, Session session){
        this.taskConfig = taskConfig;
        this.session = session;
        this.destination = destination;
    }

    /**
     * 完成HiveTaskConfig、TaskLog的初始化
     */
    public void init(){
        String taskID = taskConfig.getTaskID();
        try {
            this.producer = ActiveMQConnect.getMessageProducer(destination,session);
        }catch (Exception e){
            LOG.error("run task taskID:" + taskID + " faild:"+ e);
            e.printStackTrace();
        }

        hiveTaskConfig = null;
        try {
            HTableInterface htable = TablePool.getHTable(ConfigStatic.HiveTaskConfigHTable);
            Result res = htable.get(new Get(taskID.getBytes()));
            hiveTaskConfig = HiveTaskUtil.resToConfigBean(res);
            try {
                htable.close();
            } catch (IOException e) {
                LOG.error(e);
                e.printStackTrace();
            }
        }catch (Exception e){
            LOG.error("run task taskID:" + taskID + " faild:"+ e);
            e.printStackTrace();
        }
        taskLog = new TaskLog();
        taskLog.setTaskID(taskID);
        taskLog.setTaskStartTime(DateTool.getMySQLDate());
    }
    public void run(){
        init();
        putToHtable(taskLog.getTaskID(), ConfigStatic.TaskLogHTable, TaskUtil.taskLogToPut(taskLog));
        /*判断任务是否有效*/
        if(taskConfig.isTaskAuth() && taskConfig.isTaskStatus()){
            //执行hive任务
            try {
                //解析任务
                String taskSql = parseHiveSql(hiveTaskConfig);
                //执行该sql任务
                HiveJdbcCli.executeSql(taskSql);
                taskLog.setTaskStatus(TsakStatus.SUCCESS.value());
            }catch (Exception e){
                taskLog.setTaskStatus(TsakStatus.FAIL.value());
                taskLog.setErrorInfo(e.toString());
                LOG.error("run task taskID:" + taskLog.getTaskID() + " faild:"+ e);
                e.printStackTrace();
            }

        }else{
            /*无效任务*/
            taskLog.setContent("任务未启用或者审核未通过");
            taskLog.setTaskStatus(TsakStatus.FINISH.value());
            taskLog.setTaskEndTime(DateTool.getMySQLDate());
        }
        /*记录日志*/
        putToHtable(taskLog.getTaskID(), ConfigStatic.TaskLogHTable, TaskUtil.taskLogToPut(taskLog));
        /*发送任务结束消息*/
        try {
            TextMessage messageSend = session
                    .createTextMessage(taskLog.getTaskID() + ConfigStatic.REGX + taskLog.getTaskStatus());
            // 发送消息到目的地方
            System.out.println("send message to MQ:"+messageSend.getText());
            LOG.info("send message to MQ:"+messageSend.getText());
            producer.send(messageSend);
        }catch (Exception e){
            LOG.error("run task taskID:" + taskLog.getTaskID() + " faild:" + e);
            e.printStackTrace();
        }

        close();
    }

    public void putToHtable(String taskID, String htableName, Put put){
        try {
            HTableInterface htable = TablePool.getHTable(htableName);
            htable.put(put);
            try {
                htable.close();
            } catch (IOException e) {
                LOG.error(e);
                e.printStackTrace();
            }
        }catch (Exception e){
            LOG.error("run task taskID:" + taskLog.getTaskID() + " faild:"+ e);
            e.printStackTrace();
        }
    }
    public String parseHiveSql(HiveTaskConfig hiveTaskConfig){
        String taskSql = hiveTaskConfig.getTaskSql();
        System.out.print("taskSql:"+taskSql+"\n");
        boolean isTimePartition = hiveTaskConfig.isTimePartition();
        if(isTimePartition){
            String dateFormat = hiveTaskConfig.getDateFormat();
            String replaceDs = hiveTaskConfig.getReplaceDs();
            String taskType = hiveTaskConfig.getTaskType();
            if(taskType.equals(ConfigStatic.REPORT_MONTH)){
                /*月度任务*/

            }else if(taskType.equals(ConfigStatic.REPORT_WEEk)){
                /*周任务*/

            }else{
                /*默认按天执行*/
                String lastDay = DateTool.getLastDay(dateFormat);
                taskSql = taskSql.replace(replaceDs, lastDay);
            }
        }
        return taskSql;
    }

    public void close(){
        if(producer!=null){
            try{
                producer.close();
            }catch (Exception e){
                LOG.error(e);
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        String dateFormat = "yyyyMMdd";
        String lastDay = DateTool.getLastDay(dateFormat);
        System.out.print(lastDay);
    }
}
