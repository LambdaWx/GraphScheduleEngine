package org.lambdawx.GraphScheduleEngine.DoHIveTask;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;
import org.apache.log4j.Logger;
import org.datanucleus.util.StringUtils;
import org.lambdawx.GraphScheduleEngine.TaskSchedule.TaskUtil;
import org.lambdawx.GraphScheduleEngine.connection.ActiveMQConnect;
import org.lambdawx.GraphScheduleEngine.connection.TablePool;
import org.lambdawx.GraphScheduleEngine.model.ConfigStatic;
import org.lambdawx.GraphScheduleEngine.model.TaskConfig;
import org.lambdawx.GraphScheduleEngine.model.TaskTypes;

import javax.jms.Connection;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by L on 2016/12/12.
 * 接受MQ的启动消息，并启动任务
 * 任务完成后，将消息返送给MQ
 */
public class HiveTaskSchedule {
    private static Connection connection = null;
    private static Session session = null;
    private static MessageConsumer consumer = null;
    private static ThreadPoolExecutor threadPoolExecutor;
    private static Logger LOG = Logger.getLogger(HiveTaskSchedule.class);
    static{
        init();
    }

    private static void init() {
        try {
            connection = ActiveMQConnect.getConnection();
            session = ActiveMQConnect.getSession(connection);
            consumer = ActiveMQConnect.getMessageConsumer(TaskTypes.HIVETASK.value(),session);
        }catch (Exception e){
            LOG.error(e);
        }
        threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
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
                    HTableInterface htable = TablePool.getHTable(ConfigStatic.TaskConfigHTable);
                    Result res = htable.get(new Get(taskId.getBytes()));

                    TaskConfig taskConfig = TaskUtil.resToTaskConfigBean(res);
                    System.out.print(taskId);
                    System.out.print(taskConfig.toString());

                    TaskRun taskRun = new TaskRun(taskConfig,"ReduceQueue",session);
                    threadPoolExecutor.execute(taskRun);
                }

            }catch (Exception e){
                LOG.error(e);
                e.printStackTrace();
            }
        }
    }

}
