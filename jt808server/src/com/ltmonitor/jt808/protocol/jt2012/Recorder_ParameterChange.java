package com.ltmonitor.jt808.protocol.jt2012;

import java.util.ArrayList;
import java.util.List;

/** 
 采集指定的参数修改记录
 
*/
public class Recorder_ParameterChange implements IRecorderDataBlock_2012
{
	private  List<RecorderEvent> eventList = new ArrayList<RecorderEvent>();

	 /** 
	 命令字
	 
	 */
	public final byte getCommandWord()
	{
		return 0x06;
	}

	/** 
	 数据块长度
	 
	*/
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: public ushort getDataLength()
	public final short getDataLength()
	{
		return 87;
	}

	public final byte[] WriteToBytes()
	{
		byte[] bytes = null;
		return bytes;
	}

	public final void ReadFromBytes(byte[] bytes)
	{
		StringBuilder sb = new StringBuilder();
		if (bytes != null)
		{
			for (int i = 0; i < bytes.length / 7; i++)
			{
				
				byte[] EventTime = new byte[6];
				System.arraycopy(bytes, 0 + 7 * i, EventTime, 0, 6);
				String strTime =  new java.util.Date(java.util.Date.parse("20" + String.format("%02X", EventTime[0]) + "-" + String.format("%02X", EventTime[1]) + "-" + String.format("%02X", EventTime[2]) + " " + String.format("%02X", EventTime[3]) + ":" + String.format("%02X", EventTime[4]) + ":" + String.format("%02X", EventTime[5]))).toString();
				String eventType = "";
				switch (bytes[6 + 7 * i])
				{
					case 0x00:
						eventType = "00H 采集记录仪执行标准版本";
						break;
					case 0x01:
						eventType = "01H 采集当前驾驶人信息";
						break;
					case 0x02:
						eventType = "O2H 采集记录仪的实时时间";
						break;
					case 0x03:
						eventType = "03H 采集累计行驶里程";
						break;
					case 0x04:
						eventType = "04H 采集记录仪脉冲系数";
						break;
					case 0x05:
						eventType = "05H 采集车辆信息";
						break;
					case 0x06:
						eventType = "06H 采集记录仪状态信号配置信息";
						break;
					case 0x07:
						eventType = "07H 采集记录仪唯一性编号";
						break;
					case 0x08:
						eventType = "08H 采集指定的行驶速度记录";
						break;
					case 0x09:
						eventType = "09H 采集指定的位置信息记录";
						break;
					case 0x10:
						eventType = "10H 采集指定的事故疑点记录";
						break;
					case 0x11:
						eventType = "11H 采集指定的超时驾驶记录";
						break;
					case 0x12:
						eventType = "12H 采集指定的驾驶人身份记录";
						break;
					case 0x13:
						eventType = "13H 采集指定的外部供电记录";
						break;
					case 0x14:
						eventType = "14H 采集指定的参数修改记录";
						break;
					case 0x15:
						eventType = "15H 采集指定的速度状态日志";
						break;
					case 0x16:
						eventType = "16H-1FH 预留";
						break;
					default:
						eventType = "其他";
						break;
				}
				this.eventList.add(new RecorderEvent(strTime, eventType));
			}
		}
	}

	public List<RecorderEvent> getEventList() {
		return eventList;
	}

	public void setEventList(List<RecorderEvent> eventList) {
		this.eventList = eventList;
	}
}

