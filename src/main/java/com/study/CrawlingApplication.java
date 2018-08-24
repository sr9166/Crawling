package com.study;

import java.util.ArrayList;
import java.util.Comparator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import com.study.crawling.Crawler;
import com.study.crawling.facebook_fanpage;
import com.study.crawling.filtering;
import com.study.crawling.naver_data_lab;
import com.study.model.FacebookData;

@EnableAsync
@SpringBootApplication
public class CrawlingApplication {
	/*
	 * Static Variables
	 * 
	 * NaverKeywords 네이버 키워드 크롤링 리스트
	 * Fanpages 네이버 키워드를 바탕으로 페이스북 팬 페이지 검색 결과 크롤링 리스트
	 * Filtered_Fanpages 검색한 페이지를 바탕으로 필터링(한국계정만)한 크롤링 리스트
	 * crawler 실제 크롤링 하는 객체들의 리스트
	 * loopingcnt 전체 네이버 크롤링 할 개수
	 * navercnt 현재 네이버 크롤링 한 개수
	 * fanpagecnt 현재 페이스북 팬 페이지 검색결과 크롤링 한 개수
	 * filteringcnt 현재 필터링 한 팬 페이지 개수
	 */
	public static ArrayList<String> NaverKeywords = new ArrayList<>();
	public static ArrayList<FacebookData> Fanpages = new ArrayList<>();
	public static ArrayList<FacebookData> Filtered_Fanpages = new ArrayList<>();
	public static ArrayList<Crawler> crawler = new ArrayList<>();
	public static int loopingcnt = 0;
	public static int navercnt = 0;
	public static int fanpagecnt = 0;
	public static int filteringcnt = 0;
	
	/*
	 * 생성자
	 * 
	 * crawler에 크롤링 할 객체들 추가
	 */
	public CrawlingApplication() {
		crawler.add(new naver_data_lab());
		crawler.add(new facebook_fanpage());
		crawler.add(new filtering());
	}
	
	/*
	 * main 함수
	 * 
	 * 스프링 어플리케이션 실행
	 */
	public static void main(String[] args) {
		SpringApplication.run(CrawlingApplication.class, args);
	}
	
	/*
	 * 중복제거 함수
	 * 
	 * 페이스북 팬 페이지 검색결과를 크롤링한 리스트인 Fanpages안의 FacebookData를 중복제거하는데 사용
	 * FacebookData's primarykey : number(각 페이지마다 할당된 고유 숫자)
	 */
	public static void OverlapRemove(ArrayList<FacebookData> arr) {
		for(int i = 0; i < arr.size() - 1; i++) {
			if(arr.get(i).getNumber().equals(arr.get(i+1).getNumber())) {
				arr.remove(i+1);
				i--;
			}
		}
	}
	
	/*
	 * 각 크롤링 대상의 리스트들과 카운트를 초기화
	 */
	public static void Initailize() {
		NaverKeywords.clear();
		Fanpages.clear();
		Filtered_Fanpages.clear();
		navercnt = 0;
		fanpagecnt = 0;
		filteringcnt = 0;
	}
	
	/*
	 * Sort할때 사용하는 함수 Ascending을 호출해서 사용한다.
	 */
	public static Comparator<FacebookData> getCmp() {
		return new Ascending();
	}
	
	/*
	 * FacebookData를 정렬할때 호출되는 함수
	 * primarykey인 number를 대상으로 정렬한다
	 */
	static class Ascending implements Comparator<FacebookData> {
		 
	    @Override
	    public int compare(FacebookData o1, FacebookData o2) {
	        return o1.getNumber().compareTo(o2.getNumber());
	    }
	}
}

