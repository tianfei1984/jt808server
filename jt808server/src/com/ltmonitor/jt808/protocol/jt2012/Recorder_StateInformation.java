package com.ltmonitor.jt808.protocol.jt2012;

import com.ltmonitor.jt808.protocol.BitConverter;


/** 
 采集状态信号配置信息
 
*/
public class Recorder_StateInformation implements IRecorderDataBlock_2012
{
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

	/** 
	 实时时间
	 
	*/
	private java.util.Date privateRealTime = new java.util.Date(0);
	public final java.util.Date getRealTime()
	{
		return privateRealTime;
	}
	public final void setRealTime(java.util.Date value)
	{
		privateRealTime = value;
	}

	/** 
	 D7状态信号
	 
	*/
	private String privateD7State;
	public final String getD7State()
	{
		return privateD7State;
	}
	public final void setD7State(String value)
	{
		privateD7State = value;
	}

	/** 
	 D7状态名称
	 
	*/
	private String privateD7Name;
	public final String getD7Name()
	{
		return privateD7Name;
	}
	public final void setD7Name(String value)
	{
		privateD7Name = value;
	}

	/** 
	 D6状态信号
	 
	*/
	private String privateD6State;
	public final String getD6State()
	{
		return privateD6State;
	}
	public final void setD6State(String value)
	{
		privateD6State = value;
	}

	/** 
	 D6状态名称
	 
	*/
	private String privateD6Name;
	public final String getD6Name()
	{
		return privateD6Name;
	}
	public final void setD6Name(String value)
	{
		privateD6Name = value;
	}

	/** 
	 D5状态信号
	 
	*/
	private String privateD5State;
	public final String getD5State()
	{
		return privateD5State;
	}
	public final void setD5State(String value)
	{
		privateD5State = value;
	}

	/** 
	 D5状态名称
	 
	*/
	private String privateD5Name;
	public final String getD5Name()
	{
		return privateD5Name;
	}
	public final void setD5Name(String value)
	{
		privateD5Name = value;
	}

	/** 
	 D4状态信号
	 
	*/
	private String privateD4State;
	public final String getD4State()
	{
		return privateD4State;
	}
	public final void setD4State(String value)
	{
		privateD4State = value;
	}

	/** 
	 D4状态名称
	 
	*/
	private String privateD4Name;
	public final String getD4Name()
	{
		return privateD4Name;
	}
	public final void setD4Name(String value)
	{
		privateD4Name = value;
	}

	/** 
	 D3状态信号
	 
	*/
	private String privateD3State;
	public final String getD3State()
	{
		return privateD3State;
	}
	public final void setD3State(String value)
	{
		privateD3State = value;
	}

	/** 
	 D3状态名称
	 
	*/
	private String privateD3Name;
	public final String getD3Name()
	{
		return privateD3Name;
	}
	public final void setD3Name(String value)
	{
		privateD3Name = value;
	}

	/** 
	 D2状态信号
	 
	*/
	private String privateD2State;
	public final String getD2State()
	{
		return privateD2State;
	}
	public final void setD2State(String value)
	{
		privateD2State = value;
	}

	/** 
	 D2状态名称
	 
	*/
	private String privateD2Name;
	public final String getD2Name()
	{
		return privateD2Name;
	}
	public final void setD2Name(String value)
	{
		privateD2Name = value;
	}

	/** 
	 D1状态信号
	 
	*/
	private String privateD1State;
	public final String getD1State()
	{
		return privateD1State;
	}
	public final void setD1State(String value)
	{
		privateD1State = value;
	}

	/** 
	 D1状态名称
	 
	*/
	private String privateD1Name;
	public final String getD1Name()
	{
		return privateD1Name;
	}
	public final void setD1Name(String value)
	{
		privateD1Name = value;
	}

	/** 
	 D0状态信号
	 
	*/
	private String privateD0State;
	public final String getD0State()
	{
		return privateD0State;
	}
	public final void setD0State(String value)
	{
		privateD0State = value;
	}

	/** 
	 D0状态名称
	 
	*/
	private String privateD0Name;
	public final String getD0Name()
	{
		return privateD0Name;
	}
	public final void setD0Name(String value)
	{
		privateD0Name = value;
	}

