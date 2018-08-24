package com.study.model;

/*
 * 최초 index뷰에서 입력받는 사용자 입력 데이터
 */
public class InputData {
	private String startdate;
	private String enddate; 
	private String facebook_id;
	private String facebook_pw;
	private String loopingnumber;
	private String maxcnt;
	
	public void display() {
		System.out.println("start date\t" + getStartdate());
		System.out.println("end date\t" + getEnddate());
		System.out.println("facebook_id\t" + getFacebook_id());
		System.out.println("facebook_pw\t" + getFacebook_pw());
		System.out.println("max count\t" + getMaxcnt());
	}

	public String getStartdate() {
		return startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public String getFacebook_id() {
		return facebook_id;
	}

	public void setFacebook_id(String facebook_id) {
		this.facebook_id = facebook_id;
	}

	public String getFacebook_pw() {
		return facebook_pw;
	}

	public void setFacebook_pw(String facebook_pw) {
		this.facebook_pw = facebook_pw;
	}

	public String getLoopingnumber() {
		return loopingnumber;
	}

	public void setLoopingnumber(String loopingnumber) {
		this.loopingnumber = loopingnumber;
	}

	public String getMaxcnt() {
		return maxcnt;
	}

	public void setMaxcnt(String maxcnt) {
		this.maxcnt = maxcnt;
	}
	
}
