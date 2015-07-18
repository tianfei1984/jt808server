package com.ltmonitor.app;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.ltmonitor.jt808.model.Parameter;
import com.ltmonitor.jt808.protocol.T808Message;

public class GlobalConfig {

	public static Parameter paramModel = new Parameter();

	private static ConcurrentLinkedQueue<T808Message> messageQueueForDisplay = new ConcurrentLinkedQueue();
	/**
	 * 不在界面上显示消息，只显示指令，调试时打开，运行时要关闭，防止消耗内存
	 */
	public static boolean displayMsg = false;
	/**
	 * 控制是否在界面上显示连接信息
	 */
	public static boolean displayConnection = false;
	/**
	 * 总包数
	 */
	public static long totalPacketNum = 0;
	//总定位包数
	public static long totalLocationPacketNum = 0;
	
	public static long connectNum = 0;
	public static String filterSimNo = "";

	public static void putMsg(T808Message tm) {
		if (displayMsg == false) {
			int msgType = tm.getMessageType();
			if (msgType == 0x0002 || msgType == 0x0200 || msgType == 0x8001
					|| msgType == 0x0102 || msgType == 0x8100
					|| msgType == 0x0100
					|| msgType == 0x0003
					)
				return;
		}
		if(filterSimNo!= null && filterSimNo.length() > 0 )
		{
			if(tm.getSimNo() == null || tm.getSimNo().indexOf(filterSimNo) < 0)
				return;
		}
		messageQueueForDisplay.add(tm);
	}

	public static T808Message getMsgForDisplay() {
		return messageQueueForDisplay.poll();
	}

}
