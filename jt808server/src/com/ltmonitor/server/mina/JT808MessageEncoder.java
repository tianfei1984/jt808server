package com.ltmonitor.server.mina;

import java.nio.charset.Charset;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.ltmonitor.jt808.protocol.T808Message;
import com.ltmonitor.jt808.tool.Tools;


public class JT808MessageEncoder extends ProtocolEncoderAdapter {
	private Charset charset;

	public JT808MessageEncoder(Charset charset) {
		this.charset = charset;
	}

	public void encode(IoSession session, Object message,
			ProtocolEncoderOutput out) throws Exception {
		//out.write(message);
		
		IoBuffer buf = IoBuffer.allocate(500).setAutoExpand(true);

		//byte[] content = Tools.HexString2Bytes(message.toString());
		buf.put((byte[])message);
		buf.flip();
		out.write(buf);
		out.flush();
		buf.free();
		
	}
}
