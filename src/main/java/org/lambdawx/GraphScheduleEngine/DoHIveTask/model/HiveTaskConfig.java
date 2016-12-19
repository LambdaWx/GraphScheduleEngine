package org.lambdawx.GraphScheduleEngine.DoHIveTask.model;

/**
 * Created by L on 2016/12/9.
 */
public class HiveTaskConfig {
    /*任务ID*/
    private String taskID;
    /*任务Sql*/
    private String taskSql;
    /*sql中释放需要时间替换,针对时间分区计算的sql任务*/
    private boolean isTimePartition;
    /*时间格式化类型*/
    private String dateFormat;
    /*hivesql中等时间替换的字符标记*/
    private String replaceDs;
    /*任务类别-日任务、周任务、月任务*/
    private String taskType;


    public HiveTaskConfig(){

    }
    public HiveTaskConfig(String taskID, String taskSql, boolean isTimePartition, String dateFormat, String replaceDs, String taskType) {
        this.taskID = taskID;
        this.taskSql = taskSql;
        this.isTimePartition = isTimePartition;
        this.dateFormat = dateFormat;
        this.replaceDs = replaceDs;
        this.taskType = taskType;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public String getTaskSql() {
        return taskSql;
    }

    public void setTaskSql(String taskSql) {
        this.taskSql = taskSql;
    }

    public boolean isTimePartition() {
        return isTimePartition;
    }

    public void setIsTimePartition(boolean isTimePartition) {
        this.isTimePartition = isTimePartition;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getReplaceDs() {
        return replaceDs;
    }

    public void setReplaceDs(String replaceDs) {
        this.replaceDs = replaceDs;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    @Override
    public String toString() {
        return "HiveTaskConfig[" +
                "taskID=" + taskID +
                ", taskSql=" + taskSql +
                ", isTimePartition=" + isTimePartition +
                ", dateFormat=" + dateFormat +
                ", replaceDs=" + replaceDs +
                ", taskType=" + taskType +
                ']';
    }
}
