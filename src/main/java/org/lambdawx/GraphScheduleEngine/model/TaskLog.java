package org.lambdawx.GraphScheduleEngine.model;

/**
 * Created by L on 2016/12/9.
 */
public class TaskLog {
    /*任务ID*/
    private String taskID;
    /*任务开始的时间*/
    private String taskStartTime;
    /*任务结束时间*/
    private String taskEndTime;
    /*任务运行状态 成功、失败、运行中、结束*/
    private String taskStatus;
    /*任务出错日志*/
    private String errorInfo;
    /*备注*/
    private String content;
    public TaskLog(){}
    public TaskLog(String taskID, String taskStartTime, String taskEndTime, String taskStatus, String errorInfo, String content) {
        this.taskID = taskID;
        this.taskStartTime = taskStartTime;
        this.taskEndTime = taskEndTime;
        this.taskStatus = taskStatus;
        this.errorInfo = errorInfo;
        this.content = content;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public String getTaskStartTime() {
        return taskStartTime;
    }

    public void setTaskStartTime(String taskStartTime) {
        this.taskStartTime = taskStartTime;
    }

    public String getTaskEndTime() {
        return taskEndTime;
    }

    public void setTaskEndTime(String taskEndTime) {
        taskEndTime = taskEndTime;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "TaskLog[" +
                "taskID=" + taskID +
                ", taskStartTime=" + taskStartTime +
                ", taskEndTime=" + taskEndTime +
                ", taskStatus=" + taskStatus +
                ", errorInfo=" + errorInfo +
                ", content=" + content +
                ']';
    }
}
