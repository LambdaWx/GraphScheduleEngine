package org.lambdawx.GraphScheduleEngine.TaskSchedule;

import org.apache.log4j.Logger;
import org.lambdawx.GraphScheduleEngine.cache.Caches;
import org.lambdawx.GraphScheduleEngine.connection.ActiveMQConnect;
import org.lambdawx.GraphScheduleEngine.model.ConfigStatic;
import org.lambdawx.GraphScheduleEngine.model.TaskConfig;
import org.lambdawx.GraphScheduleEngine.model.TaskTypes;

import javax.jms.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by L on 2016/12/12.
 * 运行独立的任务独立任务不需要依赖其他任务，任务完成后，将通过MQ报告自己的任务状态
 */
public class IndependentTask  extends TimerTask {
    private static Connection connection = null;
    private static Session session = null;
    private static Map<String,MessageProducer> producerMap = null;
    private static Logger Log = Logger.getLogger(IndependentTask.class);
    static{
        init();
    }
    private static void init() {
        try {
            connection = ActiveMQConnect.getConnection();
            session = ActiveMQConnect.getSession(connection);
            producerMap = new HashMap<String, MessageProducer>();
        }catch (Exception e){
            Log.error(e);
        }
    }
    @Override
    public void run() {
        try {
            List<TaskConfig> configs = TaskUtil.getTaskConfig();//任务配置

            List<String> ids = new ArrayList<String>();
            for(TaskConfig conf : configs){
                ids.add(conf.getTaskID());
                long delay;
                if(Caches.runningTask.containsKey(conf.getTaskID())){//正在运行的任务包含该配置ID
                    if(!Caches.runningTask.get(conf.getTaskID()).compareTo(conf)){
                        //任务发生更改，删除旧版本任务
                        Caches.runningTask.remove(conf.getTaskID());
                        //调用timer.cancel() 结束timerTask，如果timerTask正在运行 则执行完当次之后才不会继续执行
                        Caches.runningTimer.get(conf.getTaskID()).cancel();
                        Caches.runningTimer.remove(conf.getTaskID());
                        delay = creatTimer(conf);//将改变后的任务加入其中
                        Log.info("调度任务“"+conf.getTaskID()+"”发生更改,将于"+delay/3600000+"小时"
                                +(delay%3600000)/60000+"分后执行!");
                    }
                } else{
                    delay = creatTimer(conf);
                    Log.info("调度任务“"+conf.getTaskID()+"”初始化完成,将于"+delay/3600000+"小时"
                            +(delay%3600000)/60000+"分后执行!");
                }
            }
            //移除已失效任务
            for(String key : Caches.runningTask.keySet()){
                if(!ids.contains(key)){
                    Log.info("调度任务“"+Caches.runningTask.get(key).getTaskID()+"”失效,移除定时状态!");
                    Caches.runningTask.remove(key);
                    Caches.runningTimer.get(key).cancel();
                    Caches.runningTimer.remove(key);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取下一次任务执行时间
     * @param start_time 开始时间(yyyyMMddHHmmss)
     * @param interval	任务调度间隔
     * @return
     * 		下次任务执行时间与当前时间差
     * @throws Exception
     */
    private long getDelay(String start_time, long interval) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        long start = dateFormat.parse(start_time).getTime();
        long now = System.currentTimeMillis();
        while(start < now)
            start += interval*1000;
        return start - now;
    }

    /**
     * 创建定时任务
     * @param conf
     * @return
     * @throws Exception
     */
    private long creatTimer(TaskConfig conf) throws Exception{
                                /*执行一个hive任务*/
        if( TaskTypes.HIVETASK.value().equals(conf.getTaskType()) ){

        }else{
            /*其他任务*/
            //在此发送启动taskid的消息
            //执行该taskid的worker消费该消息并执行任务
        }
        int Interval = 86400;
        long delay = getDelay(conf.getTaskStartTime(), Interval);
        Timer timer = new Timer();
        timer.schedule(new ProcessTask(conf), delay, Interval*1000);
        Caches.runningTask.put(conf.getTaskID(), conf);
        Caches.runningTimer.put(conf.getTaskID(), timer);
        return delay;
    }
    public class ProcessTask extends TimerTask {
        private TaskConfig taskConfig;

        public ProcessTask(TaskConfig conf) {
            this.taskConfig = conf;
        }

        @Override
        public void run() {
            try {
                MessageProducer producer = null;
                synchronized (producerMap) {
                    if (!producerMap.containsKey(taskConfig.getTaskType())) {
                        producer = ActiveMQConnect.getMessageProducer(taskConfig.getTaskType(), session);
                        producerMap.put(taskConfig.getTaskType(), producer);
                    } else {
                        producer = producerMap.get(taskConfig.getTaskType());
                    }
                }
                if (producer != null) {
                    /*发送任务结束消息*/
                    try {
                        TextMessage messageSend = session
                                .createTextMessage(taskConfig.getTaskID() + ConfigStatic.REGX + taskConfig.getTaskType());
                        // 发送消息到目的地方
                        System.out.println("send message to " + taskConfig.getTaskType() + " MQ:" + messageSend.getText());
                        Log.info("send message to " + taskConfig.getTaskType() + " MQ:" + messageSend.getText());
                        producerMap.get(taskConfig.getTaskType()).send(messageSend);
                    } catch (Exception e) {
                        Log.error("send message to " + taskConfig.getTaskType() + " MQ  faild:" + e);
                        e.printStackTrace();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}