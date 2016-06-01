package cn.com.adtec.comm.proxy.codec;

import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class SubSOAPServerCodecFactory implements ProtocolCodecFactory{
	private SoapDecoder decoder;
	private ManageServerEncoder encoder;
	
	public SubSOAPServerCodecFactory(){
		this(Charset.defaultCharset());
	}
	public SubSOAPServerCodecFactory(Charset defaultCharset) {
		this.encoder = new ManageServerEncoder(defaultCharset);
		this.decoder = new SoapDecoder(defaultCharset);
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
