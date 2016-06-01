package cn.com.adtec.comm.proxy.codec;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import cn.com.adtec.comm.proxy.bean.CommObject;
import cn.com.adtec.comm.proxy.server.AbstractSubServer;

public class SubFlowDecoder extends CumulativeProtocolDecoder {
	private final Charset charset;
	private final AttributeKey CONTEXT = new AttributeKey(getClass(), "context");
	private final AbstractSubServer subserver;
	
	

	public SubFlowDecoder(Charset charset,AbstractSubServer ass) {
		this.charset = charset;
		this.subserver = ass;
	}

	public AbstractSubServer getSubserver() {
		return subserver;
	}

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in,
			ProtocolDecoderOutput out) throws Exception {
		CommObject co = getContext(session);
		CharsetDecoder cd = charset.newDecoder();
		int totleLen = co.getTotalLen();
		int currLen = co.getCurrLen();
		
		IoBuffer buffer = co.getInnerBuffer();

		while (in.hasRemaining()) {
			byte b = in.get();
			totleLen++;
			currLen++;
			buffer.put(b);
			//System.out.println("TotleLen["+totleLen+"]PkgHeadLen()["+this.getSubserver().getPkgHeadLen()+"]");
			if (totleLen == this.getSubserver().getPkgHeadLen())
			{
				//处理报文头
				co.setHeadLen(totleLen);
				buffer.flip();
				co.setHead(buffer.getString(totleLen, cd));
				//System.out.println("HEAD:["+co.getHead()+"]");
				//取得报文体长度
				String temp = co.getHead().substring(this.getSubserver().getPkgLengthStartLoc(), this.getSubserver().getPkgLengthEndLoc());
				int iTemp = 0;
				//判断长度格式是十进制还是网络字节序格式
				//System.out.println("this.getSubserver().getPkgZipFlag():["+this.getSubserver().getPkgZipFlag()+"]");
				if (this.getSubserver().getPkgZipFlag().equals("0"))
				{
					//0-代表十进制格式,不压缩
					iTemp = Integer.parseInt(temp.trim());
				}
				else if (this.getSubserver().getPkgZipFlag().equals("1"))
				{
					//1-代表网络字节序,压缩，留待以后补上
					
				}
				//System.out.println("iTemp:["+iTemp+"]");
				if (this.getSubserver().getPkgIncludeSelf().equals("0"))
				{
					//0-表示不包括报文头自身长度，则什么也不做
				}
				else if (this.getSubserver().getPkgIncludeSelf().equals("1"))
				{
					//1-表示包括报文头自身长度,计算实际报文体长度时需要减去报文头长度
					if (iTemp>0)
					{
						iTemp = iTemp - co.getHeadLen();
					}
				}
				co.setBodyLen(iTemp);
				//System.out.println("setBodyLen:["+co.getBodyLen()+"]");
				currLen=0;
				buffer.clear();
				temp = null;
			}
			else if (totleLen == co.getHeadLen()+co.getBodyLen()){
					
						buffer.flip();
						co.setBody(buffer.getString(co.getBodyLen(), cd));
						//System.out.println("报文体内容:"+co.getBody());
						/*
						CommObject co_send = new CommObject();
						co_send.setBody(co.getBody());
						co_send.setHead(co.getHead());
						co_send.setBodyLen(co.getBodyLen());
						co_send.setHeadLen(co.getHeadLen());
						*/
						
						
						out.write(co);
						//System.out.println("co_send报文体内容:"+co_send.getBody());
						//co.reset();
						//co_send = null;
						//co = null;
						cd = null;
						buffer = null;
						return true;
			}
			
			co.setTotalLen(totleLen);
			co.setCurrLen(currLen);
				
			
		}
		
		
		co = null;
		cd = null;
		buffer = null;
		
		return false;
	}

	private CommObject getContext(IoSession session) {
		CommObject context = (CommObject) session.getAttribute(CONTEXT);
		if (context == null) {
			context = new CommObject();
			session.setAttribute(CONTEXT, context);
		}
		return context;
	}
}
