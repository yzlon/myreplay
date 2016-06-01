package cn.com.adtec.comm.proxy.codec;

import java.nio.charset.Charset;


import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;



public class NewMoniEncoder extends ProtocolEncoderAdapter{
	private  Charset charset;
	
	public NewMoniEncoder(Charset charset) {
	this.charset = charset;
	}

/*	
		public void encode(IoSession arg0, Object arg1, ProtocolEncoderOutput arg2)
			throws Exception {
		// TODO Auto-generated method stub
			System.out.println("该我encode处理了");
			try{
		CommObject co = (CommObject) arg1;
		CharsetEncoder ce = charset.newEncoder();
		IoBuffer buffer = IoBuffer.allocate(CommObject.PkgHeadLength)
				.setAutoExpand(true);

		String tmp = co.getHead().getCommlen();
		buffer.putString(addonSpace(tmp, CommObject.COMMLEN_LENGTH ), ce);

		tmp = co.getHead().getOutipstr();
		buffer.putString(addonSpace(tmp, CommObject.OUTIPSTR_LENGTH ), ce);

		tmp = co.getHead().getOutport();
		buffer.putString(addonSpace(tmp, CommObject.OUTPORT_LENGTH ), ce);

		tmp = co.getHead().getOtimeout();
		buffer.putString(addonSpace(tmp, CommObject.OTIMEOUT_LENGTH), ce);

		tmp = co.getHead().getCommname();
		buffer.putString(addonSpace(tmp, CommObject.COMMNAME_LENGTH ), ce);

		tmp = co.getHead().getCompname();
		buffer.putString(addonSpace(tmp, CommObject.COMPNAME_LENGTH), ce);

		tmp = co.getHead().getCodein();
		buffer.putString(addonSpace(tmp, CommObject.CODEIN_LENGTH ), ce);

		tmp = co.getHead().getCodeout();
		buffer.putString(addonSpace(tmp, CommObject.CODEOUT_LENGTH ), ce);

		tmp = co.getHead().getExtlen();
		buffer.putString(addonSpace(tmp, CommObject.EXTLEN_LENGTH ), ce);

		if (!tmp.trim().equals("0")) {
			// 转扩展段
			for (int i = 0; i < co.getParas().length; i++) {
				tmp = co.getParas()[i].getParasn();
				buffer.putString(
						addonSpace(tmp, CommObject.PARASN_LENGTH ), ce);

				tmp = co.getParas()[i].getParaname();
				buffer.putString(addonSpace(tmp,
						CommObject.PARANAME_LENGTH ), ce);

				tmp = co.getParas()[i].getInitvalue();
				buffer.putString(addonSpace(tmp,
						CommObject.INITVALUE_LENGTH ), ce);
			}
		}

		if (!co.getHead().getCommlen().trim().equals("")) {
			// 转报文体
			int bodyLen = Integer.parseInt(co.getHead().getCommlen());
			tmp = co.getBody();
			buffer.putString(addonSpace(tmp, bodyLen), ce);
		}
		buffer.flip();
		arg2.write(buffer);
			}catch (Exception e)
			{
				e.printStackTrace();
			}
	}
	*/
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out)
			throws Exception {
		// TODO Auto-generated method stub
		
		
		//CharsetEncoder ce = charset.newEncoder();
		if (message instanceof String)
		{
			byte[] tmp = ((String)message).getBytes(charset);
			IoBuffer buf = IoBuffer.allocate(tmp.length).setAutoExpand(false);
			buf.put(tmp);
			buf.flip();
			//System.out.println("____编码........报文长度["+tmp.length+"]报文内容["+message+"]");
			out.write(buf);
			tmp = null;
			buf = null;
		}
		else
		{
			out.write(message);
		}

		
		//CharsetEncoder ce = charset.newEncoder();
		/*
		NewMoniObject nmo = (NewMoniObject)message;
		if (null!=nmo)
		{
			nmo.getInnerBuffer().flip();
			out.write(nmo.getInnerBuffer());
		}
*/


		
	}
}
