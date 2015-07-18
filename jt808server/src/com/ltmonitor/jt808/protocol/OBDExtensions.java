package com.ltmonitor.jt808.protocol;

/**
 * OBD扩展数据
 */
public class OBDExtensions implements IPositionAdditionalItem {
	/**
	 * 负荷计算值 单位：%
	 */
	private byte oBDOverloadCalculate;

	public final byte getOBDOverloadCalculate() {
		return oBDOverloadCalculate;
	}

	public final void setOBDOverloadCalculate(byte value) {
		oBDOverloadCalculate = value;
	}

	/**
	 * 冷却液温度 单位：℃ (A-40）
	 */
	private byte oBDCoolantTemperature;

	public final byte getOBDCoolantTemperature() {
		return oBDCoolantTemperature;
	}

	public final void setOBDCoolantTemperature(byte value) {
		oBDCoolantTemperature = value;
	}

	/**
	 * OBD车速 单位：km/h
	 */
	private byte oBDSpeed;

	public final byte getOBDSpeed() {
		return oBDSpeed;
	}

	public final void setOBDSpeed(byte value) {
		oBDSpeed = value;
	}

	/**
	 * 发动机转速 单位：rpm
	 */

	// ORIGINAL LINE: private ushort oBDEngineTurnRate;
	private short oBDEngineTurnRate;

	// ORIGINAL LINE: public ushort getOBDEngineTurnRate()
	public final short getOBDEngineTurnRate() {
		return oBDEngineTurnRate;
	}

	// ORIGINAL LINE: public void setOBDEngineTurnRate(ushort value)
	public final void setOBDEngineTurnRate(short value) {
		oBDEngineTurnRate = value;
	}

	/**
	 * OBD里程 单位：Km
	 */

	// ORIGINAL LINE: private uint oBDMileage;
	private int oBDMileage;

	// ORIGINAL LINE: public uint getOBDMileage()
	public final int getOBDMileage() {
		return oBDMileage;
	}

	// ORIGINAL LINE: public void setOBDMileage(uint value)
	public final void setOBDMileage(int value) {
		oBDMileage = value;
	}

	/**
	 * 进气温度 单位：℃ (A-40)
	 */
	private byte oBDInletTemperature;

	public final byte getOBDInletTemperature() {
		return oBDInletTemperature;
	}

	public final void setOBDInletTemperature(byte value) {
		oBDInletTemperature = value;
	}

	/**
	 * 空气流量 单位：g/s
	 */

	// ORIGINAL LINE: private ushort oBDAirMassFlow;
	private short oBDAirMassFlow;

	// ORIGINAL LINE: public ushort getOBDAirMassFlow()
	public final short getOBDAirMassFlow() {
		return oBDAirMassFlow;
	}

	// ORIGINAL LINE: public void setOBDAirMassFlow(ushort value)
	public final void setOBDAirMassFlow(short value) {
		oBDAirMassFlow = value;
	}

	/**
	 * 节气门绝对位置 单位：%
	 */
	private byte oBDThrottlePosition;

	public final byte getOBDThrottlePosition() {
		return oBDThrottlePosition;
	}

	public final void setOBDThrottlePosition(byte value) {
		oBDThrottlePosition = value;
	}

	/**
	 * 控制模块电压 单位：V
	 */
	private byte oBDControlVoltage;

	public final byte getOBDControlVoltage() {
		return oBDControlVoltage;
	}

	public final void setOBDControlVoltage(byte value) {
		oBDControlVoltage = value;
	}

	/**
	 * 环境温度 单位：℃ (A-40）
	 */
	private byte oBDAmbientTemperature;

	public final byte getOBDAmbientTemperature() {
		return oBDAmbientTemperature;
	}

	public final void setOBDAmbientTemperature(byte value) {
		oBDAmbientTemperature = value;
	}

	/**
	 * 长期燃油修正 单位：% (A-128)*100/128
	 */
	private byte oBDLongTermFuelCorrection;

