package org.lambdawx.GraphScheduleEngine.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {
	private static ThreadPool pool = new ThreadPool();
    public static ExecutorService getExcute(){
    	
    	return pool.newFixedThreadPool();
    	
    }
	
	public  ExecutorService newFixedThreadPool() {
		return Executors.newCachedThreadPool();
	}

}