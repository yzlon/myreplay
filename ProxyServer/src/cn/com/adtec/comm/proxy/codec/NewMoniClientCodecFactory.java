package cn.com.adtec.comm.proxy.codec;

import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class NewMoniClientCodecFactory implements ProtocolCodecFactory{
	private NewMoniClientDecoder decoder;
	private NewMoniEncoder encoder;
	
	public NewMoniClientCodecFactory(){
		this(Charset.defaultCharset());
	}
	public NewMoniClientCodecFactory(Charset defaultCharset) {
		this.encoder = new NewMoniEncoder(defaultCharset);
		this.decoder = new NewMoniClientDecoder(defaultCharset);
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
