package org.lambdawx.GraphScheduleEngine.model;

/**
 * Created by L on 2016/12/9.
 */
public enum TsakStatus {
    // hive sql task
    SUCCESS("success"),
    // otherTask
    FAIL("fail"),
    FINISH("finish");

    private final String value;

    private TsakStatus(String value){
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
