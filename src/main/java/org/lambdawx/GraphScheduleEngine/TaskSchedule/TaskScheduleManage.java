package org.lambdawx.GraphScheduleEngine.TaskSchedule;

import org.apache.log4j.Logger;
import org.datanucleus.util.StringUtils;
import org.lambdawx.GraphScheduleEngine.connection.ActiveMQConnect;
import org.lambdawx.GraphScheduleEngine.model.ConfigStatic;
import org.lambdawx.GraphScheduleEngine.model.TaskConfig;

import javax.jms.*;
import java.util.*;

/**
 * Created by L on 2016/12/9.
 * GraphScheduleEnginde的核心，复杂任务消息的收发，初始化任务状态等工作
 */
public class TaskScheduleManage {
    /*计划完成的任务*/
    private Map<String, TaskConfig> taskConfigMap;
    /*已经完成的任务*/
    private Map<String, TaskConfig> finishTaskConfigMap;
    private static Connection connection = null;
    private static Session session = null;
    private static MessageConsumer consumer = null;
    private static Map<String,MessageProducer> producerMap = null;
    private static Logger LOG = Logger.getLogger(TaskScheduleManage.class);
    static{
        init();
    }

    public TaskScheduleManage(){
        updateTaskConfig();
        Timer timer1 = new Timer();
        timer1.schedule(new updateTaskConfigTask(), 60*60*1000, 60*60*1000);//每六十分钟更新一次配置

        Timer timer2 = new Timer();
        timer2.schedule(new IndependentTask(), 1000, 60*60*1000);//每六十分钟更新一次配置
    }
    private static void init() {
        try {
            connection = ActiveMQConnect.getConnection();
            session = ActiveMQConnect.getSession(connection);
            consumer = ActiveMQConnect.getMessageConsumer("ReduceQueue",session);
            producerMap = new HashMap<String, MessageProducer>();
        }catch (Exception e){
            LOG.error(e);
        }
    }
    /**
     * 从数据库更新本地的task配置
     */
    private void updateTaskConfig(){
        synchronized (this) {
            if (taskConfigMap == null) {
//            taskConfigMap = new ArrayList<TaskConfig>();
            } else {
                taskConfigMap.clear();
            }
            taskConfigMap = TaskUtil.getTaskConfigMap();
            if (finishTaskConfigMap == null) {
                finishTaskConfigMap = new HashMap<String, TaskConfig>();
            } else {
                finishTaskConfigMap.clear();
            }
            notifyAll();
        }
    }
    public class updateTaskConfigTask extends TimerTask{
        @Override
        public void run() {
            updateTaskConfig();
        }
    }

    /**
     * 执行存在依赖的任务
     */
    public void doDependTask(){
        while(true){
            try {
                TextMessage messageRev = (TextMessage) consumer.receive(100000);
                if (null != messageRev) {
                    String message = messageRev.getText();
                    if(StringUtils.isEmpty(message)) {
                        continue;
                    }
                    System.out.println("收到消息:" + message);
                    String[] splits = message.split(ConfigStatic.REGX);
                    if(splits==null || splits.length<2){
                        continue;
                    }
                    String taskId = splits[0];
                    System.out.print(taskId);
                    System.out.print(taskConfigMap.toString());
                    String status = splits[1];
                    List<TaskConfig> taskConfigList = new ArrayList<TaskConfig>();
                    synchronized (this) {
                        finishTaskConfigMap.put(taskId, taskConfigMap.get(taskId));
                        taskConfigMap.remove(taskId);
                        System.out.print("taskId:" + taskId + "\n");
                        //找到依赖于任务taskId的task
                        for (String tid : taskConfigMap.keySet()) {
                            TaskConfig taskConfig = taskConfigMap.get(tid);
                            if (taskConfig.getDependIDs() != null && taskConfig.getDependIDs().contains(taskId)) {
                            /*确认所有依赖的任务是否完成*/
                                boolean isFinish = true;
                                for (String dtid : taskConfig.getDependIDs()) {
                                    if (!finishTaskConfigMap.containsKey(dtid)) {
                                        isFinish = false;
                                    }
                                }
                            /*所有依赖的任务均已完成*/
                                if (isFinish) {
                                    taskConfigList.add(taskConfig);
                                }
                            }
                        }
                    }
                    System.out.print(taskConfigList.toString() + "\n");
                    /*通过MQ启动taskConfigList的任务*/
                    /*每一类任务将发送给该类任务对应的MQ,任务worker接受该消息后执行任务*/
                    for(TaskConfig taskConfig : taskConfigList){
                        MessageProducer producer = null;
                        if(!producerMap.containsKey(taskConfig.getTaskType()) ){
                            producer = ActiveMQConnect.getMessageProducer(taskConfig.getTaskType(),session);
                            producerMap.put(taskConfig.getTaskType(),producer);
                        }else{
                            producer = producerMap.get(taskConfig.getTaskType());
                        }

                        if(producer!=null){
                            /*发送任务结束消息*/
                            try {
                                TextMessage messageSend = session
                                            .createTextMessage(taskConfig.getTaskID() + ConfigStatic.REGX + taskConfig.getTaskType());
                                // 发送消息到目的地方
                                System.out.println("send message to " + taskConfig.getTaskType() + " MQ:"+messageSend.getText());
                                LOG.info("send message to " + taskConfig.getTaskType() + " MQ:"+messageSend.getText());
                                producerMap.get(taskConfig.getTaskType()).send(messageSend);
                            }catch (Exception e){
                                LOG.error("send message to " + taskConfig.getTaskType() + " MQ  faild:" + e);
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }catch (Exception e){
                LOG.error(e);
                e.printStackTrace();
            }
        }
    }

}
