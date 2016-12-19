package org.lambdawx.GraphScheduleEngine;

import org.lambdawx.GraphScheduleEngine.DoHIveTask.HiveTaskUtil;
import org.lambdawx.GraphScheduleEngine.model.ConfigStatic;

/**
 * Created by L on 2016/12/9.
 */
public class HiveTaskConfigInit {

    public static void main(String[] args){
        /*任务ID*/
         String taskID = "20161009_0002";
        /*任务Sql*/
         String taskSql  = "INSERT INTO TABLE test_db.test2 select * from test_db.test2";
        /*sql中释放需要时间替换,针对时间分区计算的sql任务*/
         boolean isTimePartition = false;
        /*时间格式化类型*/
         String dateFormat = ConfigStatic.DAYDATEFORMAT;
        /*hivesql中等时间替换的字符标记*/
         String replaceDs = "re_ds";
        /*任务类别-日任务、周任务、月任务*/
         String taskType = ConfigStatic.REPORT_DAY;

        HiveTaskUtil.hiveTaskConfigIn(taskID,  taskSql,  isTimePartition,
                dateFormat,  replaceDs,  taskType);


    }
}
