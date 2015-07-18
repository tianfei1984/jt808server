package com.ltmonitor.jt808.tool;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.codec.binary.Base64;

import com.ltmonitor.jt808.model.Parameter;


public class Tools {
	public static long sendCount = 0L;
	/** 
     *  
     * 创建日期2011-4-25上午10:12:38 
     * 修改日期 
     * 作者：dh *TODO 使用Base64加密算法加密字符串 
     *return 
     */  
    public static String encodeStr(String plainText){  
        byte[] b=plainText.getBytes();  
        Base64 base64=new Base64();  
        b=base64.encode(b);  
        String s=new String(b);  
        return s;  
    }  
      
    /** 
     *  
     * 创建日期2011-4-25上午10:15:11 
     * 修改日期 
     * 作者：dh     *TODO 使用Base64加密 
     *return 
     */  
    public static String decodeStr(String encodeStr){  
        byte[] b=encodeStr.getBytes();  
        Base64 base64=new Base64();  
        b=base64.decode(b);  
        String s=new String(b);  
        return s;  
    }  

	public static String CRC16(String str) {

		byte[] data = null;
		/* CRC低位字节值表 */
		char crc_lo[] = { 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01,
				0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80,
				0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01,
				0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81,
				0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00,
				0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80,
				0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00,
				0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80,
				0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01,
				0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81,
				0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00,
				0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81,
				0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01,
				0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81,
				0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01,
				0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81,
				0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01,
				0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80,
				0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01,
				0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80,
				0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01,
				0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81,
				0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00,
				0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80,
				0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01,
				0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40 };

		/* CRC 高位字节值表 */
		char crc_hi[] = { 0x00, 0xC0, 0xC1, 0x01, 0xC3, 0x03, 0x02, 0xC2, 0xC6,
				0x06, 0x07, 0xC7, 0x05, 0xC5, 0xC4, 0x04, 0xCC, 0x0C, 0x0D,
				0xCD, 0x0F, 0xCF, 0xCE, 0x0E, 0x0A, 0xCA, 0xCB, 0x0B, 0xC9,
				0x09, 0x08, 0xC8, 0xD8, 0x18, 0x19, 0xD9, 0x1B, 0xDB, 0xDA,
				0x1A, 0x1E, 0xDE, 0xDF, 0x1F, 0xDD, 0x1D, 0x1C, 0xDC, 0x14,
				0xD4, 0xD5, 0x15, 0xD7, 0x17, 0x16, 0xD6, 0xD2, 0x12, 0x13,
				0xD3, 0x11, 0xD1, 0xD0, 0x10, 0xF0, 0x30, 0x31, 0xF1, 0x33,
				0xF3, 0xF2, 0x32, 0x36, 0xF6, 0xF7, 0x37, 0xF5, 0x35, 0x34,
				0xF4, 0x3C, 0xFC, 0xFD, 0x3D, 0xFF, 0x3F, 0x3E, 0xFE, 0xFA,
				0x3A, 0x3B, 0xFB, 0x39, 0xF9, 0xF8, 0x38, 0x28, 0xE8, 0xE9,
				0x29, 0xEB, 0x2B, 0x2A, 0xEA, 0xEE, 0x2E, 0x2F, 0xEF, 0x2D,
				0xED, 0xEC, 0x2C, 0xE4, 0x24, 0x25, 0xE5, 0x27, 0xE7, 0xE6,
				0x26, 0x22, 0xE2, 0xE3, 0x23, 0xE1, 0x21, 0x20, 0xE0, 0xA0,
				0x60, 0x61, 0xA1, 0x63, 0xA3, 0xA2, 0x62, 0x66, 0xA6, 0xA7,
				0x67, 0xA5, 0x65, 0x64, 0xA4, 0x6C, 0xAC, 0xAD, 0x6D, 0xAF,
				0x6F, 0x6E, 0xAE, 0xAA, 0x6A, 0x6B, 0xAB, 0x69, 0xA9, 0xA8,
				0x68, 0x78, 0xB8, 0xB9, 0x79, 0xBB, 0x7B, 0x7A, 0xBA, 0xBE,
				0x7E, 0x7F, 0xBF, 0x7D, 0xBD, 0xBC, 0x7C, 0xB4, 0x74, 0x75,
				0xB5, 0x77, 0xB7, 0xB6, 0x76, 0x72, 0xB2, 0xB3, 0x73, 0xB1,
				0x71, 0x70, 0xB0, 0x50, 0x90, 0x91, 0x51, 0x93, 0x53, 0x52,
				0x92, 0x96, 0x56, 0x57, 0x97, 0x55, 0x95, 0x94, 0x54, 0x9C,
				0x5C, 0x5D, 0x9D, 0x5F, 0x9F, 0x9E, 0x5E, 0x5A, 0x9A, 0x9B,
				0x5B, 0x99, 0x59, 0x58, 0x98, 0x88, 0x48, 0x49, 0x89, 0x4B,
				0x8B, 0x8A, 0x4A, 0x4E, 0x8E, 0x8F, 0x4F, 0x8D, 0x4D, 0x4C,
				0x8C, 0x44, 0x84, 0x85, 0x45, 0x87, 0x47, 0x46, 0x86, 0x82,
				0x42, 0x43, 0x83, 0x41, 0x81, 0x80, 0x40 };

		char crch = 0x00; // ----init
		char crcl = 0x00;
		int i, index;
		data = HexString2Bytes(str);
		for (i = 0; i < str.length() / 2; i++) {
			index = crcl ^ data[i];
			if (index < 0) {
				index += 256;
			}
			crcl = (char) (crch ^ crc_lo[index]);
			crch = crc_hi[index];
		}
		String sh = Integer.toHexString(crch);
		String sl = Integer.toHexString(crcl);
		if (Integer.toHexString(crch).length() == 1)
			sh = "0" + Integer.toHexString(crch);
		if (Integer.toHexString(crcl).length() == 1)
			sl = "0" + Integer.toHexString(crcl);
		return (sh + sl).toUpperCase();
	}
	/**
	 * 得出CRC计算结果
	 * @param buf 要计算CRC的字符串
	 * @return 字符串,2个字节
	 */
	public static String getCRCString(String buff) {
		int crc = 0xFFFF; // initial value
		int polynomial = 0x1021; // 0001 0000 0010 0001 (0, 5, 12)
		
		byte[] data = HexString2Bytes(buff);

		for (int j = 0; j < data.length; j++) {
			//char b = buf.charAt(j);
			byte b = data[j];
			for (int i = 0; i < 8; i++) {
				boolean bit = ((b >> (7 - i) & 1) == 1);
				boolean c15 = ((crc >> 15 & 1) == 1);
				crc <<= 1;
				if (c15 ^ bit)
					crc ^= polynomial;
			}
		}

		crc &= 0xffff;
		String str = ToHexString(crc, 2);//"" + (char) (crc / 256) + (char) (crc % 256);
		return str;
	}
	
