package org.lambdawx.GraphScheduleEngine.DoHIveTask;

import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.lambdawx.GraphScheduleEngine.DoHIveTask.model.HiveTaskConfig;
import org.lambdawx.GraphScheduleEngine.DoHIveTask.model.HiveTaskStatic;
import org.lambdawx.GraphScheduleEngine.connection.TablePool;
import org.lambdawx.GraphScheduleEngine.model.ConfigStatic;

import java.io.IOException;

/**
 * Created by L on 2016/12/9.
 */
public class HiveTaskUtil {
    public static Put hiveTaskConfigToPut(HiveTaskConfig hiveTaskConfig){
        Put put = new Put((hiveTaskConfig.getTaskID()).getBytes());
        put.add(HiveTaskStatic.FAMILY, HiveTaskStatic.TaskID.getBytes(), hiveTaskConfig.getTaskID().getBytes());
        if(null != hiveTaskConfig.getTaskID())
            put.add(HiveTaskStatic.FAMILY, HiveTaskStatic.TaskID.getBytes(), Bytes.toBytes(hiveTaskConfig.getTaskID()));

        if(null != hiveTaskConfig.getTaskSql())
            put.add(HiveTaskStatic.FAMILY, HiveTaskStatic.TaskSql.getBytes(), hiveTaskConfig.getTaskSql().getBytes());

        put.add(HiveTaskStatic.FAMILY, HiveTaskStatic.isTimePartition.getBytes(), Bytes.toBytes(hiveTaskConfig.isTimePartition()));

        if(null != hiveTaskConfig.getDateFormat())
            put.add(HiveTaskStatic.FAMILY, HiveTaskStatic.DateFormat.getBytes(), Bytes.toBytes((hiveTaskConfig.getDateFormat())));

        if(null != hiveTaskConfig.getReplaceDs())
            put.add(HiveTaskStatic.FAMILY, HiveTaskStatic.ReplaceDs.getBytes(), Bytes.toBytes(hiveTaskConfig.getReplaceDs()));

        if(null != hiveTaskConfig.getTaskType())
            put.add(HiveTaskStatic.FAMILY, HiveTaskStatic.TaskType.getBytes(), Bytes.toBytes(hiveTaskConfig.getTaskType()));
        return put;
    }

    /**
     *
     * @param re
     * @return
     */
    public static HiveTaskConfig resToConfigBean(Result re) {
        if (null == re) {
            return null;
        }
        HiveTaskConfig bean = new HiveTaskConfig();
        bean.setTaskSql(null != re.getValue(HiveTaskStatic.FAMILY, Bytes.toBytes(HiveTaskStatic.TaskID)) ?
                Bytes.toString(re.getValue(HiveTaskStatic.FAMILY, Bytes.toBytes(HiveTaskStatic.TaskID))):"");
        bean.setTaskSql(null != re.getValue(HiveTaskStatic.FAMILY, Bytes.toBytes(HiveTaskStatic.TaskSql)) ?
                Bytes.toString(re.getValue(HiveTaskStatic.FAMILY, Bytes.toBytes(HiveTaskStatic.TaskSql))):"");
        bean.setIsTimePartition(null != re.getValue(HiveTaskStatic.FAMILY, Bytes.toBytes(HiveTaskStatic.isTimePartition)) ?
                Bytes.toBoolean(re.getValue(HiveTaskStatic.FAMILY, Bytes.toBytes(HiveTaskStatic.isTimePartition))):false);
        bean.setDateFormat(null != re.getValue(HiveTaskStatic.FAMILY, Bytes.toBytes(HiveTaskStatic.DateFormat)) ?
                Bytes.toString(re.getValue(HiveTaskStatic.FAMILY, Bytes.toBytes(HiveTaskStatic.DateFormat))) : "");
        bean.setReplaceDs(null != re.getValue(HiveTaskStatic.FAMILY, Bytes.toBytes(HiveTaskStatic.ReplaceDs)) ?
                Bytes.toString(re.getValue(HiveTaskStatic.FAMILY, Bytes.toBytes(HiveTaskStatic.ReplaceDs))) : "");
        bean.setTaskType(null != re.getValue(HiveTaskStatic.FAMILY, Bytes.toBytes(HiveTaskStatic.TaskType)) ?
                Bytes.toString(re.getValue(HiveTaskStatic.FAMILY, Bytes.toBytes(HiveTaskStatic.TaskType))) : "");
        return bean;
    }


    public static void hiveTaskConfigIn(String taskID, String taskSql, boolean isTimePartition, String dateFormat,
                                        String replaceDs, String taskType){
        HiveTaskConfig hiveTaskConfig = new HiveTaskConfig( taskID,  taskSql,  isTimePartition,
                                                         dateFormat,  replaceDs,  taskType);
        Put put = HiveTaskUtil.hiveTaskConfigToPut(hiveTaskConfig);
        try {
            HTableInterface htable = TablePool.getHTable(ConfigStatic.HiveTaskConfigHTable);
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

}
