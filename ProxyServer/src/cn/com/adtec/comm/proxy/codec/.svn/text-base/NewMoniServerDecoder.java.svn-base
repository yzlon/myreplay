package cn.com.adtec.comm.proxy.codec;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;


import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import cn.com.adtec.comm.proxy.bean.NewMoniObject;



public class NewMoniServerDecoder extends CumulativeProtocolDecoder {
	private final Charset charset;
	private final AttributeKey CONTEXT = new AttributeKey(getClass(), "context");
	
	
	

	public NewMoniServerDecoder(Charset charset) {
		this.charset = charset;
	}

	@Override
	/**
	 * 获取请求/响应报文
	 */
	protected boolean doDecode(IoSession session, IoBuffer in,
			ProtocolDecoderOutput out) throws Exception {
		
		NewMoniObject co = getContext(session);
		CharsetDecoder cd = co.getCharsetDecoder();
		int totleLen = co.getTotalLen();
		int currLen = co.getCurrLen();
		
		IoBuffer buffer = co.getInnerBuffer();
		
		while (in.hasRemaining()) {
			
			int thisLimit = in.limit();
			//System.out.println("解码开始,可读取长度：【"+thisLimit+"】totleLen["+totleLen+"]currLen["+currLen+"]");
			byte[] b = new byte[thisLimit];
			//System.out.print(Integer.toHexString(b));
			//System.out.print(" ");
			in.get(b);
			buffer.position(totleLen);
			totleLen+=thisLimit;
			currLen+=thisLimit;
			buffer.put(b);
			if(!co.isPubHeadOkFlag()&& totleLen>=NewMoniObject.COMMLEN_LENGTH )
			{
				//公共头接收
				//1.版本号
				buffer.position(totleLen-currLen);
				co.setVersionNo(buffer.getString(NewMoniObject.PUB_VERSION_NO_LEN,cd));
				
				//2.报文头长度
				buffer.position(totleLen-currLen+NewMoniObject.PUB_VERSION_NO_LEN);
				co.setPkgHeadLen(buffer.getString(NewMoniObject.PUB_HEAD_LEN,cd));
				co.setHeadLen(Integer.parseInt(co.getPkgHeadLen())-NewMoniObject.PUB_TYPE_LEN);
				//System.out.println("报文头长度：【"+co.getHeadLen()+"】");
				
				//3.数据类型
				buffer.position(totleLen-currLen+NewMoniObject.PUB_VERSION_NO_LEN+NewMoniObject.PUB_HEAD_LEN);
				co.setTyp(buffer.getString(NewMoniObject.PUB_TYPE_LEN,cd));	
				//System.out.println("数据类型：【"+co.getTyp()+"】");
				co.setPubHeadOkFlag(true);     //公共报文头收取完毕	
				currLen = 0;
				
				buffer.position(totleLen);
				/*co.setTotalLen(totleLen);
				co.setCurrLen(currLen);
				return false;*/
		}//if(totleLen<NewMoniObject.COMMLEN_LENGTH)
		if (co.isPubHeadOkFlag() )
		{
			
			//收报文体，先根据类别分别拆解
			if (co.getTyp().equals("1"))
			{
				//该报文为控制报文，后续报文体的长度就在公共头的三字节HEADLEN里，注意要减去本字段（类型）长度
				if (totleLen==NewMoniObject.COMMLEN_LENGTH+co.getHeadLen())
				{
					buffer.position(totleLen-currLen);
					co.setCtrlPkgBody(buffer.getString(currLen,cd));
					buffer.position(totleLen);
					currLen = 0;
					co.setBodyOkFlag(true);
					buffer.flip();
					
					NewMoniObject newObj = new NewMoniObject();
					newObj.setDataLen(co.getDataLen());
					newObj.setEncptTyp(co.getEncptTyp());
					newObj.setFmtData(co.getFmtData());
					newObj.setTxCd(co.getTxCd());
					
					newObj.setBodyLen(co.getBodyLen());
					newObj.setCtrlPkgBody(co.getCtrlPkgBody());
					
					newObj.setLeftTimeOut(co.getLeftTimeOut());
					newObj.setMD5(co.getMD5());
					newObj.setPackFlg(co.getPackFlg());
										
					newObj.setHeadLen(co.getHeadLen());
					newObj.getInnerBuffer().put(co.getInnerBuffer());
					newObj.setVersionNo(co.getVersionNo());
					newObj.setPkgHeadLen(co.getPkgHeadLen());
					newObj.setTyp(co.getTyp());
					newObj.setTotalLen(co.getTotalLen());
					
					//System.out.println("在解码请求报文时获取的整个报文：【"+newObj.getInnerBuffer().getString(totleLen, cd)+"】长度【"+newObj.getInnerBuffer().array().length+"】");
					out.write(newObj);
					
					newObj=null;
					//out.write(co);
					co.reset();
					co = null;
					buffer.clear();
					cd = null;
					buffer = null;
					return true;
				}
			}//if (co.getPubhead().getTyp().equals("1"))
			else if (co.getTyp().equals("0"))
			{
				//数据报文，真正的业务类报文
				//先收数据报文头
				if(!co.isHeadOkFlag() && totleLen>=NewMoniObject.COMMLEN_LENGTH + co.getHeadLen())
				{
					//4.收取：封包标签
					buffer.position(NewMoniObject.COMMLEN_LENGTH);
					co.setPackFlg(buffer.getString(NewMoniObject.DATA_HEAD_PACK_FLG_LEN,cd));
					//System.out.println("封包标签：【"+co.getPackFlg()+"】");
					
					//5.收取：剩余时间
					buffer.position(NewMoniObject.COMMLEN_LENGTH+NewMoniObject.DATA_HEAD_PACK_FLG_LEN);
					co.setLeftTimeOut(buffer.getString(NewMoniObject.DATA_HEAD_LEFT_TIME_OUT_LEN,cd));
					//System.out.println("剩余时间：【"+co.getLeftTimeOut()+"】");
					//6.收取：MD5码
					buffer.position(NewMoniObject.COMMLEN_LENGTH+NewMoniObject.DATA_HEAD_PACK_FLG_LEN+NewMoniObject.DATA_HEAD_LEFT_TIME_OUT_LEN);
					co.setMD5(buffer.getString(NewMoniObject.DATA_HEAD_MD5_LEN,cd));
					//System.out.println("MD5码：【"+co.getMD5()+"】");
					
					co.setHeadOkFlag(true);
					buffer.position(NewMoniObject.COMMLEN_LENGTH+NewMoniObject.DATA_HEAD_PACK_FLG_LEN+NewMoniObject.DATA_HEAD_LEFT_TIME_OUT_LEN+NewMoniObject.DATA_HEAD_MD5_LEN);
				}//if(totleLen>=NewMoniObject.COMMLEN_LENGTH + co.getHeadLen())
				if(co.isHeadOkFlag() && totleLen>=(NewMoniObject.COMMLEN_LENGTH+NewMoniObject.DATA_HEAD_PACK_FLG_LEN+NewMoniObject.DATA_HEAD_LEFT_TIME_OUT_LEN+NewMoniObject.DATA_HEAD_MD5_LEN+NewMoniObject.DATA_LEN+NewMoniObject.DATA_ENCPT_TYPE_LEN+NewMoniObject.DATA_TXCD_LEN) )
				{
					//该收取数据报文体了
					if (co.getBodyLen()==0)
					{
						//7.收取：报文体长度
						buffer.position(NewMoniObject.COMMLEN_LENGTH+NewMoniObject.DATA_HEAD_PACK_FLG_LEN+NewMoniObject.DATA_HEAD_LEFT_TIME_OUT_LEN+NewMoniObject.DATA_HEAD_MD5_LEN);
						co.setDataLen(buffer.getString(NewMoniObject.DATA_LEN,cd));	//报文长度
						co.setBodyLen(Integer.parseInt(co.getDataLen())-NewMoniObject.DATA_ENCPT_TYPE_LEN-NewMoniObject.DATA_TXCD_LEN);
						//System.out.println("报文体长度：【"+co.getBodyLen()+"】");
						
						//8.收取：加密方式
						buffer.position(NewMoniObject.COMMLEN_LENGTH+NewMoniObject.DATA_HEAD_PACK_FLG_LEN+NewMoniObject.DATA_HEAD_LEFT_TIME_OUT_LEN+NewMoniObject.DATA_HEAD_MD5_LEN
								+NewMoniObject.DATA_LEN);
						co.setEncptTyp(buffer.getString(NewMoniObject.DATA_ENCPT_TYPE_LEN,cd));	
						//System.out.println("加密方式：【"+co.getEncptTyp()+"】");
						
						//9.收取：服务码
						buffer.position(NewMoniObject.COMMLEN_LENGTH+NewMoniObject.DATA_HEAD_PACK_FLG_LEN+NewMoniObject.DATA_HEAD_LEFT_TIME_OUT_LEN+NewMoniObject.DATA_HEAD_MD5_LEN
								+NewMoniObject.DATA_LEN+NewMoniObject.DATA_ENCPT_TYPE_LEN);
						co.setTxCd(buffer.getString(NewMoniObject.DATA_TXCD_LEN,cd)); 
						
						//System.out.println("服务码：【"+co.getTxCd()+"】");
						buffer.position(NewMoniObject.COMMLEN_LENGTH+NewMoniObject.DATA_HEAD_PACK_FLG_LEN+NewMoniObject.DATA_HEAD_LEFT_TIME_OUT_LEN+NewMoniObject.DATA_HEAD_MD5_LEN
								+NewMoniObject.DATA_LEN+NewMoniObject.DATA_ENCPT_TYPE_LEN+NewMoniObject.DATA_TXCD_LEN);
					}
					if (co.isHeadOkFlag() && totleLen==NewMoniObject.PUB_VERSION_NO_LEN+NewMoniObject.PUB_HEAD_LEN+NewMoniObject.PUB_TYPE_LEN
							+NewMoniObject.DATA_HEAD_PACK_FLG_LEN+NewMoniObject.DATA_HEAD_LEFT_TIME_OUT_LEN
							+NewMoniObject.DATA_HEAD_MD5_LEN+NewMoniObject.DATA_LEN+NewMoniObject.DATA_ENCPT_TYPE_LEN
							+NewMoniObject.DATA_TXCD_LEN+co.getBodyLen() && co.getBodyLen()!=0)
					{
						buffer.position(totleLen-co.getBodyLen());
						co.setFmtData(buffer.getString(co.getBodyLen(),cd));	//业务报文
						buffer.position(totleLen);
						currLen = 0;
						co.setBodyOkFlag(true);
						buffer.flip();
						//System.out.println("业务报文：【"+co.getFmtData()+"】");
						
						NewMoniObject newObj = new NewMoniObject();
						newObj.setDataLen(co.getDataLen());
						newObj.setEncptTyp(co.getEncptTyp());
						newObj.setFmtData(co.getFmtData());
						newObj.setTxCd(co.getTxCd());
						
						newObj.setBodyLen(co.getBodyLen());
						newObj.setCtrlPkgBody(co.getCtrlPkgBody());
						
						newObj.setLeftTimeOut(co.getLeftTimeOut());
						newObj.setMD5(co.getMD5());
						newObj.setPackFlg(co.getPackFlg());
											
						newObj.setHeadLen(co.getHeadLen());
						newObj.getInnerBuffer().put(co.getInnerBuffer());
						newObj.setVersionNo(co.getVersionNo());
						newObj.setPkgHeadLen(co.getPkgHeadLen());
						newObj.setTyp(co.getTyp());
						newObj.setTotalLen(co.getTotalLen());
						out.write(newObj);
						newObj=null;
						//out.write(co);
						co.reset();
						buffer.clear();
						cd = null;
						buffer = null;
						co = null;
						return true;
					}//取完整个报文了
				}
				
				
			}//else if (co.getPubhead().getTyp().equals("0"))
			else
			{
				//不支持的报文种类
			}

			
			//return true;
		}//if (co.isPubHeadOkFlag() )
		co.setTotalLen(totleLen);
		co.setCurrLen(currLen);

	}//while (in.hasRemaining())
		
	co = null;
	cd = null;
	//
	
	
	return false;
        
}

	private NewMoniObject getContext(IoSession session) {
		NewMoniObject context = (NewMoniObject) session.getAttribute(CONTEXT);
		if (context == null) {
			context = new NewMoniObject();
			CharsetDecoder cd = charset.newDecoder();
			context.setCharsetDecoder(cd);
			session.setAttribute(CONTEXT, context);
		}
		return context;
	}
}
