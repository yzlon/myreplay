package cn.com.adtec.comm.proxy.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/**
 * 日期/时间应用
 * @author chenlong
 *
 */


public class SystemTime {
	Calendar t;
	int y, m, d, hh, mm, ss, ms,m0;
	public SystemTime() {
		t = Calendar.getInstance();
		y = t.get(Calendar.YEAR);
		m = t.get(Calendar.MONTH) + 1;
		m0 = t.get(Calendar.MONTH);
		d = t.get(Calendar.DATE);
		hh = t.get(Calendar.HOUR_OF_DAY);
		mm = t.get(Calendar.MINUTE);
		ss = t.get(Calendar.SECOND);
		ms = t.get(Calendar.MILLISECOND);
	}

	public String getChinaDate() {
		StringBuffer sb = new StringBuffer(add0(y));
		return sb.append("年").append(add0(m)).append("月").append(add0(d))
				.append("日").toString();
	}
	// yyyy-mm-dd
	public String getDate() {
		StringBuffer sb = new StringBuffer(add0(y));
		return sb.append("-").append(add0(m)).append("-").append(add0(d))
				.toString();

	}
	// yyyy-mm-dd
	public String getDate(String split) {
		StringBuffer sb = new StringBuffer(add0(y));
		return sb.append(split).append(add0(m)).append(split).append(add0(d))
				.toString();

	}
	// yyyymmdd
	public String getDateStr() {
		StringBuffer sb = new StringBuffer(add0(y));

		return sb.append(add0(m)).append(add0(d)).toString();

	}
	public String getDateStr(int i) {
		if (m + i > 12) {
			y = y + (m + i) / 12;
			m = (m + i) % 12;
		} else {
			m = m + i;
		}
		StringBuffer sb = new StringBuffer(add0(y));

		return sb.append(add0(m)).append(add0(d)).toString();
	}
	// yymmdd
	public String getDateStryymmdd() {
		StringBuffer sb = new StringBuffer(add0(y).substring(2,
				add0(y).length()));
		return sb.append(add0(m)).append(add0(d)).toString();

	}
	public String getTimeStrhhmmss() {
		StringBuffer sb = new StringBuffer(add0(hh));
		return sb.append(add0(mm)).append(add0(ss)).toString();
	}

	public String getChinaTime() {
		StringBuffer sb = new StringBuffer(add0(hh));
		return sb.append("小时").append(mm).append("分").append(ss).append("秒")
				.toString();

	}

	public String add0(int parm) {

		String parmout = null;
		if (parm >= 0 && parm < 10) {
			parmout = "0" + String.valueOf(parm);
			return parmout;
		} else {
			return String.valueOf(parm);
		}
	}
	
	public String add1(int parm) {

		String parmout = null;
		if (parm >= 0 && parm < 10) {
			parmout = "00" + String.valueOf(parm);
			return parmout;
		}else if (parm >= 10 && parm < 100) {
			parmout = "0" + String.valueOf(parm);
			return parmout;
		}  
		else {
			return String.valueOf(parm);
		}
	}

	public String getTime() {
		StringBuffer sb = new StringBuffer(add0(hh));
		return sb.append(":").append(add0(mm)).append(":").append(add0(ss))
				.toString();
	}

	public String getRigorTime() {
		StringBuffer sb = new StringBuffer(add0(hh));
		return sb.append(add0(mm)).append(add0(ss)).append(add1(ms)).toString();
	}

	public Date getdtime(String dtimestr) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = sdf.parse(dtimestr.substring(0, 10));

			// System.out.println(date.toString());
		} catch (ParseException ex) {
		}
		return date;
	}
	
	/**
	 * YYYYMMDD HH:MM:SS
	 * @return
	 *///fzg 20100806 add
	public String getDateTime()
	{
		StringBuffer sb = new StringBuffer(add0(y));

		String date = sb.append(add0(m)).append(add0(d)).toString();
		StringBuffer sb2 = new StringBuffer(add0(hh));
		String time = sb2.append(":").append(add0(mm)).append(":").append(add0(ss))
				.toString();
		return date + " " + time;
	}

}
