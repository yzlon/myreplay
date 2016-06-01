package cn.com.adtec.comm.proxy.codec;

import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

import cn.com.adtec.comm.proxy.server.AbstractSubServer;

public class SubFLOWServerCodecFactory implements ProtocolCodecFactory{
	private SubFlowDecoder decoder;
	private ManageServerEncoder encoder;
	
	public SubFLOWServerCodecFactory(){
		this(Charset.defaultCharset(),null);
	}
	public SubFLOWServerCodecFactory(Charset defaultCharset,AbstractSubServer ass) {
		this.encoder = new ManageServerEncoder(defaultCharset);
		this.decoder = new SubFlowDecoder(defaultCharset,ass);
	}
	public ProtocolDecoder getDecoder(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub
		return decoder;
	}

	public ProtocolEncoder getEncoder(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub
		return encoder;
	}

}
