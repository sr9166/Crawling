package com.study.model;

/*
 * 최종 결과물이 될 페이스북 데이터
 * NAME : 페이지 이름
 * URL : 페이지 주소
 * NUMBER : 페이지 고유 아이디
 */
public class FacebookData {
	String name;
	String url;
	String number;
	public FacebookData(String name, String url, String number) {
		super();
		this.name = name;
		this.url = url;
		this.number = number;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
}
