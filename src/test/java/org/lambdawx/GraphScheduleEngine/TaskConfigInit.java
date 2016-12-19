package org.lambdawx.GraphScheduleEngine;

import org.lambdawx.GraphScheduleEngine.TaskSchedule.TaskUtil;
import org.lambdawx.GraphScheduleEngine.model.TaskConfig;
import org.lambdawx.GraphScheduleEngine.model.TaskTypes;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by L on 2016/12/9.
 */
public class TaskConfigInit {

    public static void main(String[] args){
        /*任务别名*/
        String taskName1 = "test task1";
        /*任务ID*/
         String taskID1 = "20161009_0001";
        /*任务类别*/
        String taskType = TaskTypes.HIVETASK.value();
        /*任务状态 启用任务为true,停用任务为false*/
        boolean taskStatus = true;
        /*任务审核状态，审核通过为true*/
        boolean taskAuth = true;
        /*当前任务失败时，依赖于此任务的后置任务释放仍然运行*/
        boolean ignoreFail = false;
        /*该任务依赖的前置任务*/
        List<String> dependIDs = null;
        /*任务备注，说明*/
        String content = "this is my first test task";
        /*任务的启动时间*/
        String taskStartTime = "20161212163501";
        TaskUtil.TaskConfigIn(taskName1,  taskID1,  taskType,  taskStatus,  taskAuth,  dependIDs,
                content,  ignoreFail,  taskStartTime);

        String taskName2 = "test task2";
        String taskID2 = "20161009_0002";
        dependIDs = new ArrayList<String>();
        dependIDs.add(taskID1);
        TaskUtil.TaskConfigIn(taskName2,  taskID2,  taskType,  taskStatus,  taskAuth,  dependIDs,
                content,  ignoreFail,  taskStartTime);

        List<TaskConfig> list = TaskUtil.getTaskConfig();
        for(TaskConfig taskConfig: list){
            System.out.print(taskConfig.toString() + "\n");
        }



    }

}
