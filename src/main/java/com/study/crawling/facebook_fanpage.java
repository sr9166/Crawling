package com.study.crawling;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.study.CrawlingApplication;
import com.study.model.FacebookData;
import com.study.model.InputData;

/*
 * 네이버 키워드 검색결과를 바탕으로 페이스북 팬 페이지 키워드 검색결과를 크롤링 하는 객체
 */
public class facebook_fanpage extends Crawler {
	
	// by naver keywords, facebook fan pages crawling with overlap
	public void crawling(InputData input) throws InterruptedException, URISyntaxException, IOException {
		// TODO Auto-generated method stub
		ArrayList<String> keywordList = CrawlingApplication.NaverKeywords;		// 네이버 실시간 검색어 크롤링 결과를 받아옴
		String facebook_id = input.getFacebook_id();		// 사용자 입력 데이터인 페이스북 아이디
		String facebook_pw = input.getFacebook_pw();		// 사용자 입력 데이터인 페이스북 비밀번호
		
		System.out.println("=====================================================================");
		System.out.println("                    Facebook Pan-Page Crawling Start                 ");
		System.out.println("                    Total Loop Count is " + keywordList.size()        );
		System.out.println("=====================================================================");		

		// ============================================================= WebBrowser start
		System.setProperty("webdriver.chrome.driver", getDriverPath());
		ChromeOptions options = new ChromeOptions();
		options.addArguments("headless");		// headless 속성 설정
		WebDriver driver = new ChromeDriver(options);
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		
		// ============================================================= Login
		try {
			driver.get("https://www.facebook.com");
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		driver.findElements(By.xpath("//*[@class='inputtext']")).get(0).sendKeys(facebook_id);		// 페이스북 아이디 입력
		driver.findElements(By.xpath("//*[@class='inputtext']")).get(1).sendKeys(facebook_pw);		// 페이스북 비밀번호 입력
		driver.findElement(By.id("loginbutton")).click();											// 로그인 버튼 클릭
		
		// ============================================================= Login check
		if(driver.findElements(By.className("login_form_container")).size() != 0) {		// 로그인이 실패할 경우
			CrawlingApplication.fanpagecnt = -1;
			CrawlingApplication.filteringcnt = -1;
			return;
		}

		// ============================================================= Search by keywordList
		String url = "";
		int cnt = -1;
		int lack_cnt = 0;
		String name = "";
		String URL = "";
		String id = "";
		List<WebElement> elList = null;
		ArrayList<FacebookData> returnArray = new ArrayList<>();
		for (int i = 0; i < keywordList.size(); i++) {
			url = String.format("https://www.facebook.com/search/str/%s/keywords_pages", keywordList.get(i));
			System.out.println(url);
			try {
				driver.get(url);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			// ============================================================= Drag FanPages
			cnt = -1;		// 드래그 하기 전 페이스북 검색결과 리스트 개수
			lack_cnt = 0;	// 끝까지 드래그 했는지 알아보기 위해 설정한 임시 변수
			
			while (true) {
				elList = driver.findElements(By.xpath("//*[@class='_3u1 _gli _6pe1']"));		// 페이스북 검색결과 리스트
				if (elList.size() == 0) {	// 검색결과가 없으면 drag 안함
					break;
				}
				
				if (cnt == elList.size()) {		// 드래그를 해도 이전과 페이지 개수가 같은경우
					lack_cnt++;					// lack_cnt를 증가시키고
					if (lack_cnt > 100)			// 100 번 반복된 경우 더이상 페이지가 없다라고 판단하고
						break;					// break
					else
						continue;
				}
				
				if(!input.getMaxcnt().equals("Infinite") && elList.size() > Integer.valueOf(input.getMaxcnt())) {		// MaxCount가 Infinite가 아니고 검색결과 페이지 개수가 MaxCount를 넘은경우 
					break;
				}
				
				lack_cnt = 0;				// lack_cnt 다시 0으로 초기화
				cnt = elList.size();		// cnt를 현재 elList.size으로 초기화
				WebElement lastElem = elList.get(elList.size() - 1);		// 페이스북 검색결과 리스트의 마지막 element
				jse.executeScript("arguments[0].scrollIntoView();", lastElem);	// lastElem을 뷰에 보이게 스크롤 즉, Scroll Down to Bottom
				Thread.sleep(500);
			}

			// ============================================================= DataWrite
			for (WebElement el : elList) {	// 검색된 페이스북 팬 페이지 검색결과 만큼 반복
				WebElement temp = el.findElement(By.className("_32mo"));
				name = temp.getText();
				URL = URLDecoder.decode(temp.getAttribute("href"), "utf-8");
				URL = URL.split("\\?")[0];
				id = el.getAttribute("data-bt");
				id = id.substring(6);
				id = id.split(",")[0];
				returnArray.add(new FacebookData(name, URL, id)); // name URL id를 FacebookData객체로 returnArray에 저장
			}
			String inTime = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
			System.out.println(i + "\t" + inTime);
			CrawlingApplication.fanpagecnt = i + 1;
		}
		driver.close();
		
		// 팬페이지 number를 기준으로 정렬
		Collections.sort(returnArray, CrawlingApplication.getCmp());
		
		// 팬페이지 중복제거
		CrawlingApplication.OverlapRemove(returnArray);
		
		CrawlingApplication.Fanpages = returnArray;
	}
}
