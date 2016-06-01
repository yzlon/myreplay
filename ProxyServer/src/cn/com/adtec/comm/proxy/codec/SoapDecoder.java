package cn.com.adtec.comm.proxy.codec;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import cn.com.adtec.comm.proxy.bean.SOAPCommObject;

public class SoapDecoder extends CumulativeProtocolDecoder {
	private final Charset charset;
	private final AttributeKey CONTEXT = new AttributeKey(getClass(), "context");
	private final int strContentLen = "Content-Length".length();

	public SoapDecoder(Charset charset) {
		this.charset = charset;
	}

	@Override
	/**
	 * 获取SOAP报文
	 */
	protected boolean doDecode(IoSession session, IoBuffer in,
			ProtocolDecoderOutput out) throws Exception {
		SOAPCommObject co = getSoapContext(session);
		CharsetDecoder cd = co.getCharsetDecoder();
		int totleLen = co.getTotalLen();
		int currLen = co.getCurrLen();
		//System.out.println("charset.name()_____"+charset.name());
		//System.out.println("charset.displayName()_____"+charset.displayName());
		
		//co.setCharset(charset.name());
		IoBuffer buffer = co.getInnerBuffer();
		
		while (in.hasRemaining()) {
			System.out.println("in.hasRemaining()");
			int iArrayLen = in.limit();
			byte[] bArray = new byte[iArrayLen];
				//in.get();
			in.get(bArray);
			totleLen+=iArrayLen;
			currLen+=iArrayLen;
			buffer.put(bArray);
			//buffer.position(totleLen);
			buffer.flip();
			String temStr = buffer.getString(cd);
			buffer.position(totleLen);
			System.out.println("totleLen"+":["+totleLen+"]  buffer:["+temStr+"]");
			
			int headPos = temStr.indexOf("\r\n\r\n");

			if(headPos!=-1 && !co.isHeadOkFlag())
			{
				//HTTP头已经收完
				//buffer.flip();
				co.setHead(temStr.substring(0,headPos+4));
				System.out.println("HTTP头：【"+co.getHead()+"】headPos【"+headPos+"】");
				currLen=totleLen-(headPos+4);
				//buffer.clear();
				co.setHeadOkFlag(true);
				co.setHeadLen(headPos+4);
				if (co.getHead().indexOf("Content-Length")!=-1)
				{
					String temp = co.getHead().substring(co.getHead().indexOf("Content-Length")+strContentLen+1);
					temp = temp.substring(0,temp.indexOf("\r\n"));
					
					co.setBodyLen(Integer.parseInt(temp.trim()));
					//System.out.println("HTTP头长度：【"+co.getHeadLen()+"】");
					//System.out.println("HTTP体长度：【"+co.getBodyLen()+"】");
					temp = null;
				}
				else
				{
					//只有报文头
					currLen=0;
					//
					//System.out.println("终于收完了，报文体：【"+co.getBody()+"】");
					
					SOAPCommObject nco = new SOAPCommObject();
					nco.setBody(co.getBody());
					nco.setHead(co.getHead());
					nco.setBodyLen(co.getBodyLen());
					nco.setHeadLen(co.getHeadLen());
					
					out.write(nco);
					nco=null;
					//System.out.println("co_send报文体内容:"+co_send.getBody());
					co.reset();
					
					co = null;
					cd = null;
					buffer.clear();
					buffer = null;
					//nco = null;
					bArray = null;
					return true;
				}
			}
			
			
			System.out.println("是否收完报文体totleLen：【"+totleLen+"】co.getHeadLen()["+co.getHeadLen()+"]["+co.getBodyLen()+"]");
			if (co.isHeadOkFlag() && co.getBodyLen()!=0 && totleLen==co.getHeadLen()+co.getBodyLen())
			{
				//报文收取完毕
				
//				System.out.println("buffer.position():["+buffer.position()+"]");
//				System.out.println("co.getBodyLen():["+co.getBodyLen()+"]");
				co.setBody(temStr.substring(co.getHeadLen()));
				currLen=0;
				//
				//System.out.println("终于收完了，报文体：【"+co.getBody()+"】");
				
				SOAPCommObject nco = new SOAPCommObject();
				nco.setBody(co.getBody());
				nco.setHead(co.getHead());
				nco.setBodyLen(co.getBodyLen());
				nco.setHeadLen(co.getHeadLen());
				
				out.write(nco);
				nco=null;
				//System.out.println("co_send报文体内容:"+co_send.getBody());
				co.reset();
				
				co = null;
				cd = null;
				buffer.clear();
				buffer = null;
				//nco = null;
				bArray = null;
				return true;
			}
			temStr = null;
			
			co.setTotalLen(totleLen);
			co.setCurrLen(currLen);
			bArray = null;
		}
		
		co = null;
		cd = null;
		buffer = null;
		
		System.out.println("doDecoder over");
		
		return false;
	}
/*

	
	private CommObject getContext(IoSession session) {
		CommObject context = (CommObject) session.getAttribute(CONTEXT);
		if (context == null) {
			context = new CommObject();
			session.setAttribute(CONTEXT, context);
		}
		return context;
	}

*/	
	private SOAPCommObject getSoapContext(IoSession session) {
		SOAPCommObject context = (SOAPCommObject) session.getAttribute(CONTEXT);
		if (context == null) {
			context = new SOAPCommObject();
			CharsetDecoder cd = charset.newDecoder();
			context.setCharsetDecoder(cd);
			session.setAttribute(CONTEXT, context);
		}
		return context;
	}
}