	public final byte getOBDLongTermFuelCorrection() {
		return oBDLongTermFuelCorrection;
	}

	public final void setOBDLongTermFuelCorrection(byte value) {
		oBDLongTermFuelCorrection = value;
	}

	/**
	 * 汽缸1点火提前角 单位：°
	 */
	private byte oBDCylinder1FireAngle;

	public final byte getOBDCylinder1FireAngle() {
		return oBDCylinder1FireAngle;
	}

	public final void setOBDCylinder1FireAngle(byte value) {
		oBDCylinder1FireAngle = value;
	}

	/**
	 * 进气歧管绝对压力 单位：kpa
	 */
	private byte oBDInletBranchPressure;

	public final byte getOBDInletBranchPressure() {
		return oBDInletBranchPressure;
	}

	public final void setOBDInletBranchPressure(byte value) {
		oBDInletBranchPressure = value;
	}

	/**
	 * 本车OBD标准
	 */
	private byte oBDStandard;

	public final byte getOBDStandard() {
		return oBDStandard;
	}

	public final void setOBDStandard(byte value) {
		oBDStandard = value;
	}

	/**
	 * 每小时油耗 单位：L/H
	 */
	private byte oBDGasolineConsumptionPerHour;

	public final byte getOBDGasolineConsumptionPerHour() {
		return oBDGasolineConsumptionPerHour;
	}

	public final void setOBDGasolineConsumptionPerHour(byte value) {
		oBDGasolineConsumptionPerHour = value;
	}

	/**
	 * 100km油耗 单位：L/100Km (A/10)
	 */
	private byte oBDGasolineConsumptionPerHunKm;

	public final byte getOBDGasolineConsumptionPerHunKm() {
		return oBDGasolineConsumptionPerHunKm;
	}

	public final void setOBDGasolineConsumptionPerHunKm(byte value) {
		oBDGasolineConsumptionPerHunKm = value;
	}

	/**
	 * 油量
	 */

	// ORIGINAL LINE: private uint oBDOilValue;
	private int oBDOilValue;

	// ORIGINAL LINE: public uint getOBDOilValue()
	public final int getOBDOilValue() {
		return oBDOilValue;
	}

	// ORIGINAL LINE: public void setOBDOilValue(uint value)
	public final void setOBDOilValue(int value) {
		oBDOilValue = value;
	}

	public final int getAdditionalId() {
		return  0xE2;
	}

	public final byte getAdditionalLength() {
		return 25;
	}

