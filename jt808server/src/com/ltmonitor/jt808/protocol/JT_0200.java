package com.ltmonitor.jt808.protocol;

import org.apache.log4j.Logger;

import com.ltmonitor.entity.StringUtil;
import com.ltmonitor.jt808.service.impl.GpsDataService;

/**
 * 位置信息汇报
 */
public class JT_0200 implements IMessageBody {

	private static Logger logger = Logger.getLogger(JT_0200.class);
	/**
	 * 报警标志
	 */

	private int alarmFlag;

	public final int getAlarmFlag() {
		return alarmFlag;
	}

	public final void setAlarmFlag(int value) {
		alarmFlag = value;
	}

	/**
	 * 状态
	 */

	private int status;

	public final int getStatus() {
		return status;
	}

	public final void setStatus(int value) {
		status = value;
	}

	/**
	 * 纬度,以度为单位的纬度值乘以10的6次方，精确到百万分之一度
	 */

	private int latitude;

	public final int getLatitude() {
		return latitude;
	}

	public final void setLatitude(int value) {
		latitude = value;
	}

	/**
	 * 经度,以度为单位的经度值乘以10的6次方，精确到百万 分之一度
	 */

	private int longitude;

	public final int getLongitude() {
		return longitude;
	}

	public final void setLongitude(int value) {
		longitude = value;
	}

	/**
	 * 海拔高度，单位为米（m）
	 */

	private short altitude;

	public final short getAltitude() {
		return altitude;
	}

	public final void setAltitude(short value) {
		altitude = value;
	}

	/**
	 * 速度,1/10km/h
	 */

	// ORIGINAL LINE: private ushort speed;
	private short speed;

	// ORIGINAL LINE: public ushort getSpeed()
	public final short getSpeed() {
		return speed;
	}

	// ORIGINAL LINE: public void setSpeed(ushort value)
	public final void setSpeed(short value) {
		speed = value;
	}

	/**
	 * 方向,0～359，正北为0，顺时针
	 */

	// ORIGINAL LINE: private ushort course;
	private short course;

	// ORIGINAL LINE: public ushort getCourse()
	public final short getCourse() {
		return course;
	}

	// ORIGINAL LINE: public void setCourse(ushort value)
	public final void setCourse(short value) {
		course = value;
	}

	/**
	 * 时间,YY-MM-DD-hh-mm-ss（GMT+8时间，本标准中之后涉及的时间均采用此时区）
	 */
	private String time;

	public final String getTime() {
		return time;
	}

	public final void setTime(String value) {
		time = value;
	}

	private boolean valid;

	public final boolean isValid() {
		return valid;
	}

	public final void setIsValid(boolean value) {
		valid = value;
	}

	private String strStatus;

	public final String getStrStatus() {
		return strStatus;
	}

	public final void setStrStatus(String value) {
		strStatus = value;
	}

	private String strWarn;

	public final String getStrWarn() {
		return strWarn;
	}

	public final void setStrWarn(String value) {
		strWarn = value;
	}

	/**
	 * 里程
	 */
	public final int getMileage() {
		IPositionAdditionalItem addtionalItem = null;

		for (IPositionAdditionalItem item : getAdditionals()) {
			if (item.getAdditionalId() == 0x01) {
				addtionalItem = item;
				break;
			}

		}

		if (addtionalItem != null) {
			PostitionAdditional_Mileage mileage = (PostitionAdditional_Mileage) addtionalItem;
			return mileage.getMileage();
		} else {
			return 0;
		}
	}
	
	/**
	 * 附加扩展信号状态位
	 */
	public final int getSignal() {
		IPositionAdditionalItem addtionalItem = null;

		for (IPositionAdditionalItem item : getAdditionals()) {
			if (item.getAdditionalId() == 0x25) {
				addtionalItem = item;
				break;
			}

		}

		if (addtionalItem != null) {
			PostitionAdditional_Signal item = (PostitionAdditional_Signal) addtionalItem;
			return item.getSignal();
		} else {
			return 0;
		}
	}

	/**
	 * 油量
	 */

	// ORIGINAL LINE: public ushort getOil()
	public final short getOil() {

		IPositionAdditionalItem addtionalItem = null;
		for (IPositionAdditionalItem item : getAdditionals()) {
			if (item.getAdditionalId() == 0x02) {
				addtionalItem = item;
				break;
			}

		}
		if (addtionalItem != null) {
			PostitionAdditional_Oil oil = (PostitionAdditional_Oil) addtionalItem;
			return oil.getOil();
		} else {
			return 0;
		}
	}

	/**
	 * 进出区域报警附加信息
	 */
	public final PostitionAdditional_InOutAreaAlarmAdditional getInOutAreaAlarmAdditional() {

		IPositionAdditionalItem addtionalItem = null;
		for (IPositionAdditionalItem item : getAdditionals()) {
			if (item.getAdditionalId() == 0x12) {
				addtionalItem = item;
				break;
			}

		}
		if (addtionalItem != null) {
			PostitionAdditional_InOutAreaAlarmAdditional additional = (PostitionAdditional_InOutAreaAlarmAdditional) addtionalItem;
			return additional;
		}
		return null;
	}
	

	/**
	 * 行驶时间过长或过短报警附加信息
	 */
	public final PostitionAdditional_RouteDriveTimeAlarmAdditional getRouteTimeAlarmAdditional() {

		IPositionAdditionalItem addtionalItem = null;
		for (IPositionAdditionalItem item : getAdditionals()) {
			if (item.getAdditionalId() == 0x13) {
				addtionalItem = item;
				break;
			}

		}
		if (addtionalItem != null) {
			PostitionAdditional_RouteDriveTimeAlarmAdditional additional = (PostitionAdditional_RouteDriveTimeAlarmAdditional) addtionalItem;
			return additional;
		}
		return null;
	}
	

