package com.ltmonitor.jt808.protocol;

public class JT808Common
{

	private static java.util.Hashtable descrMap = new java.util.Hashtable();

	private static java.util.Hashtable ErrorMsgMap = new java.util.Hashtable();

	private static java.util.Hashtable paramMap = new java.util.Hashtable();

	private static java.util.Hashtable FieldTypeMap = new java.util.Hashtable();
	
	private static boolean new808Protocol;

	public static String GetDescr(String ID)
	{
		if (descrMap.isEmpty())
		{
			Init();
		}

		return "" + descrMap.get(ID);
	}

	public static String GetParamType(int Field)
	{
		if (FieldTypeMap.isEmpty())
		{
			Init();
		}

		return "" + FieldTypeMap.get(Field);
	}

	public static String GetParamDescr(String ID)
	{
		if (paramMap.isEmpty())
		{
			SetParamMap();
		}

		return "" + paramMap.get(ID);
	}

	public static String DescrSocketError(int ErrorCode)
	{
		if (ErrorMsgMap.isEmpty())
		{
			SetErrorMsg();
		}
		return "" + ErrorMsgMap.get(ErrorCode);
	}

	private static void SetErrorMsg()
	{
		ErrorMsgMap.put(995, "连接已关闭");
		ErrorMsgMap.put(10004, "操作被取消");
		ErrorMsgMap.put(10013, "请求的地址是一个广播地址，但不设置标志");
		ErrorMsgMap.put(10014, "无效的参数");
		ErrorMsgMap.put(10022, "套接字没有绑定，无效的地址，或听不调用之前接受");
		ErrorMsgMap.put(10024, "没有更多的文件描述符，接受队列是空的");
		ErrorMsgMap.put(10035, "套接字是非阻塞的，指定的操作将阻止");
		ErrorMsgMap.put(10036, "一个阻塞的Winsock操作正在进行中");
		ErrorMsgMap.put(10037, "操作完成，没有阻塞操作正在进行中");
		ErrorMsgMap.put(10038, "描述符不是一个套接字");
		ErrorMsgMap.put(10039, "目标地址是必需的");
		ErrorMsgMap.put(10040, "数据报太大，无法进入缓冲区，将被截断");
		ErrorMsgMap.put(10041, "指定的端口是为这个套接字错误类型");
		ErrorMsgMap.put(10042, "股权不明，或不支持的");
		ErrorMsgMap.put(10043, "指定的端口是不支持");
		ErrorMsgMap.put(10044, "套接字类型不支持在此地址族");
		ErrorMsgMap.put(10045, " Socket是不是一个类型，它支持面向连接的服务");
		ErrorMsgMap.put(10047, "地址族不支持");
		ErrorMsgMap.put(10048, "地址在使用中");
		ErrorMsgMap.put(10049, "地址是不是可以从本地机器");
		ErrorMsgMap.put(10050, "网络子系统失败");
		ErrorMsgMap.put(10051, "网络可以从这个主机在这个时候不能达到");
		ErrorMsgMap.put(10052, "连接超时设置SO_KEEPALIVE时");
		ErrorMsgMap.put(10053, "连接被中止，由于超时或其他故障");
		ErrorMsgMap.put(10054, "连接被重置连接被远程端重置远程端");
		ErrorMsgMap.put(10055, "无缓冲区可用空间");
		ErrorMsgMap.put(10056, "套接字已连接");
		ErrorMsgMap.put(10057, "套接字未连接");
		ErrorMsgMap.put(10058, "套接字已关闭");
		ErrorMsgMap.put(10060, "尝试连接超时");
		ErrorMsgMap.put(10061, "连接被强制拒绝");
		ErrorMsgMap.put(10101, "监听服务已关闭");
		ErrorMsgMap.put(10201, "套接字已创建此对象");
		ErrorMsgMap.put(10202, "套接字尚未创建此对象");
		ErrorMsgMap.put(11001, "权威的答案：找不到主机");
		ErrorMsgMap.put(11002, "非权威的答案：找不到主机");
		ErrorMsgMap.put(11003, "非可恢复的错误");
		ErrorMsgMap.put(11004, "有效的名称，没有请求类型的数据记录");
	}
	private static void Init()
	{
		descrMap.put("0x0001", "终端通用应答");
		descrMap.put("0x0003", "终端注销");
		descrMap.put("0x8001", "平台通用应答");
		descrMap.put("0x0002", "终端心跳");
		descrMap.put("0x0100", "终端注册");
		descrMap.put("0x8100", "终端注册应答");
		descrMap.put("0x0101", "终端注销");
		descrMap.put("0x0102", "终端鉴权");
		descrMap.put("0x8103", "设置终端参数");
		descrMap.put("0x8104", "查询终端参数");
		descrMap.put("0x0104", "查询终端参数应答");
		descrMap.put("0x8105", "终端控制");
		descrMap.put("0x0200", "位置信息汇报");
		descrMap.put("0x8201", "位置信息查询");
		descrMap.put("0x0201", "位置信息查询应答");
		descrMap.put("0x8202", "临时位置跟踪控制");
		descrMap.put("0x8300", "文本信息下发");
		descrMap.put("0x8301", "事件设置");
		descrMap.put("0x0301", "事件报告");
		descrMap.put("0x8302", "提问下发");
		descrMap.put("0x0302", "提问应答");
		descrMap.put("0x8303", "信息点播菜单设置");
		descrMap.put("0x0303", "信息点播/取消");
		descrMap.put("0x8802", "存储多媒体数据检索");
		descrMap.put("0x0802", "存储多媒体数据检索应答");
		descrMap.put("0x8803", "存储多媒体数据上传");
		descrMap.put("0x8804", "录音开始命令");
		descrMap.put("0x8900", "数据下行透传");
		descrMap.put("0x0900", "数据上行透传");
		descrMap.put("0x8304", "信息服务");
		descrMap.put("0x8400", "电话回拨");
		descrMap.put("0x8401", "设置电话本");
		descrMap.put("0x8500", "车辆控制");
		descrMap.put("0x0500", "车辆控制应答");
		descrMap.put("0x8600", "设置圆形区域");
		descrMap.put("0x8601", "删除圆形区域");
		descrMap.put("0x8602", "设置矩形区域");
		descrMap.put("0x8603", "删除矩形区域");
		descrMap.put("0x8604", "设置多边形区域");
		descrMap.put("0x8605", "删除多边形区域");
		descrMap.put("0x8606", "设置路线");
		descrMap.put("0x8607", "删除路线");
		descrMap.put("0x8700", "行车记录仪数据采集命令");
		descrMap.put("0x0700", "行车记录仪数据上报");
		descrMap.put("0x8701", "行车记录仪参数下达命令");
		descrMap.put("0x0701", "电子运单上报");
		descrMap.put("0x0702", "驾驶员身份信息采集上报");
		descrMap.put("0x0800", "多媒体事件信息上报");
		descrMap.put("0x0801", "多媒体数据上传");
		descrMap.put("0x8800", "多媒体数据上传应答");
		descrMap.put("0x8801", "摄像头立即拍摄命令");
		descrMap.put("0x0901", "数据压缩上报");
		descrMap.put("0x8A00", "平台RSA公钥");
		descrMap.put("0x0A00", "终端RSA公钥");

		FieldTypeMap.put(0x0001, "DWORD");
		FieldTypeMap.put(0x0002, "DWORD");
		FieldTypeMap.put(0x0003, "DWORD");
		FieldTypeMap.put(0x0004, "DWORD");
		FieldTypeMap.put(0x0005, "DWORD");
		FieldTypeMap.put(0x0006, "DWORD");
		FieldTypeMap.put(0x0007, "DWORD");
		FieldTypeMap.put(0x0010, "STRING");
		FieldTypeMap.put(0x0011, "STRING");
		FieldTypeMap.put(0x0012, "STRING");
		FieldTypeMap.put(0x0013, "STRING");
		FieldTypeMap.put(0x0014, "STRING");
		FieldTypeMap.put(0x0015, "STRING");
		FieldTypeMap.put(0x0016, "STRING");
		FieldTypeMap.put(0x0017, "STRING");
		FieldTypeMap.put(0x0018, "DWORD");
		FieldTypeMap.put(0x0019, "DWORD");
		FieldTypeMap.put(0x0020, "DWORD");
		FieldTypeMap.put(0x0021, "DWORD");
		FieldTypeMap.put(0x0022, "DWORD");
		FieldTypeMap.put(0x0027, "DWORD");
		FieldTypeMap.put(0x0028, "DWORD");
		FieldTypeMap.put(0x0029, "DWORD");
		FieldTypeMap.put(0x002C, "DWORD");
		FieldTypeMap.put(0x002D, "DWORD");
		FieldTypeMap.put(0x002E, "DWORD");
		FieldTypeMap.put(0x002F, "DWORD");
		FieldTypeMap.put(0x0030, "DWORD");
		FieldTypeMap.put(0x00031, "WORD");
		FieldTypeMap.put(0x0040, "STRING");
		FieldTypeMap.put(0x0041, "STRING");
		FieldTypeMap.put(0x0042, "STRING");
		FieldTypeMap.put(0x0043, "STRING");
		FieldTypeMap.put(0x0044, "STRING");
		FieldTypeMap.put(0x0045, "DWORD");
		FieldTypeMap.put(0x0046, "DWORD");
		FieldTypeMap.put(0x0047, "DWORD");
		FieldTypeMap.put(0x0048, "STRING");
		FieldTypeMap.put(0x0049, "STRING");
		FieldTypeMap.put(0x0050, "DWORD");
		FieldTypeMap.put(0x0051, "DWORD");
		FieldTypeMap.put(0x0052, "DWORD");
		FieldTypeMap.put(0x0053, "DWORD");
		FieldTypeMap.put(0x0054, "DWORD");
		FieldTypeMap.put(0x0055, "DWORD");
		FieldTypeMap.put(0x0056, "DWORD");
		FieldTypeMap.put(0x0057, "DWORD");
		FieldTypeMap.put(0x0058, "DWORD");
		FieldTypeMap.put(0x0059, "DWORD");
		FieldTypeMap.put(0x005A, "DWORD");
		FieldTypeMap.put(0x0070, "DWORD");
		FieldTypeMap.put(0x0071, "DWORD");
		FieldTypeMap.put(0x0072, "DWORD");
		FieldTypeMap.put(0x0073, "DWORD");
		FieldTypeMap.put(0x0074, "DWORD");
		FieldTypeMap.put(0x0080, "DWORD");
		FieldTypeMap.put(0x0081, "WORD");
		FieldTypeMap.put(0x0082, "WORD");
		FieldTypeMap.put(0x0083, "STRING");
		FieldTypeMap.put(0x0084, "BYTE");
		FieldTypeMap.put(0x0100, "DWORD");
		FieldTypeMap.put(0x0101, "DWORD");
		FieldTypeMap.put(0x0102, "DWORD");
		FieldTypeMap.put(0x0103, "BYTE");
		FieldTypeMap.put(0x0104, "BYTE");
		FieldTypeMap.put(0x0105, "STRING");
		FieldTypeMap.put(0x0106, "DWORD");
		FieldTypeMap.put(0x0107, "DWORD");
		FieldTypeMap.put(0x0108, "DWORD");
		FieldTypeMap.put(0x0109, "DWORD");
		FieldTypeMap.put(0x010a, "DWORD");
		FieldTypeMap.put(0x010b, "DWORD");
		FieldTypeMap.put(0x010c, "BYTE");
		FieldTypeMap.put(0x010d, "WORD");
		FieldTypeMap.put(0x010e, "WORD");
		FieldTypeMap.put(0x010f, "WORD");
	}

	private static void SetParamMap()
	{
		paramMap.put("0x0001", "终端心跳发送间隔(s)");
		paramMap.put("0x0002", "TCP应答超时时间(s)");
		paramMap.put("0x0003", "TCP消息重传次数");
		paramMap.put("0x0004", "UDP应答超时时间(s)");
		paramMap.put("0x0013", "主服务器IP地址");
		paramMap.put("0x0017", "备份服务器IP地址");
		paramMap.put("0x0018", "服务器TCP端口");
		paramMap.put("0x0019", "服务器UDP端口");
		paramMap.put("0x0055", "最高车速,单位km/h");
		paramMap.put("0x0056", "超速持续时间(s)");
		paramMap.put("0x0080", "里程表读数");
		paramMap.put("0x0081", "所在省域ID");
		paramMap.put("0x0082", "所在市域ID");
		paramMap.put("0x0083", "机动车号牌");
		paramMap.put("0x0084", "车辆颜色");
	}

	public static boolean isNew808Protocol() {
		new808Protocol = false;
		return new808Protocol;
	}

	public static void setNew808Protocol(boolean new808Protocol) {
		JT808Common.new808Protocol = new808Protocol;
	}

}