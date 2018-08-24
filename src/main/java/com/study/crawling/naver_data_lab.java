package com.study.crawling;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TreeSet;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.study.CrawlingApplication;
import com.study.model.InputData;

/*
 * 사용자 입력 데이터 바탕으로 과거의 네이버 실시간 검색어를 크롤링 하는 객체
 */
public class naver_data_lab extends Crawler {
	
	// naver data lab realtime searching keywords crawling
	public void crawling(InputData input) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		
		String startdate = input.getStartdate() + " 00:00:00";
		String enddate = input.getEnddate() + " 23:30:00";
		
		int loopingnumber = doCalLoop(startdate, enddate);		// 시작일자와 종료일자로부터 총 반복횟수를 구함
		input.setLoopingnumber(String.valueOf(loopingnumber));	// 사용자 입력 데이터에 반복횟수 정보 추가
		CrawlingApplication.loopingcnt = loopingnumber;
		
		System.out.println("=====================================================================");
		System.out.println("                    Naver Data Lab Crawling Start                    ");
		System.out.println("                    Total Loop Count is " + loopingnumber             );
		System.out.println("=====================================================================");
		
		// ============================================================= WebBrowser start
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getCookieManager().setCookiesEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());

		// ============================================================= 변수 초기화
		String url = "https://datalab.naver.com/keyword/realtimeList.naver?datetime=";
		String datetime = startdate;
		String date = startdate.split(" ")[0];
		String time = "T" + startdate.split(" ")[1];
		String callback_url="";
		HtmlPage page = null;
		ArrayList<String> returnArray = new ArrayList<>();
		
		// ============================================================= start loop
		for(int i = 0; i < loopingnumber; i++) {
			callback_url = url + date + time;
			System.out.println(callback_url);
			
			page = (HtmlPage) webClient.getPage(callback_url);
			Thread.sleep(1000);
			HtmlElement elem = (HtmlElement) page.getByXPath("//*[@class='rank_inner v2']").get(0);	// rank 가져오기
			
			for(int k = 0; k < 20; k++) {
				String str = elem.getElementsByTagName("span").get(k).getTextContent();				// rank 안의 실시간 검색어 크롤링 20번 반복
				returnArray.add(str);
			}
			
			String inTime = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
			System.out.println(i + " " + inTime + " " + datetime);
			datetime = doDateTimeAdd(datetime);		// 30분 더하기
			date = datetime.split(" ")[0];
			time = "T" + datetime.split(" ")[1];
			CrawlingApplication.navercnt = i + 1;
		}
		webClient.close();
		
		// Sort and remove overlap Naver Keywords
		TreeSet<String> treeSet = new TreeSet<String>(returnArray);
		returnArray = new ArrayList<String>(treeSet);
		CrawlingApplication.NaverKeywords = returnArray;
	}
	
	
	
	// DateTime = DateTime + 30 minute
	public static String doDateTimeAdd(String datetime) {
		SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		String date = datetime.split(" ")[0];
		String time = datetime.split(" ")[1];
		String year = date.split("-")[0];
		String month = date.split("-")[1];
		String day = date.split("-")[2];
		String hour = time.split(":")[0];
		String minute = time.split(":")[1];
		String second = time.split(":")[2];
		
		Calendar cal = Calendar.getInstance();
		cal.set(Integer.valueOf(year), 
				Integer.valueOf(month) -1, 
				Integer.valueOf(day), 
				Integer.valueOf(hour), 
				Integer.valueOf(minute), 
				Integer.valueOf(second));
		cal.add(Calendar.MINUTE, 30);
		
	    String strDate = fm.format(cal.getTime());
	    return strDate;
	}
	
	// Calculate difference between startdate and enddate
	public static int doCalLoop(String start_date, String end_date) {
		String date = start_date.split(" ")[0];
		String time = start_date.split(" ")[1];
		String year = date.split("-")[0];
		String month = date.split("-")[1];
		String day = date.split("-")[2];
		String hour = time.split(":")[0];
		String minute = time.split(":")[1];
		String second = time.split(":")[2];
		
		Calendar cal = Calendar.getInstance();
		cal.set(Integer.valueOf(year), 
				Integer.valueOf(month) -1, 
				Integer.valueOf(day), 
				Integer.valueOf(hour), 
				Integer.valueOf(minute), 
				Integer.valueOf(second));
		
		date = end_date.split(" ")[0];
		time = end_date.split(" ")[1];
		year = date.split("-")[0];
		month = date.split("-")[1];
		day = date.split("-")[2];
		hour = time.split(":")[0];
		minute = time.split(":")[1];
		second = time.split(":")[2];
		
		Calendar endcal = Calendar.getInstance();
		endcal.set(Integer.valueOf(year), 
				Integer.valueOf(month) -1, 
				Integer.valueOf(day), 
				Integer.valueOf(hour), 
				Integer.valueOf(minute), 
				Integer.valueOf(second));
		
		if(endcal.compareTo(Calendar.getInstance()) == 1) {
			endcal = Calendar.getInstance();
			endcal.set(Calendar.MINUTE, 0);
			endcal.set(Calendar.SECOND, 0);
		}
		
		for(int i = 0; ; i++) {
			if(cal.compareTo(endcal) == 1)
				return i;
			cal.add(Calendar.MINUTE, 30);
		}
	}
}
