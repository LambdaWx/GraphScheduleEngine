package org.lambdawx.GraphScheduleEngine.cache;

import org.lambdawx.GraphScheduleEngine.model.TaskConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

/**
 * 任务调度相关配置信息缓存(也可考虑存入memCache)
 */
public class Caches {
	/**正在运行的任务*/
	public static Map<String, TaskConfig> runningTask = new HashMap<String, TaskConfig>();
	/**每个调度的引用缓存*/
	public static Map<String, Timer> runningTimer = new HashMap<String, Timer>();
}