	public final byte[] WriteToBytes()
	{
		byte[] bytes = new byte[80];
		//获取状态信息字节数组
		byte[] state1 = GetStateBytes(getD0Name());
		byte[] state2 = GetStateBytes(getD1Name());
		byte[] state3 = GetStateBytes(getD2Name());
		byte[] state4 = GetStateBytes(getD3Name());
		byte[] state5 = GetStateBytes(getD4Name());
		byte[] state6 = GetStateBytes(getD5Name());
		byte[] state7 = GetStateBytes(getD6Name());
		byte[] state8 = GetStateBytes(getD7Name());
		System.arraycopy(state1, 0, bytes, 0, state1.length);
		System.arraycopy(state2, 0, bytes, 10, state2.length);
		System.arraycopy(state3, 0, bytes, 20, state3.length);
		System.arraycopy(state4, 0, bytes, 30, state4.length);
		System.arraycopy(state5, 0, bytes, 40, state5.length);
		System.arraycopy(state6, 0, bytes, 50, state6.length);
		System.arraycopy(state7, 0, bytes, 60, state7.length);
		System.arraycopy(state8, 0, bytes, 70, state8.length);
		return bytes;
	}

	public final void ReadFromBytes(byte[] bytes)
	{
		setRealTime(new java.util.Date(java.util.Date.parse("20" + String.format("%02X", bytes[0]) + "-" + String.format("%02X", bytes[1]) + "-" + String.format("%02X", bytes[2]) + " " + String.format("%02X", bytes[3]) + ":" + String.format("%02X", bytes[4]) + ":" + String.format("%02X", bytes[5]))));

		if ((bytes[6] & 0x80) == 0x80)
		{
			setD7State("1");
		}
		else
		{
			setD7State("0");
		}
		if ((bytes[6] & 0x40) == 0x40)
		{
			setD6State("1");
		}
		else
		{
			setD6State("0");
		}
		if ((bytes[6] & 0x20) == 0x20)
		{
			setD5State("1");
		}
		else
		{
			setD5State("0");
		}
		if ((bytes[6] & 0x10) == 0x10)
		{
			setD4State("1");
		}
		else
		{
			setD4State("0");
		}
		if ((bytes[6] & 8) == 8)
		{
			setD3State("1");
		}
		else
		{
			setD3State("0");
		}
		if ((bytes[6] & 4) == 4)
		{
			setD2State("1");
		}
		else
		{
			setD2State("0");
		}
		if ((bytes[6] & 2) == 2)
		{
			setD1State("1");
		}
		else
		{
			setD1State("0");
		}
		if ((bytes[6] & 0) == 0)
		{
			setD0State("1");
		}
		else
		{
			setD0State("0");
		}
		byte[] d7NameBytes = new byte[10];
		System.arraycopy(bytes,7,d7NameBytes,0,10);
		setD7Name(GetStateName(d7NameBytes));
		byte[] d6NameBytes = new byte[10];
		System.arraycopy(bytes, 17, d6NameBytes, 0, 10);
		setD6Name(GetStateName(d6NameBytes));
		byte[] d5NameBytes = new byte[10];
		System.arraycopy(bytes, 27, d5NameBytes, 0, 10);
		setD5Name(GetStateName(d5NameBytes));
		byte[] d4NameBytes = new byte[10];
		System.arraycopy(bytes, 37, d4NameBytes, 0, 10);
		setD4Name(GetStateName(d4NameBytes));
		byte[] d3NameBytes = new byte[10];
		System.arraycopy(bytes, 47, d3NameBytes, 0, 10);
		setD3Name(GetStateName(d3NameBytes));
		byte[] d2NameBytes = new byte[10];
		System.arraycopy(bytes, 57, d2NameBytes, 0, 10);
		setD2Name(GetStateName(d2NameBytes));
		byte[] d1NameBytes = new byte[10];
		System.arraycopy(bytes, 67, d1NameBytes, 0, 10);
		setD1Name(GetStateName(d1NameBytes));
		byte[] d0NameBytes = new byte[10];
		System.arraycopy(bytes, 77, d0NameBytes, 0, 10);
		setD0Name(GetStateName(d0NameBytes));

	}

	private String GetStateName(byte[] NameBytes)
	{
		String StateName = BitConverter.getString(NameBytes).trim();
		return StateName;
	}

	private byte[] GetStateBytes(String StateName)
	{
		//定义10个字节数组接收 名称信息
		byte[] bytesState;
		//接收信息名称转换为字节数组
		if (StateName.length()>5)
		{
			StateName.substring(0, 5);
		}
		bytesState = BitConverter.getBytes(StateName);
		return bytesState;
	}
}

