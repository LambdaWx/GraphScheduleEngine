package org.lambdawx.GraphScheduleEngine;

import org.lambdawx.GraphScheduleEngine.TaskSchedule.TaskScheduleManage;

/**
 * Created by L on 2016/12/9.
 */
public class TaskTest {
    public static void main(String[] args){
        TaskScheduleManage taskScheduleManage = new TaskScheduleManage();
        taskScheduleManage.doDependTask();
    }
}
