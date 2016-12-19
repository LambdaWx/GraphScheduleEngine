package org.lambdawx.GraphScheduleEngine;

import org.lambdawx.GraphScheduleEngine.DoHIveTask.HiveTaskSchedule;

/**
 * Created by L on 2016/12/9.
 */
public class TaskTest2 {
    public static void main(String[] args) throws Exception{
        HiveTaskSchedule hiveTaskSchedule = new HiveTaskSchedule();
        hiveTaskSchedule.doDependTask();

    }
}
