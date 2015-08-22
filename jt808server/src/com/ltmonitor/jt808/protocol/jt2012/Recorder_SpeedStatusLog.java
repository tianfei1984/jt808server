package com.ltmonitor.jt808.protocol.jt2012;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.ltmonitor.entity.VehicleRecorder;
import com.ltmonitor.jt808.entity.SpeedRecorder;
import com.ltmonitor.jt808.tool.DateUtil;

/**
 * 采集指定的速度状态日志 0x15H
 */
public class Recorder_SpeedStatusLog implements IRecorderDataBlock_2012 {
	
	private List<VehicleRecorder> vehicleRecorders = new ArrayList<VehicleRecorder>();

	/**
	 * 命令字
	 */
	public final byte getCommandWord() {
		return 0x15;
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
		String info = null;
		if (bytes != null) {
			for (int i = 0; i < bytes.length / 133; i++) {
				VehicleRecorder vr = new VehicleRecorder();
				switch (bytes[0]) {
				case 0x01:
					info = "正常 ";
					break;
				case 0x02:
					info = "异常 ";
					break;
				default:
					info = "不正确";
					break;
				}
				vr.setRemark(info);

				byte[] StateEstimationBeginTime = new byte[6];
				System.arraycopy(bytes, 1, StateEstimationBeginTime, 0, 6);
				Date beginTime = new Date(
						java.util.Date.parse("20"
								+ String.format("%02X",
										StateEstimationBeginTime[0])
								+ "-"
								+ String.format("%02X",
										StateEstimationBeginTime[1])
								+ "-"
								+ String.format("%02X",
										StateEstimationBeginTime[2])
								+ " "
								+ String.format("%02X",
										StateEstimationBeginTime[3])
								+ ":"
								+ String.format("%02X",
										StateEstimationBeginTime[4])
								+ ":"
								+ String.format("%02X",
										StateEstimationBeginTime[5])));
				vr.setStartTime(beginTime);
				byte[] StateEstimationEndTime = new byte[6];
				System.arraycopy(bytes, 7, StateEstimationEndTime, 0, 6);
				Date endTime = new Date(Date.parse("20" + String.format("%02X",
										StateEstimationEndTime[0])
								+ "-"
								+ String.format("%02X",
										StateEstimationEndTime[1])
								+ "-"
								+ String.format("%02X",
										StateEstimationEndTime[2])
								+ " "
								+ String.format("%02X",
										StateEstimationEndTime[3])
								+ ":"
								+ String.format("%02X",
										StateEstimationEndTime[4])
								+ ":"
								+ String.format("%02X",
										StateEstimationEndTime[5])));
				vr.setEndTime(endTime);
				//开始时间对应的速度
				
				//开始时间对应的参考速度
				SpeedRecorder sr = null;
				for (int j = 0; j < 60; j++) {
					sr = new SpeedRecorder();
					sr.setSpeed(bytes[15+1*j]);
					sr.setRefrenceSpeed(bytes[16+1*j]);
					Date takeTime = DateUtil.getDate(beginTime, Calendar.SECOND, 1);
					sr.setRecorderDate(takeTime);
					vr.getSpeedList().add(sr);
				}
				vehicleRecorders.add(vr);
			}
		}
	}

	public List<VehicleRecorder> getVehicleRecorders() {
		return vehicleRecorders;
	}

	public void setVehicleRecorders(List<VehicleRecorder> vehicleRecorders) {
		this.vehicleRecorders = vehicleRecorders;
	}
	
}