	public final byte[] WriteToBytes() {
		if (BitConverter.IsLittleEndian) {
			byte[] bytes = new byte[25];
			bytes[0] = getOBDOverloadCalculate();
			bytes[1] = getOBDCoolantTemperature();
			bytes[2] = getOBDSpeed();
			bytes[3] = (byte) (getOBDEngineTurnRate() >> 8);
			bytes[4] = (byte) getOBDEngineTurnRate();
			bytes[5] = (byte) (getOBDMileage() >> 24);
			bytes[6] = (byte) (getOBDMileage() >> 16);
			bytes[7] = (byte) (getOBDMileage() >> 8);
			bytes[8] = (byte) getOBDMileage();
			bytes[9] = getOBDInletTemperature();
			bytes[10] = (byte) (getOBDAirMassFlow() >> 8);
			bytes[11] = (byte) getOBDAirMassFlow();
			bytes[12] = getOBDThrottlePosition();
			bytes[13] = getOBDControlVoltage();
			bytes[14] = getOBDAmbientTemperature();
			bytes[15] = getOBDLongTermFuelCorrection();
			bytes[16] = getOBDCylinder1FireAngle();
			bytes[17] = getOBDInletBranchPressure();
			bytes[18] = getOBDStandard();
			bytes[19] = getOBDGasolineConsumptionPerHour();
			bytes[20] = getOBDGasolineConsumptionPerHunKm();
			bytes[21] = (byte) (getOBDOilValue() >> 24);
			bytes[22] = (byte) (getOBDOilValue() >> 16);
			bytes[23] = (byte) (getOBDOilValue() >> 8);
			bytes[24] = (byte) (getOBDOilValue());
			return bytes;
		} else {
			// C# TO JAVA CONVERTER NOTE: The following 'using' block is
			// replaced by its Java equivalent:
			// using (MemoryStream ms = new MemoryStream())

			try {
				// C# TO JAVA CONVERTER NOTE: The following 'using' block is
				// replaced by its Java equivalent:
				// using (MyBuffer buff = new MyBuffer(bytes))
				MyBuffer buff = new MyBuffer();
				try {
					buff.put(getOBDOverloadCalculate());
					buff.put(getOBDCoolantTemperature());
					buff.put(getOBDSpeed());
					buff.put(getOBDEngineTurnRate());
					buff.put(getOBDMileage());
					buff.put(getOBDInletTemperature());
					buff.put(getOBDAirMassFlow());
					buff.put(getOBDThrottlePosition());
					buff.put(getOBDControlVoltage());
					buff.put(getOBDAmbientTemperature());
					buff.put(getOBDLongTermFuelCorrection());
					buff.put(getOBDCylinder1FireAngle());
					buff.put(getOBDInletBranchPressure());
					buff.put(getOBDStandard());
					buff.put(getOBDGasolineConsumptionPerHour());
					buff.put(getOBDGasolineConsumptionPerHunKm());
					buff.put(getOBDOilValue());
				} finally {

				}
				return buff.array();
			} finally {

			}
		}
	}

	public final void ReadFromBytes(byte[] bytes) {
		if (BitConverter.IsLittleEndian) {
			setOBDOverloadCalculate(bytes[0]);
			setOBDCoolantTemperature(bytes[1]);
			setOBDSpeed(bytes[2]);
			setOBDEngineTurnRate((short) ((bytes[3] << 8) + bytes[4]));
			setOBDMileage((int) ((bytes[5] << 24) + (bytes[6] << 16)
					+ (bytes[7] << 8) + bytes[8]));
			setOBDInletTemperature(bytes[9]);
			setOBDAirMassFlow((short) ((bytes[10] << 8) + bytes[11]));
			setOBDThrottlePosition(bytes[12]);
			setOBDControlVoltage(bytes[13]);
			setOBDAmbientTemperature(bytes[14]);
			setOBDLongTermFuelCorrection(bytes[15]);
			setOBDCylinder1FireAngle(bytes[16]);
			setOBDInletBranchPressure(bytes[17]);
			setOBDStandard(bytes[18]);
			setOBDGasolineConsumptionPerHour(bytes[19]);
			setOBDGasolineConsumptionPerHunKm(bytes[20]);
			setOBDOilValue((int) ((bytes[21] << 24) + (bytes[22] << 16)
					+ (bytes[23] << 8) + bytes[24]));
		} else {
			MyBuffer buff = new MyBuffer(bytes);

			setOBDOverloadCalculate(buff.get());
			setOBDCoolantTemperature(buff.get());
			setOBDSpeed(buff.get());
			setOBDEngineTurnRate(buff.getShort());
			setOBDMileage(buff.getLong());
			setOBDInletTemperature(buff.get());
			setOBDAirMassFlow(buff.getShort());
			setOBDThrottlePosition(buff.get());
			setOBDControlVoltage(buff.get());
			setOBDAmbientTemperature(buff.get());
			setOBDLongTermFuelCorrection(buff.get());
			setOBDCylinder1FireAngle(buff.get());
			setOBDInletBranchPressure(buff.get());
			setOBDStandard(buff.get());
			setOBDGasolineConsumptionPerHour(buff.get());
			setOBDGasolineConsumptionPerHunKm(buff.get());
			setOBDOilValue(buff.getLong());

		}
	}
}