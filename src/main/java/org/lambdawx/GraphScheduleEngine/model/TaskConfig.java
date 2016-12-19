package org.lambdawx.GraphScheduleEngine.model;

import java.util.List;

/**
 * Created by L on 2016/12/9.
 */
public class TaskConfig {
    /*任务别名*/
    private String taskName;
    /*任务ID*/
    private String taskID;
    /*任务类别*/
    private String taskType;
    /*任务状态 启用任务为true,停用任务为false*/
    private boolean taskStatus;
    /*任务审核状态，审核通过为true*/
    private boolean taskAuth;
    /*当前任务失败时，依赖于此任务的后置任务释放仍然运行*/
    private boolean ignoreFail;
    /*该任务依赖的前置任务*/
    private List<String> dependIDs;
    /*任务备注，说明*/
    private String content;
    /*任务的启动时间*/
    private String taskStartTime;

    public TaskConfig(){

    }
    public TaskConfig(String taskName, String taskID, String taskType, boolean taskStatus, boolean taskAuth, List<String> dependIDs,
                      String content, boolean ignoreFail, String taskStartTime) {
        this.taskName = taskName;
        this.taskID = taskID;
        this.taskType = taskType;
        this.taskStatus = taskStatus;
        this.taskAuth = taskAuth;
        this.dependIDs = dependIDs;
        this.content = content;
        this.ignoreFail = ignoreFail;
        this.taskStartTime = taskStartTime;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public boolean isTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(boolean taskStatus) {
        this.taskStatus = taskStatus;
    }

    public boolean isTaskAuth() {
        return taskAuth;
    }

    public void setTaskAuth(boolean taskAuth) {
        this.taskAuth = taskAuth;
    }

    public List<String> getDependIDs() {
        return dependIDs;
    }

    public void setDependIDs(List<String> dependIDs) {
        this.dependIDs = dependIDs;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isIgnoreFail() {
        return ignoreFail;
    }

    public void setIgnoreFail(boolean ignoreFail) {
        this.ignoreFail = ignoreFail;
    }

    public String getTaskStartTime() {
        return taskStartTime;
    }

    public void setTaskStartTime(String taskStartTime) {
        this.taskStartTime = taskStartTime;
    }

    public boolean compareTo(TaskConfig conf) {
        return this.getTaskID().equals(conf.getTaskID())
                && this.getTaskStartTime().equals(conf.getTaskStartTime());
    }

    @Override
    public String toString() {
        return "TaskConfig[" +
                "taskName=" + taskName +
                ", taskID=" + taskID +
                ", taskType=" + taskType +
                ", taskStatus=" + taskStatus +
                ", taskAuth=" + taskAuth +
                ", ignoreFail=" + ignoreFail +
                ", dependIDs=" + dependIDs +
                ", content=" + content +
                ", taskStartTime=" + taskStartTime +
                ']';
    }
}
