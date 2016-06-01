package cn.com.adtec.comm.proxy.bean;


import java.nio.charset.CharsetDecoder;

import org.apache.mina.core.buffer.IoBuffer;



public class SOAPCommObject {
	private static int SOAPHEADLEN=50;
	private IoBuffer innerBuffer=IoBuffer.allocate(SOAPHEADLEN,false).setAutoExpand(true);
	private int totalLen;	//获取到的报文总长度;
	private int currLen;	//本次处理后的长度;
	private int headLen;	//报文头长度
	private int bodyLen;	//报文体长度
	private String charset;	//字符集
	

	private CharsetDecoder charsetDecoder;


	//每个段标志
	private boolean headOkFlag = false; //报文头传输标志,如果此部分传输完毕,此值为真
	private boolean bodyOkFlag = false;	//报文头传输标志,同上
	
	private StringBuilder head=new StringBuilder();
	private StringBuilder body=new StringBuilder();
	
	public SOAPCommObject(){
		innerBuffer.clear();
		totalLen=0;
		currLen=0;	
		headLen=0;	
		bodyLen=0;	
		headOkFlag = false; 
		bodyOkFlag = false;
		//head = "";
		//body = "";
	}

	public int getHeadLen() {
		return headLen;
	}

	public void setHeadLen(int headLen) {
		this.headLen = headLen;
	}

	public String getHead() {
		return head.toString();
	}

	public void setHead(String head) {
		this.head.append(head);
	}

	public void setInnerBuffer(IoBuffer innerBuffer) {
		this.innerBuffer = innerBuffer;
	}

	public String getBody() {
		return body.toString();
	}
	public void setBody(String body) {
		this.body.append(body);
	}
	public boolean isHeadOkFlag() {
		return headOkFlag;
	}
	public void setHeadOkFlag(boolean headOkFlag) {
		this.headOkFlag = headOkFlag;
	}

	public boolean isBodyOkFlag() {
		return bodyOkFlag;
	}
	public void setBodyOkFlag(boolean bodyOkFlag) {
		this.bodyOkFlag = bodyOkFlag;
	}
	public IoBuffer getInnerBuffer() {
		return innerBuffer;
	}
	public int getTotalLen() {
		return totalLen;
	}
	public void setTotalLen(int totalLen) {
		this.totalLen = totalLen;
	}
	public int getCurrLen() {
		return currLen;
	}
	public void setCurrLen(int currLen) {
		this.currLen = currLen;
	}

	public int getBodyLen() {
		return bodyLen;
	}
	public void setBodyLen(int bodyLen) {
		this.bodyLen = bodyLen;
	}
	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}
	public void reset(){
		innerBuffer.clear();
		totalLen=0;
		currLen=0;	
		headLen=0;	
		bodyLen=0;	
		headOkFlag = false; 
		bodyOkFlag = false;
		head.delete(0, head.length());
		body.delete(0, body.length());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getHead());
		sb.append(this.getBody());
		
		return sb.toString();
		
	}

	public CharsetDecoder getCharsetDecoder() {
		return charsetDecoder;
	}

	public void setCharsetDecoder(CharsetDecoder charsetDecoder) {
		this.charsetDecoder = charsetDecoder;
	}
	
	
}