	/**
	 * 行驶时间过长或过短报警附加信息
	 */
	public final PostitionAdditional_OverSpeedAlarmAdditional getOverSpeedAlarmAdditional() {

		IPositionAdditionalItem addtionalItem = null;
		for (IPositionAdditionalItem item : getAdditionals()) {
			if (item.getAdditionalId() == 0x11) {
				addtionalItem = item;
				break;
			}

		}
		if (addtionalItem != null) {
			PostitionAdditional_OverSpeedAlarmAdditional additional = (PostitionAdditional_OverSpeedAlarmAdditional) addtionalItem;
			return additional;
		}
		return null;
	}
	/**
	 * 电压
	 */

	// ORIGINAL LINE: public ushort getVoltage()
	public final short getVoltage() {

		IPositionAdditionalItem addtionalItem = null;
		for (IPositionAdditionalItem item : getAdditionals()) {
			if (item.getAdditionalId() == 0xE1) {
				addtionalItem = item;
				break;
			}

		}
		if (addtionalItem != null) {
			PostitionAdditional_Voltage voltage = (PostitionAdditional_Voltage) addtionalItem;
			return voltage.getVoltage();
		} else {
			return 0;
		}
	}

	/**
	 * 记录仪速度
	 */

	// ORIGINAL LINE: public ushort getRecorderSpeed()
	public final short getRecorderSpeed() {
		IPositionAdditionalItem addtionalItem = null;
		for (IPositionAdditionalItem item : getAdditionals()) {
			if (item.getAdditionalId() == 0x03) {
				addtionalItem = item;
				break;
			}

		}
		if (addtionalItem != null) {
			PostitionAdditional_RecorderSpeed recorrderSpeed = (PostitionAdditional_RecorderSpeed) addtionalItem;
			return recorrderSpeed.getRecorderSpeed();
		} else {
			return 0;
		}
	}

	private java.util.ArrayList<IPositionAdditionalItem> additionals;

	public final java.util.ArrayList<IPositionAdditionalItem> getAdditionals() {
		return additionals;
	}

	public final void setAdditionals(
			java.util.ArrayList<IPositionAdditionalItem> value) {
		additionals = value;
	}

	public final byte[] WriteToBytes() {

		MyBuffer buff = new MyBuffer();

		buff.put(getAlarmFlag());
		buff.put(getStatus());
		buff.put(getLatitude());
		buff.put(getLongitude());
		buff.put(getAltitude());
		buff.put(getSpeed());
		buff.put(getCourse());
		buff.put(Byte.parseByte(getTime().substring(2, 4), 16));
		buff.put(Byte.parseByte(getTime().substring(5, 7), 16));
		buff.put(Byte.parseByte(getTime().substring(8, 10), 16));
		buff.put(Byte.parseByte(getTime().substring(11, 13), 16));
		buff.put(Byte.parseByte(getTime().substring(14, 16), 16));
		buff.put(Byte.parseByte(getTime().substring(17, 19), 16));
		if (getAdditionals() != null && getAdditionals().size() > 0) {
			for (IPositionAdditionalItem ad : getAdditionals()) {
				buff.put(ad.getAdditionalId());
				buff.put(ad.getAdditionalLength());
				buff.put(ad.WriteToBytes());
			}
		}

		return buff.array();
	}

	public final void ReadFromBytes(byte[] bytes) {

		MyBuffer buff = new MyBuffer(bytes);

		setAlarmFlag(buff.getLong());
		setStatus(buff.getLong());
		setLatitude(buff.getLong());
		setLongitude(buff.getLong());
		setAltitude(buff.getShort());
		setSpeed(buff.getShort());
		setCourse(buff.getShort());
		byte[] timeBytes = buff.gets(6);
		setTime("20" + String.format("%02X", timeBytes[0]) + "-"
				+ String.format("%02X", timeBytes[1]) + "-"
				+ String.format("%02X", timeBytes[2]) + " "
				+ String.format("%02X", timeBytes[3]) + ":"
				+ String.format("%02X", timeBytes[4]) + ":"
				+ String.format("%02X", timeBytes[5]));
		setAdditionals(new java.util.ArrayList<IPositionAdditionalItem>());
		int pos = 28;
		while (buff.hasRemain()) {
			byte additionalId = buff.get();
			byte additionalLength = buff.get();
			int x = bytes.length;
			byte[] additionalBytes = buff.gets(additionalLength);
			IPositionAdditionalItem item = PositionAdditionalFactory
					.CreatePositionalFactory(additionalId, additionalLength,
							additionalBytes);
			if (item != null) {
				getAdditionals().add(item);
			} else {
				logger.info("未知的附加协议:" + additionalId + ",附加长度:"
						+ additionalLength);
			}
			pos = pos + 2 + additionalLength;
		}

		this.strWarn = Integer.toBinaryString(this.getAlarmFlag());
		strWarn = StringUtil.leftPad(strWarn, 32, '0');

		this.strStatus = Integer.toBinaryString(this.getStatus());
		strStatus = StringUtil.leftPad(strStatus, 32, '0');
		// strStatus = String.format("%0$"+32+"s", strStatus);
		setIsValid(getStrStatus().substring(30, 31).equals("1"));
	}

	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(String.format(
				"报警标示：%1$s,状态：%2$s,经度：%3$s,纬度：%4$s,高度：%5$s,速度：%6$s,时间：%7$s",
				getAlarmFlag(), getStatus(), getLongitude(), getLatitude(),
				getAltitude(), getSpeed(), getTime()));
		return sBuilder.toString();
	}
}