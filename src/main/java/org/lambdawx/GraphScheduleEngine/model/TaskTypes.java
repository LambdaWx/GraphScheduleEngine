package org.lambdawx.GraphScheduleEngine.model;

/**
 * Created by L on 2016/12/9.
 * 任务类别枚举
 * 目前仅支持hive-sql类别的task，其他类型的task标记为other
 */
public enum TaskTypes {
    // hive sql task
    HIVETASK("hiveTask"),
    // otherTask
    OTHER("otherTask");

    private final String value;

    private TaskTypes(String value){
        this.value = value;
    }


    public String value(){
        return this.value;
    }

    public static TaskTypes valOf(String val){
        if(val.length() < 7)
            return TaskTypes.valueOf(val.toUpperCase());
        else
            return TaskTypes.valueOf(val.toUpperCase());
    }
    @Override
    public String toString(){
        return this.value;
    }
}
