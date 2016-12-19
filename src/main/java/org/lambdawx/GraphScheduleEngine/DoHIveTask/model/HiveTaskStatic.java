package org.lambdawx.GraphScheduleEngine.DoHIveTask.model;

/**
 * Created by L on 2016/12/9.
 */
public class HiveTaskStatic {
    public static byte[] FAMILY = "info".getBytes();
    /*任务ID*/
    public static String TaskID = "taskID";
    /*任务Sql*/
    public static String TaskSql = "taskSql";
    /*sql中释放需要时间替换,针对时间分区计算的sql任务*/
    public static String isTimePartition = "isTimePartition";
    /*时间格式化类型*/
    public static String DateFormat = "dateFormat";
    /*hivesql中等时间替换的字符标记*/
    public static String ReplaceDs = "replaceDs";
    /*任务类别-日任务、周任务、月任务*/
    public static String TaskType = "taskType";
    public static String REGX = ",#,";
}