	public static String CRC(String msg)
	{
		byte[] data = HexString2Bytes(msg);
		int crc = CRC(data);
		String str = ToHexString(crc,2);
		return str;
		
	}
	
	public static int CRC(byte[] buffer)
    {
        int wTemp = 0;
        int crc = 0xffff;

        for (int i = 0; i < buffer.length; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                wTemp = (short)(((buffer[i] << j) & 0x80) ^ ((crc & 0x8000) >> 8));

                crc <<= 1;

                if (wTemp != 0)
                {
                    crc ^= 0x1021;
                }
            }
        }

        return (crc);
    }

	public static long getSendCount() {
		if (sendCount > 1000000000L)
			sendCount = 1L;
		else {
			sendCount += 1L;
		}
		return sendCount;
	}

	public static String turnDataLength(String data, int length) {
		int data_length = data.length();
		for (int i = data_length; i < length; i++) {
			data = "0" + data;
		}
		return data;
	}

	public static String turnStrLength(String data, int length) {
		int data_length = data.length();
		for (int i = data_length; i < length; i++) {
			data = data + "0";
		}
		return data;
	}

	public static String ToHexString(byte[] bts) {
		StringBuilder strBuild = new StringBuilder();

		for (int i = 0; i < bts.length; i++) {
			strBuild.append(ToHexString(bts[i]));
		}
		return strBuild.toString();
	}
	//加空格 用于阅读输出
	public static String ToHexFormatString(byte[] bts) {
		StringBuilder strBuild = new StringBuilder();

		for (int i = 0; i < bts.length; i++) {
			strBuild.append(ToHexString(bts[i])).append(" ");
		}
		return strBuild.toString();
	}
	

	public static String hex2Ascii(String hex) {
		String result = "";
		int len = hex.length() / 2;
		for (int i = 0; i < len; i++) {
			int tmp = Integer.valueOf(hex.substring(2 * i, 2 * i + 2), 16)
					.intValue();
			result = result + (char) tmp;
		}
		return result;
	}

	public static byte[] HexString2Bytes(String hexstr) {
		byte[] b = new byte[hexstr.length() / 2];
		int j = 0;

		for (int i = 0; i < b.length; i++) {
			char c0 = hexstr.charAt(j++);
			char c1 = hexstr.charAt(j++);

			b[i] = ((byte) (parse(c0) << 4 | parse(c1)));
			//int start = i * 2;
			//int c = Integer.parseInt(hexstr.substring(start, start + 2));
			//b[i] = (byte)c;
		}

		return b;
	}

	private static int parse(char c) {
		if (c >= 'a') {
			return c - 'a' + 10 & 0xF;
		}

		if (c >= 'A') {
			return c - 'A' + 10 & 0xF;
		}

		return c - '0' & 0xF;
	}

	public static String TurnISN(String str) {
		String str1 = "";
		byte[] b = (byte[]) null;
		try {
			b = str.getBytes("gbk");
		} catch (UnsupportedEncodingException e) {
			System.out.println("异常信息(ControllerReport TurnISN)"
					+ e.getMessage());
		}
		System.out.println();
		int i = 0;
		for (int max = b.length; i < max; i++) {
			str1 = str1 + Integer.toHexString(b[i] & 0xFF);
		}
		return str1.toUpperCase();
	}

	// 将数字转换成16进制字符串，不足补零
	public static String ToHexString(byte data) {
		return Integer.toHexString(data & 0xff | 0x100).substring(1);
	}

	// 将数字转换成16进制字符串，不足补零
	public static String ToHexString(Short data) {
		return Integer.toHexString(data & 0xffff | 0x10000).substring(1);
	}

	public static String ToHexString(long val) {
		String tmp = Long.toHexString(val);
		StringBuilder sb = new StringBuilder("0000000000000000");
		sb.replace(16 - tmp.length(), 16, tmp);
		return sb.toString();
	}

	// 将数字转换成16进制字符串，不足补零
	public static String ToHexString(int data) {
		String tmp = Integer.toHexString(data);
		StringBuilder sb = new StringBuilder("00000000");
		sb.replace(8 - tmp.length(), 8, tmp);
		return sb.toString();
	}

	public static String ToHexString(long data, int byteNum) {

		StringBuilder sb = new StringBuilder("");
		for (int m = 0; m < byteNum; m++) {
			sb.append("00");
		}
		int totalLen = byteNum * 2;
		String tmp = Long.toHexString(data);
		sb.replace(totalLen - tmp.length(), totalLen, tmp);
		return sb.toString();
	}

	/**
	 * 将字符串转换成16进制串
	 */
	public static String ToHexString(String str) {
		String str1 = "";
		try {
			byte[] b = str.getBytes("gbk");
			int i = 0;
			int max = b.length;
			for (; i < max; i++) {
				str1 = str1 + Integer.toHexString(b[i] & 0xFF);
			}
		} catch (UnsupportedEncodingException e) {
			System.out.println("异常信息ToHexString"
					+ e.getMessage());
		}
		return str1;
	}
	
	public static boolean isNullOrEmpty(String str)
	{
	   return str == null || str.equals("");
	}

	/**
	 * 将字符串转换成16进制串，长度不足补零
	 */
	public static String ToHexString(String str, int length) {
		if(str == null)
			str = "";
		String str1 = "";
		byte[] b = (byte[]) null;
		try {
			b = str.getBytes("gbk");
		} catch (UnsupportedEncodingException e) {
			System.out.println("异常信息(ControllerReport TurnISN)"
					+ e.getMessage());
		}
		System.out.println();
		int i = 0;
		int max = b.length;
		for (; i < max; i++) {
			str1 = str1 + Integer.toHexString(b[i] & 0xFF);
		}
		str1 = str1.toUpperCase();
		return turnStrLength(str1, length * 2);

	}
	
	

	public static String getStringFromHex(String str1) {
		try {
			str1 = new String(HexString2Bytes(str1), "gbk");
		} catch (UnsupportedEncodingException ex) {
			Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, null, ex);
		}
		return str1;
	}

	public static int getYear(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
		String year = format.format(date);
		return Integer.valueOf(year.substring(0, 4)).intValue();
	}

	public static int getMonth(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
		String year = format.format(date);
		return Integer.valueOf(year.substring(4, 6)).intValue();
	}

	public static int getDay(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
		String year = format.format(date);
		return Integer.valueOf(year.substring(6, 8)).intValue();
	}

	public static int getHour(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
		String year = format.format(date);
		return Integer.valueOf(year.substring(8, 10)).intValue();
	}

	public static int getMinute(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
		String year = format.format(date);
		return Integer.valueOf(year.substring(10, 12)).intValue();
	}

	public static int getSeconds(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
		String year = format.format(date);
		return Integer.valueOf(year.substring(12, 14)).intValue();
	}

	public static String getDateBCDStr(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmss");
		return format.format(date);
	}

	public static String getHexDateTime(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String hexdate = format.format(date);

		String year = turnDataLength(Integer.toString(
				Integer.valueOf(hexdate.substring(0, 4)).intValue(), 16), 4);
		String month = turnDataLength(Integer.toString(
				Integer.valueOf(hexdate.substring(4, 6)).intValue(), 16), 2);
		String day = turnDataLength(Integer.toString(
				Integer.valueOf(hexdate.substring(6, 8)).intValue(), 16), 2);
		String hour = turnDataLength(Integer.toString(
				Integer.valueOf(hexdate.substring(8, 10)).intValue(), 16), 2);
		String minute = turnDataLength(Integer.toString(
				Integer.valueOf(hexdate.substring(10, 12)).intValue(), 16), 2);
		String seconds = turnDataLength(Integer.toString(
				Integer.valueOf(hexdate.substring(12, 14)).intValue(), 16), 2);

		return day + month + year + hour + minute + seconds;
	}

	// 对于参数指定的日期和时间，返回自 1970 年 1 月 1 日 00:00:00 GMT 以来的毫秒数
	public static String getUTC(Date date) {
		
		//long dt = date.getTime()/1000;//
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
	

		long dt = date.UTC(date.getYear(), date.getMonth(), date.getDay(),
				calendar.get(Calendar.HOUR_OF_DAY) - 8, date.getMinutes(), date.getSeconds());
		return ToHexString(dt / 1000 , 8);
	}

	public static long myround(double num) {
		BigDecimal b = new BigDecimal(num);
		num = b.setScale(2, 4).longValue();
		return (long) num;
	}

	public static Date strToDate(String str) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return format.parse(str);
		} catch (ParseException e) {
			System.out.println("异常信息（strToDate）:" + e.toString());
		}
		return null;
	}

	public static String encoderStringEscape(String strEscape) {
		strEscape = strEscape.toUpperCase();
		int byteNum = strEscape.length() / 2;
		StringBuilder sb = new StringBuilder();
		for (int m = 0; m < byteNum; m++) {
			int start = m * 2;

			String temp = strEscape.substring(start, start + 2);
			if (temp.equals("5A"))
				sb.append("5A02");
			else if (temp.equals("5B"))
				sb.append("5A01");
			else if (temp.equals("5E"))
				sb.append("5E02");
			else if (temp.equals("5D"))
				sb.append("5E01");
			else
				sb.append(temp);
		}
		// strEscape = strEscape.replaceAll("5A", "5A02");
		// strEscape = strEscape.replaceAll("5B", "5A01");
		// strEscape = strEscape.replaceAll("5E", "5E02");
		// strEscape = strEscape.replaceAll("5D", "5E01");
		return sb.toString();
	}

	public static String decoderStringEscape(String strEscape) {
		strEscape = strEscape.toUpperCase();
		strEscape = strEscape.replaceAll("5A01", "5B");
		strEscape = strEscape.replaceAll("5a01", "5B");
		strEscape = strEscape.replaceAll("5A02", "5A");
		strEscape = strEscape.replaceAll("5a02", "5A");
		strEscape = strEscape.replaceAll("5E01", "5D");
		strEscape = strEscape.replaceAll("5e01", "5D");
		strEscape = strEscape.replaceAll("5E02", "5E");
		strEscape = strEscape.replaceAll("5e02", "5E");
		return strEscape;
	}


	public static Timestamp convertToTimestamp(String message) {
		long time = Long.valueOf(message, 16).longValue();

		int year = (int) (time >> 26 & 0x3F) + 2000;
		int month = (int) (time >> 22 & 0xF);
		int day = (int) (time >> 17 & 0x1F);
		int hour = (int) (time >> 12 & 0x1F);
		int minute = (int) (time >> 6 & 0x3F);
		int second = (int) (time & 0x3F);
		Calendar cd = Calendar.getInstance();
		cd.set(year, month - 1, day, hour, minute, second);
		return new Timestamp(cd.getTimeInMillis());
	}

	public static String encrypt(int key, String str) {
		int m1 = 10000000;
		int a1 = 20000000;
		int c1 = 30000000;

		byte[] b = HexString2Bytes(str);
		int size = b.length;
		if (key == 0)
			key = 1;
		for (int i = 0; i < size; i++) {
			key = a1 * (key % m1) + c1;
			int tmp49_47 = i;
			byte[] tmp49_45 = b;
			tmp49_45[tmp49_47] = ((byte) (tmp49_45[tmp49_47] ^ (char) (key >> 20) & 0xFF));
		}
		return ToHexString(b);
	}

	public static void main(String[] args) {
		
		short t = -23;
		String str = Integer.toHexString(t &0xff);
		
		System.out.println(str);
		
		Date date = new Date();

		System.out.println(date.toString());
		System.out.println(date.getYear()+ "," + date.getMonth()+ "," + date.getDay()+ "," +
				date.getHours()+ "," + date.getMinutes()+ "," + date.getSeconds());
		long dt = Date.UTC(date.getYear(), (date.getMonth() ), 29,
				date.getHours() - 8, date.getMinutes(), date.getSeconds());
		
		Date date1 = new Date(dt);
		
		System.out.println(date1.toString());
	}
}
