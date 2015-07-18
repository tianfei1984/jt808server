package com.ltmonitor.jt808.protocol;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.ltmonitor.jt808.tool.DateUtil;

public class BitConverter {

	public static boolean IsLittleEndian = false;

	public static byte[] GetBytes(int num) {
		byte[] result = new byte[4];
		result[0] = (byte) (num >>> 24);// 取最高8位放到0下标
		result[1] = (byte) (num >>> 16);// 取次高8为放到1下标
		result[2] = (byte) (num >>> 8); // 取次低8位放到2下标
		result[3] = (byte) (num); // 取最低8位放到3下标
		return result;
	}

	public static int ToUInt16(byte[] bytes, int start) {
		int value = 0;
		int m = start+2;
		// 由高位到低位
		for (int i = start; i < m; i++) {
			int shift = (m - 1 - i) * 8;
			value += (bytes[i] & 0x000000FF) << shift;// 往高位游
		}
		return value;
	}

	public static int ToUInt32(byte[] bytes, int start) {
		int value = 0;
		int m = start+4;
		// 由高位到低位
		for (int i = start; i < m; i++) {
			int shift = (m - 1 - i) * 8;
			value += (bytes[i] & 0x000000FF) << shift;// 往高位游
		}
		return value;
	}

	public static int ToUInt32(byte b) {
		int value = 0;
		int m = 4;
		// 由高位到低位
		int shift = (m - 1 - 3) * 8;
		value += (b & 0x000000FF) << shift;// 往高位游
		return value;
	}

	public static byte[] getBytes(String str) {
		try {
			return str.getBytes("gbk");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将808协议中的自己数组，转换成日期,这个转换只适用于808协议的规定
	 * 
	 * @param bytes
	 * @param start
	 * @return
	 */
	public static Date getDate(byte[] bytes, int start) {
		StringBuilder sb = new StringBuilder();
		sb.append("20").append(String.format("%02X", bytes[start + 0]))
				.append("-").append(String.format("%02X", bytes[start + 1]))
				.append("-").append(String.format("%02X", bytes[start + 2]))
				.append(" ").append(String.format("%02X", bytes[start + 3]))
				.append(":").append(String.format("%02X", bytes[start + 4]))
				.append(":").append(String.format("%02X", bytes[start + 5]));
		String strDate = sb.toString();
		Date d = DateUtil.stringToDatetime(strDate, "yyyy-MM-dd HH:mm:ss");
		return d;
	}

	public static String getString(byte[] data) {
		return getString(data, 0, data.length);
	}

	public static String getString(byte[] data, int start, int len) {
		try {
			return new String(data, start, len, "gbk");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] getBytes(Date date) {
		int start = 0;
		byte[] bytes = new byte[6];
		Calendar cal = Calendar.getInstance();// 使用日历类
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);// 得到年
		int month = cal.get(Calendar.MONTH) + 1;// 得到月，因为从0开始的，所以要加1
		int day = cal.get(Calendar.DAY_OF_MONTH);// 得到天
		int hour = cal.get(Calendar.HOUR_OF_DAY);// 得到小时
		int minute = cal.get(Calendar.MINUTE);// 得到分钟
		int second = cal.get(Calendar.SECOND);// 得到秒
		bytes[start++] = Byte.parseByte("" + (year - 2000), 16);
		bytes[start++] = Byte.parseByte((month + ""), 16);
		bytes[start++] = Byte.parseByte((day + ""), 16);
		bytes[start++] = Byte.parseByte((hour + ""), 16);
		bytes[start++] = Byte.parseByte((minute + ""), 16);
		bytes[start++] = Byte.parseByte((second + ""), 16);
		return bytes;
	}

	public static String format(Date date) {

		SimpleDateFormat time = new SimpleDateFormat("yy-MM-dd HH:mm:s");
		return time.format(date);
	}

	public int getUnsignedByte(byte data) { // 将data字节型数据转换为0~255 (0xFF 即BYTE)。
		return data & 0x0FF;
	}

	public int getUnsignedByte(short data) { // 将data字节型数据转换为0~65535 (0xFFFF 即
												// WORD)。
		return data & 0x0FFFF;
	}

	public long getUnsignedIntt(int data) { // 将int数据转换为0~4294967295
											// (0xFFFFFFFF即DWORD)。
		return data & 0x0FFFFFFFFl;
	}

	public static void main(String[] args) {
		byte b = (byte) 130;
		int x = ToUInt32(b);
		System.out.println(x);

		System.out.println((byte) 254);
	}

}
