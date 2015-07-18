package com.ltmonitor.app;

import java.util.HashMap;
import java.util.Hashtable;

public class T809Constants {
	
	public static HashMap<Integer, String> msgMap = new HashMap<Integer, String>();
	
	public static String getMsgDescr(Integer msgId)
	{
		if(msgMap.isEmpty())
			init();
		
		return "" + msgMap.get(msgId);
	}
	
	
	private static void init()
	{		
		msgMap.put(0x1001,"主链路登录请求消息");
		msgMap.put(0x1002,"主链路登录应答消息");
		msgMap.put(0x1003,"主链路注销请求消息");
		msgMap.put(0x1004,"主链路注销应答消息");
		msgMap.put(0x1005,"主链路连接保持请求消息");
		msgMap.put(0x1006,"主链路连接保持应答消息");
		msgMap.put(0x1007,"主链路断开通知消息");
		msgMap.put(0x1008,"下级平台主动关闭链路通知消息");
		msgMap.put(0x9001,"从链路连接请求消息");
		msgMap.put(0x9002,"从链路连接应答消息");
		msgMap.put(0x9003,"从链路注销请求消息");
		msgMap.put(0x9004,"从链路注销应答消息");
		msgMap.put(0x9005,"从链路连接保持请求消息");
		msgMap.put(0x9006,"从链路连接保持应答消息");
		msgMap.put(0x9007,"从链路断开通知消息");
		msgMap.put(0x9008,"上级平台主动关闭链路通知消息");
		msgMap.put(0x9101,"接收定位信息数量通知消息");
		msgMap.put(0x1200,"主链路动态信息交换消息");
		msgMap.put(0x9200,"从链路动态信息交换消息");
		msgMap.put(0x1300,"主链路平台间信息交互消息");
		msgMap.put(0x9300,"从链路平台间信息交互消息");
		msgMap.put(0x1400,"主链路报警信息交互消息");
		msgMap.put(0x9400,"从链路报警信息交互消息");
		msgMap.put(0x1500,"主链路车辆监管消息");
		msgMap.put(0x9500,"从链路车辆监管消息");
		msgMap.put(0x1600,"主链路静态信息交换消息");
		msgMap.put(0x9600,"从链路静态信息交换消息");


		msgMap.put(0x1201,"上传车辆注册信息");
		msgMap.put(0x1202,"事实上传车辆定位信息");
		msgMap.put(0x1203,"车辆定位信息自动补报");
		msgMap.put(0x1205,"启动车辆定位信息交换应答");
		msgMap.put(0x1206,"结束车辆定位信息交换应答");
		msgMap.put(0x1207,"申请交换指定车辆定");
		msgMap.put(0x1208,"取消交换指定车辆定位信息请求");
		msgMap.put(0x1209,"补发车辆定位信息请求");
		msgMap.put(0x120A,"上报车辆驾驶员身份识别信息应答");
		msgMap.put(0x120B,"上报车辆电子运单应答");
		msgMap.put(0x120C,"主动上报驾驶员身份信息");
		msgMap.put(0x120D,"主动上报车辆电子运单信息");

		msgMap.put(0x9202,"交换车辆定位信息");
		msgMap.put(0x9203,"车辆定位信息交换补发");
		msgMap.put(0x9204,"交换车辆静态信息");
		msgMap.put(0x9205,"启动车辆定位信息交换请求");
		msgMap.put(0x9206,"结束车辆定位信息交换请求");
		msgMap.put(0x9207,"申请交换指定车辆定位应答");
		msgMap.put(0x9208,"取消交换指定车辆定位应答");
		msgMap.put(0x9209,"补发车辆定位信息应答");
		msgMap.put(0x920A,"上报车辆驾驶员身份识别信息请求");
		msgMap.put(0x920B,"上报车辆电子运单请求");

		msgMap.put(0x1301,"平台查岗应答");
		msgMap.put(0x1302,"下发平台间报文应答");
		msgMap.put(0x9301,"平台查岗请求");
		msgMap.put(0x9302,"下发平台间报文请求");
		msgMap.put(0x1401,"报警督办应答");
		msgMap.put(0x1402,"上报报警信息");
		msgMap.put(0x1402,"主动上报报警处理结果信息");

		msgMap.put(0x9401,"报警督办请求");
		msgMap.put(0x9402,"报警预警");
		msgMap.put(0x9403,"实时交换报警消息");

		msgMap.put(0x1501,"车辆单向监听应答");
		msgMap.put(0x1502,"车辆拍照应答");
		msgMap.put(0x1503,"下发车辆报文应答");
		msgMap.put(0x1504,"上报车辆行驶记录应答");
		msgMap.put(0x1505,"车辆应急接入监管平台应答消息");

		msgMap.put(0x9501,"车辆单向监听请求");
		msgMap.put(0x9502,"车辆拍照请求");
		msgMap.put(0x9503,"下发车辆报文请求");
		msgMap.put(0x9504,"上报车辆行驶记录应答");

		msgMap.put(0x9505,"车辆应急接入监管平台请求消息");
		msgMap.put(0x1601,"补报车辆静态信息应答");
		msgMap.put(0x9601,"补报车辆静态信息请求");
	}
	
	
	
}

