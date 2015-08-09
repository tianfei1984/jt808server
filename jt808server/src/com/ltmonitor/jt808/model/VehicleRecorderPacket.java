package com.ltmonitor.jt808.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ltmonitor.jt808.protocol.JT_0200;
import com.ltmonitor.jt808.protocol.JT_0700;
import com.ltmonitor.jt808.protocol.JT_0801;
import com.ltmonitor.jt808.protocol.T808Message;
import com.ltmonitor.jt808.protocol.T808MessageHeader;
import com.ltmonitor.util.DateUtil;
/**
 * 行车记录仪包装类
 * @author tianfei
 *
 */
public class VehicleRecorderPacket {
	//包的唯一key
	private String key;
	
	private int totalNum;//包数据 
	
	private int dataLength = 0;//数据量
	
	private Date updateDate = new Date();
	/**
	 * 分包和分包号的映射关系
	 */
	private Map<Integer, byte[]> packets = new HashMap<Integer, byte[]>();
	
	private T808Message t808Message;
	
	/**
	 * 重传次数
	 */
	private int retransCount;
	
	private Date createDate = new Date();
	
	public VehicleRecorderPacket(T808Message msg)
	{
		T808MessageHeader header = msg.getHeader();
		this.totalNum = header.getMessageTotalPacketsCount();
		dataLength += header.getMessageSize();//消息体长度
		// 如果是第一个分包就正常解析;
		JT_0700 data = (JT_0700) msg.getMessageContents();
		packets.put((int)header.getMessagePacketNo(), data.getCmdData());
		this.t808Message = msg;
	}
	/**
	 * 是否接收到所有的分包
	 * @return
	 */
	public boolean isComplete()
	{
		return packets.size() == this.totalNum; 
	}
	
	public byte[] getWholePacket()
	{
		if(isComplete() == false && dataLength  == 0)
			return null;
		byte[] bytes = new byte[dataLength];
		int pos = 0;
		for(int m = 1; m <= this.totalNum; m++)
		{
			byte[] data = packets.get(m);
			System.arraycopy(data, 0, bytes, pos, data.length);
			pos+=data.length;
		}
		return bytes;
	}
	/**
	 * 得到需要重传的分包序号
	 * @return
	 */
	public ArrayList<Short> getNeedReTransPacketNo()
	{
		ArrayList<Short> result = new ArrayList<Short>();
		if(this.totalNum > this.packets.size())
		{
			for(int m = 1; m <= this.totalNum;m++)
			{
				if(packets.containsKey(m) == false)
					result.add((short)m);
			}
		}
		return result;
	}
	
	public boolean containPacket(int packetNo)
	{
		return packets.containsKey(packetNo);
	}
	
	public void addPacket(int packetNo, byte[] packetData)
	{
		packets.put(packetNo, packetData);
		this.updateDate = new Date();//更新上传时间
		dataLength += packetData.length;
	}
	//得到重传的包号
	public List<Integer> getNeedReUploadPacketNo()
	{
		List<Integer> result = new ArrayList<Integer>();
		for(int m = 1; m <= totalNum; m++)
		{
			if(packets.containsKey(m) == false)
				result.add(m);
		}
		return result;
	}
	
	public String toString()
	{
		double seconds = DateUtil.getSeconds(createDate, updateDate);
		StringBuilder sb = new StringBuilder();
		sb.append(this.t808Message.getPlateNo()).append(this.t808Message.getSimNo())
		.append(",总包数:").append(this.totalNum)
		.append(",收到包数:").append(this.packets.size())
		.append("重传数:").append(this.retransCount)
		.append("耗时:").append(seconds).append("秒");
		return sb.toString();
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Map<Integer, byte[]> getPackets() {
		return packets;
	}

	public void setPackets(Map<Integer, byte[]> packets) {
		this.packets = packets;
	}
	public T808Message getT808Message() {
		return t808Message;
	}
	public void setT808Message(T808Message t808Message) {
		this.t808Message = t808Message;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public int getRetransCount() {
		return retransCount;
	}
	public void setRetransCount(int retransCount) {
		this.retransCount = retransCount;
	}
}
