package com.ltmonitor.server.mina;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.ltmonitor.jt808.protocol.T808Message;
import com.ltmonitor.jt808.tool.Tools;


public class JT808MessageDecoder extends CumulativeProtocolDecoder {
	private static Logger logger = Logger.getLogger(JT808MessageDecoder.class);
	private CharsetDecoder decoder;
	/**
	 * 转发客户端
	 */
	private MinaClient transferClient;

	public JT808MessageDecoder(Charset charset) {
		this.decoder = charset.newDecoder();
	}

	protected boolean doDecode(IoSession session, IoBuffer in,
			ProtocolDecoderOutput out) throws Exception {

		if (in.remaining() < 1) {
			return false;
		}
		in.mark();
		byte[] data = new byte[in.remaining()];
		in.get(data);
		//this.logger.warn(Tools.ToHexString(data));
		int pos = 0;
		in.reset();
		while (in.remaining() > 0) {
			in.mark();
			byte tag = in.get();
			//搜索包的开始位置
			if(tag == 0x7E && in.remaining() > 0)
			{
				tag = in.get();
				//防止是两个0x7E,取后面的为包的开始位置
				//寻找包的结束
				while(tag != 0x7E)
				{
					if(in.remaining() <= 0)
					{
						in.reset(); //没有找到结束包，等待下一次包
						//logger.error("半包:"+Tools.ToHexString(data));
						return false;
					}
					tag = in.get();
				}
				pos = in.position();
				int packetLength = pos - in.markValue();
				if(packetLength > 1)
				{
					byte[] tmp = new byte[packetLength];
					in.reset();
					in.get(tmp);
					T808Message message = new T808Message();
					message.ReadFromBytes(tmp);			
					JT808TransferQueue.forward(message.getSimNo(),tmp); //808转发
					out.write(message); //触发接收Message的事件
				}else
				{
					//说明是两个0x7E
					in.reset();
					in.get(); //两个7E说明前面是包尾，后面是包头
				}		
			}
		}
		

		return false;
	}
}
