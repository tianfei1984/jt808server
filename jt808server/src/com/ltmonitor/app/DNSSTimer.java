package com.ltmonitor.app;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.ltmonitor.jt808.service.impl.T808Manager;


/**
 * 定时任务类，暂时不用
 * @author DELL
 *
 */
public class DNSSTimer {
	private Logger logger = Logger.getLogger(DNSSTimer.class);

	private static DNSSTimer instance = null;
	private int counter = 0;
	String message = "";
	public ScheduledExecutorService scheduleService = null;
	
	public DNSSTimer()
	{
		scheduleService = Executors.newScheduledThreadPool(1);
	}

	public static final synchronized DNSSTimer getInstance() {
		if (instance == null) {
			instance = new DNSSTimer();
		}
		return instance;
	}
	
	
	public  final void Stop()
	{
		scheduleService.shutdown();
	}

	public  final boolean start() {
		return false;
	}

	public void run() {
		scheduleService.scheduleAtFixedRate(new Runnable() {
			public void run() {
				DNSSTimer.this.counter += 1;
				if (DNSSTimer.this.counter % 30 == 0) {
					
				}
			}
				
		}, 2L, 1L, TimeUnit.SECONDS);
	}
}
