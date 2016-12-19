package org.lambdawx.GraphScheduleEngine.model;

import java.util.List;

/**
 * Created by L on 2016/12/9.
 */
public class ConfigStatic {
    /*htable*/
    public static byte[] FAMILY = "info".getBytes();
    public static String TaskConfigHTable = "taskConfig";
    public static String HiveTaskConfigHTable = "hiveTaskConfig";
    public static String TaskLogHTable = "taskLog";
    public static String REGX = ",#,";

    // 时间格式
    public static String HOURDATEFORMAT = "yyyy-MM-dd-HH";
    public static String DAYDATEFORMAT = "yyyyMMdd";
    public static String FULLDATEFORMAT = "yyyy-MM-dd HH:mm:ss";
    public static String DAYSTART = "-00";
    public static String DAYEND = "-23";

    public static String REPORT_DAY = "report_day";
    public static String REPORT_WEEk = "report_week";
    public static String REPORT_MONTH = "report_month";


    /*任务ID*/
    public static  String TaskID = "taskID";
    /*任务开始的时间*/
    public static  String TaskStartTime = "taskStartTime";
    /*任务结束时间*/
    public static  String TaskEndTime = "taskEndTime";
    /*任务运行状态 成功、失败、运行中、结束*/
    public static  String TaskStatus = "taskStatus";
    /*任务出错日志*/
    public static  String ErrorInfo = "errorInfo";
    /*备注*/
    public static  String Content = "content";


    /*任务别名*/
    public static   String TaskName = "taskName";
    /*任务ID*/
    //    public static   String TaskID = "taskID";
    /*任务类别*/
    public static   String TaskType = "taskType";
    /*任务状态 启用任务为true,停用任务为false*/
//    public static   String TaskStatus = "taskStatus";
    /*任务审核状态，审核通过为true*/
    public static   String TaskAuth = "taskAuth";
    /*当前任务失败时，依赖于此任务的后置任务释放仍然运行*/
    public static   String IgnoreFail = "ignoreFail";
    /*该任务依赖的前置任务*/
    public static   String DependIDs = "dependIDs";
    /*任务备注，说明*/
//    public static   String Content = "content";
    /*任务的启动时间*/
//    public static   String TaskStartTime = "taskStartTime";

}
