package com.ltmonitor.server.mina;

import java.nio.charset.Charset;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class JT808MessageCodecFactory implements ProtocolCodecFactory {
	private final JT808MessageDecoder decoder;
	private final JT808MessageEncoder encoder;

	public JT808MessageCodecFactory() {
		this.decoder = new JT808MessageDecoder(Charset.forName("utf-8"));
		this.encoder = new JT808MessageEncoder(Charset.forName("utf-8"));
	}

	public ProtocolDecoder getDecoder(IoSession arg0) throws Exception {
		return this.decoder;
	}

	public ProtocolEncoder getEncoder(IoSession arg0) throws Exception {
		return this.encoder;
	}
}
