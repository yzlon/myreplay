package cn.com.adtec.comm.proxy.bean;

import org.apache.mina.core.buffer.IoBuffer;



public class CommObject {
	private IoBuffer innerBuffer=IoBuffer.allocate(PkgHeadLength).setAutoExpand(true);
	private int totalLen;	//获取到的报文总长度;
	private int currLen;	//本次处理后的长度;
	private int bodyLen;	//报文体长度
	private int headLen;	//报文头长度

	
	public int getHeadLen() {
		return headLen;
	}


	public void setHeadLen(int headLen) {
		this.headLen = headLen;
	}

	//报文头类 字段长度
	public final static int COMMLEN_LENGTH=2;
		//8;			//字段长度
/*	public final static int OUTIPSTR_LENGTH=20;
	public final static int OUTPORT_LENGTH=5;			
	public final static int OTIMEOUT_LENGTH=5;			
	public final static int COMMNAME_LENGTH=20;			
	public final static int COMMTYPE_LENGTH=20;	
	public final static int COMPNAME_LENGTH=50;
	public final static int CODEIN_LENGTH=20;				
	public final static int CODEOUT_LENGTH=20;			
	public final static int EXTLEN_LENGTH=8;
	
	//扩展类 字段长度
	public final static int PARASN_LENGTH=8;
	public final static int PARANAME_LENGTH=20;
	public final static int INITVALUE_LENGTH=50;
*/	
	//报文头总长度 
	public final static int PkgHeadLength=COMMLEN_LENGTH
/*									+OUTIPSTR_LENGTH
									+OUTPORT_LENGTH
									+OTIMEOUT_LENGTH
									+COMMNAME_LENGTH
									+COMPNAME_LENGTH
									+COMMTYPE_LENGTH
									+CODEIN_LENGTH
									+CODEOUT_LENGTH
									+EXTLEN_LENGTH*/
									;
/*	

	*/
	//每个段标志
	private boolean headOkFlag = false; //报文头传输标志,如果此部分传输完毕,此值为真

	public String getHead() {
		return head;
	}


	public void setHead(String head) {
		this.head = head;
	}

	private boolean bodyOkFlag = false;	//报文体传输标志,同上
	
	private String head;
	private String body;
	
	public CommObject(){
		innerBuffer.clear();
		totalLen=0;
		currLen=0;	

		bodyLen=0;	
		headOkFlag = false; 

		bodyOkFlag = false;
		head = "";
		body = "";
	}
	

	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
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
	
	public void reset(){
		innerBuffer.clear();
		totalLen=0;
		currLen=0;	
	
		bodyLen=0;	
		headOkFlag = false; 

		bodyOkFlag = false;

		head = "";
		body = "";
	}


	@Override
	public String toString() {
		
		return this.getHead()+this.getBody();
	}
	
}
