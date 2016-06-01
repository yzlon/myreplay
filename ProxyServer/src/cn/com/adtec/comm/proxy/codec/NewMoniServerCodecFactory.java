package cn.com.adtec.comm.proxy.codec;

import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class NewMoniServerCodecFactory implements ProtocolCodecFactory{
	private NewMoniServerDecoder decoder;
	private NewMoniEncoder encoder;
	
	public NewMoniServerCodecFactory(){
		this(Charset.defaultCharset());
	}
	public NewMoniServerCodecFactory(Charset defaultCharset) {
		this.encoder = new NewMoniEncoder(defaultCharset);
		this.decoder = new NewMoniServerDecoder(defaultCharset);
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
