package com.ltmonitor.jt808.protocol.jt2012;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.ltmonitor.entity.StringUtil;
import com.ltmonitor.entity.VehicleRecorder;
import com.ltmonitor.jt808.entity.SpeedRecorder;
import com.ltmonitor.jt808.protocol.BitConverter;
import com.ltmonitor.jt808.tool.DateUtil;

/**
 * 采集指定的事故疑点记录 10H
 */
public class Recorder_AccidentRecordsOfDoubt implements IRecorderDataBlock_2012 {

	//记录仪数据
	private List<VehicleRecorder> vehicleRecorderList = new ArrayList<VehicleRecorder>();

	/**
	 * 命令字
	 */
	public final byte getCommandWord() {
		return 0x10;
	}

	/**
	 * 数据块长度
	 */
	public final short getDataLength() {
		return 87;
	}

	public final byte[] WriteToBytes() {
		byte[] bytes = null;
		return bytes;
	}

	public final void ReadFromBytes(byte[] bytes) {
		//StringBuilder sb = new StringBuilder();
		if (bytes != null) {
			for (int i = 0; i < bytes.length / 234; i++) {
				VehicleRecorder vr = new VehicleRecorder();
				byte[] questionable = new byte[234];
				System.arraycopy(bytes, 0 + 234 * i, questionable, 0, 234);
				byte[] time = new byte[6];
				System.arraycopy(questionable, 0 + 234 * i, time, 0, 6);
				java.util.Date beginTime = BitConverter.getDate(time, 0);
				vr.setStartTime(beginTime);
				
				byte[] nub = new byte[18];
				System.arraycopy(questionable, 6 + 234 * i, nub, 0, 18);
				//机动车驾驶证号码
				String driverLicense = BitConverter.getString(nub);
				vr.setDriverLicense(driverLicense);
				if (driverLicense.length() == 15) {
					String add = "00H";
					driverLicense = driverLicense + add;
				}
				
				//100组事故疑点记录
				for (int j = 0; j < 100; j++) {
					SpeedRecorder sr = new SpeedRecorder();
					//行驶结束时速度
					int s = BitConverter.ToUInt32(questionable[24 + 2 * j]);
					String speed = "" + s;
					if (speed.length() < 3) {
						speed = StringUtil.leftPad(speed, 3, '0');
					}
					sr.setSpeed(s);
					//行驶结束时的状态信号 
					int signal = questionable[25 + 2 * j];
					sr.setSignalState(signal);

					String State = "";

					if ((signal & 0x80) == 0x80) {
						State = "1";
					} else {
						State = "0";
					}
					if ((signal & 0x40) == 0x40) {
						State += "1";
					} else {
						State += "0";
					}
					if ((signal & 0x20) == 0x20) {
						State += "1";
					} else {
						State += "0";
					}
					if ((signal & 0x10) == 0x10) {
						State += "1";
					} else {
						State += "0";
					}
					if ((signal & 8) == 8) {
						State += "1";
					} else {
						State += "0";
					}
					if ((signal & 4) == 4) {
						State += "1";
					} else {
						State += "0";
					}
					if ((signal & 2) == 2) {
						State += "1";
					} else {
						State += "0";
					}
					if ((signal & 0) == 0) {
						State += "1";
					} else {
						State += "0";
					}

					int milisecond = (int)(1000 * (0.2 + 0.2 * j));
					Date takeTime = DateUtil.getDate(beginTime, Calendar.MILLISECOND, milisecond);
					sr.setRecorderDate(takeTime);
					
					vr.getSpeedList().add(sr);
					//getOneQuestionableInfo().put(takeTime, speed + State);
				}
				//行驶结束前最后一次有效位置信息
				byte[] placeInfo = new byte[10];
				System.arraycopy(questionable, 224, placeInfo, 0, 10);
				int start = 224;
				int longitude = BitConverter.ToUInt32(questionable, start);
				int latitude = BitConverter.ToUInt32(questionable, start+4);
				int altitude = BitConverter.ToUInt16(questionable, start+8);
				vr.setLatitude(latitude);
				vr.setLongitude(longitude);
				vr.setAltitude(altitude);
				
				vehicleRecorderList.add(vr);
			}

		}
	}

	public List<VehicleRecorder> getVehicleRecorderList() {
		return vehicleRecorderList;
	}
}
