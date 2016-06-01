package cn.com.adtec.comm.proxy.bean;

import java.nio.charset.CharsetDecoder;

import org.apache.mina.core.buffer.IoBuffer;


/**
 * 核心新监控报文数据
 * @author tangjb
 *
 */
public class NewMoniObject {
	private IoBuffer innerBuffer=IoBuffer.allocate(COMMLEN_LENGTH).setAutoExpand(true);
	private int totalLen;	//获取到的报文总长度;
	private int currLen;	//本次处理后的长度;
	CharsetDecoder charsetDecoder;		//编码方式
	
	//报文头类 字段长度
	public final static int COMMLEN_LENGTH=5;  //公共报文头
	//1+3+1
	

	//每个段标志
	private boolean pubHeadOkFlag = false; 	//公共头传输标志
	private boolean headOkFlag = false; //报文头传输标志,如果此部分传输完毕,此值为真
	private boolean bodyOkFlag = false;	//报文体传输标志,同上
	private int bodyLen;
	private int headLen;
	
	private StringBuilder packFlg=new StringBuilder();			//封包标签,长度36
	private StringBuilder leftTimeOut=new StringBuilder();		//交易剩余超时时间,长度6
	private StringBuilder MD5=new StringBuilder();				//dataLen+encptTyp+...)整个字符BUFFER的MD5值,长度32
	
	private	StringBuilder 		versionNo=new StringBuilder();		//通讯版本号
	private	StringBuilder 		pkgheadLen=new StringBuilder();			//包头长度
	private	StringBuilder		typ=new StringBuilder();				//报文类型
	
	private StringBuilder dataLen=new StringBuilder();			//数据域长度,长度6	十进制字符串（左补0）
	private StringBuilder encptTyp=new StringBuilder();		//加密方式,长度2,00 明文  01 3DES
	private StringBuilder txCd=new StringBuilder();			//服务码,长度15
	private StringBuilder fmtData=new StringBuilder();			//系统组好的格式化报文,长度
	private StringBuilder ctrlPkgBody=new StringBuilder();		//控制报文体
	public int getBodyLen() {
		return bodyLen;
	}


	public void setBodyLen(int bodyLen) {
		this.bodyLen = bodyLen;
	}


	public boolean isHeadOkFlag() {
		return headOkFlag;
	}
	public String getVersionNo()
	{
		return versionNo.toString();
	}
	public void setVersionNo(String vn)
	{
		this.versionNo.append(vn);
	}
	
	public String getPkgHeadLen()
	{
		return pkgheadLen.toString();
	}
	public void setPkgHeadLen(String hl)
	{
		this.pkgheadLen.append(hl);
	}
	public String getTyp()
	{
		return typ.toString();
	}
	public void setTyp(String ty)
	{
		this.typ.append(ty);
	}
	public String getPackFlg() {
		return packFlg.toString();
	}
	public void setPackFlg(String packFlg) {
		this.packFlg.append(packFlg);
	}
	public String getLeftTimeOut() {
		return leftTimeOut.toString();
	}
	public void setLeftTimeOut(String leftTimeOut) {
		this.leftTimeOut.append(leftTimeOut);
	}
	public String getMD5() {
		return MD5.toString();
	}
	public void setMD5(String md5) {
		MD5.append(md5);
	}
	
	public String getDataLen() {
		return dataLen.toString();
	}
	public void setDataLen(String dataLen) {
		this.dataLen.append(dataLen);
	}
	public String getEncptTyp() {
		return encptTyp.toString();
	}
	public void setEncptTyp(String encptTyp) {
		this.encptTyp.append(encptTyp);
	}
	public String getTxCd() {
		return txCd.toString();
	}
	public void setTxCd(String txCd) {
		this.txCd.append(txCd);
	}
	public String getFmtData() {
		return fmtData.toString();
	}
	public void setFmtData(String fmtData) {
		this.fmtData.append(fmtData);
	}
	
	public static final int PUB_VERSION_NO_LEN = 1;
	public static final int PUB_HEAD_LEN=3;
	public static final int PUB_TYPE_LEN = 1;
	public static final int DATA_HEAD_PACK_FLG_LEN=36;
	public static final int DATA_HEAD_LEFT_TIME_OUT_LEN=6;
	public static final int DATA_HEAD_MD5_LEN = 32;
	public static final int DATA_LEN=6;
	public static final int DATA_ENCPT_TYPE_LEN=2;
	public static final int DATA_TXCD_LEN=15;
	
	
	
	
	
	
	
	
	
	
	public NewMoniObject(){
		innerBuffer.clear();
		totalLen=0;
		currLen=0;	

		headOkFlag = false; 
		bodyLen = 0;
		bodyOkFlag = false;
		pubHeadOkFlag = false;

		//ctrlPkgBody = null;
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

	
	public void reset(){
		innerBuffer.clear();
		totalLen=0;
		currLen=0;	
		bodyLen = 0;
		pubHeadOkFlag = false;
		headOkFlag = false; 
	
		bodyOkFlag = false;
		
		packFlg.delete(0, packFlg.length());			//封包标签,长度36
		leftTimeOut.delete(0, leftTimeOut.length());		//交易剩余超时时间,长度6
		MD5.delete(0, MD5.length());				//dataLen+encptTyp+...)整个字符BUFFER的MD5值,长度32

		versionNo.delete(0, versionNo.length());		//通讯版本号
		pkgheadLen.delete(0, pkgheadLen.length());			//包头长度
		typ.delete(0, typ.length());				//报文类型

		dataLen.delete(0,dataLen.length());			//数据域长度,长度6	十进制字符串（左补0）
		encptTyp.delete(0, encptTyp.length());		//加密方式,长度2,00 明文  01 3DES
		txCd.delete(0, txCd.length());			//服务码,长度15
		fmtData.delete(0, fmtData.length());			//系统组好的格式化报文,长度
		ctrlPkgBody.delete(0, ctrlPkgBody.length());
	}


	public String getCtrlPkgBody() {
		return ctrlPkgBody.toString();
	}


	public void setCtrlPkgBody(String ctrlPkgBody) {
		this.ctrlPkgBody.append(ctrlPkgBody);
	}


	public void setInnerBuffer(IoBuffer innerBuffer) {
		this.innerBuffer = innerBuffer;
	}


	


	public boolean isPubHeadOkFlag() {
		return pubHeadOkFlag;
	}


	public void setPubHeadOkFlag(boolean pubHeadOkFlag) {
		this.pubHeadOkFlag = pubHeadOkFlag;
	}


	public int getHeadLen() {
		return headLen;
	}


	public void setHeadLen(int headLen) {
		this.headLen = headLen;
	}


	@Override
	public String toString() {
		if ("0".equals(this.getTyp().toString()))
		{
			//数据报文
			return this.getFmtData().toString();
		}
		else
		{
			return this.getCtrlPkgBody().toString();
		}
		//return super.toStringBuilder();
	}


	public CharsetDecoder getCharsetDecoder() {
		return charsetDecoder;
	}


	public void setCharsetDecoder(CharsetDecoder charsetDecoder) {
		this.charsetDecoder = charsetDecoder;
	}
	
}
