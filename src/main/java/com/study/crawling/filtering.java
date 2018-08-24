package com.study.crawling;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
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
 * 페이스북 검색결과 팬 페이지를 바탕으로 유효한 계정인지 게시글을 크롤링하여 판단하는 객체
 */
public class filtering extends Crawler {
	
	/*
	 * Static Variables
	 * 
	 * HANGUL_UNICODE_START	한글 유니코드의 시작 : '가'
	 * HANGUL_UNICODE_END	한글 유니코드의 끝 : '힣'
	 * Possibility			한글의 비율이 Possibility 이상이어야 유효하다고 판단
	 */
	final static int HANGUL_UNICODE_START = 0xAC00;
	final static int HANGUL_UNICODE_END = 0xD7AF;
	final static double Possibility = 0.6d;

	// check korean accounts by their posts' language
	public void crawling(InputData input) throws IOException, InterruptedException, URISyntaxException { 
		String facebook_id = input.getFacebook_id();
		String facebook_pw = input.getFacebook_pw();
		
		// TODO Auto-generated method stub
		// ============================================================= File read
		ArrayList<FacebookData> facebook_panpage = CrawlingApplication.Fanpages;
		
		System.out.println("=====================================================================");
		System.out.println("                    Filerting Process Start                          ");
		System.out.println("                    Total Loop Count is " + facebook_panpage.size()   );
		System.out.println("=====================================================================");

		// ============================================================= WEB Browsing start
		System.setProperty("webdriver.chrome.driver", getDriverPath());
		ChromeOptions options = new ChromeOptions();
		options.addArguments("headless");
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

		// ============================================================= Algorithm repeat
		String name = "";
		String url = "";
		String page_url = "";
		String number = "";
		String str = "";
		for (int i = 0; i < facebook_panpage.size(); i++) {
			name = facebook_panpage.get(i).getName();
			url = facebook_panpage.get(i).getUrl();
			number = facebook_panpage.get(i).getNumber();
			page_url = "https://www.facebook.com/pg/" + number + "/posts";
			
			// =============================================================
			try {
				driver.get(page_url);

				List<WebElement> el = driver
						.findElements(By.xpath("//*[@class='_4-u2 _4-u8']//*[@class='_5pbx userContent _3576']//p"));		// 유저 컨텐츠 크롤링
				List<WebElement> elHashTag = driver.findElements(By.xpath("//*[@class='_58cn']"));							// 해쉬태그 크롤링
				if (el.size() == 0) {		// 유저 컨텐츠가 없는경우 다음 팬 페이지로 넘어감
					String inTime = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
					System.err.println(i + "\t" + inTime + "\tNOT FOUND");
					CrawlingApplication.filteringcnt = i + 1;
					continue;
				}
				if (el.size() < 10) {		// 유저 컨텐츠가 10개 미만인 경우 한번 드래그 하여 유저 컨텐츠 데이터 크기 증가시킴
					WebElement div = el.get(el.size() - 1);
					jse.executeScript("arguments[0].scrollIntoView();", div);
					Thread.sleep(1000);
					el = driver.findElements(
							By.xpath("//*[@class='_4-u2 _4-u8']//*[@class='_5pbx userContent _3576']//p"));
					elHashTag = driver.findElements(By.xpath("//*[@class='_58cn']"));
				}

				// ============================================================= 수정부분 해쉬태그 제거
				ArrayList<String> text = new ArrayList<>();			// 유저 컨텐츠 내용
				ArrayList<String> hashTag = new ArrayList<>();		// 해쉬태그 내용
				ArrayList<String> text_NoTag = new ArrayList<>();	// 유저 컨텐츠에서 해쉬태그를 제거한 내용

				// 유저 컨텐츠 내용 저장
				for (int k = 0; k < el.size(); k++) {
					str = el.get(k).getText().trim();
					if (str.equals(""))
						continue;
					text.add(str);
				}

				// 해쉬태그 내용 저장
				for (int k = 0; k < elHashTag.size(); k++) {
					str = elHashTag.get(k).getText().trim();
					if (str.equals(""))
						continue;
					hashTag.add(str);
				}

				// 유저 컨텐츠에서 해쉬태그 내용 제거
				for (String _text : text) {
					for (String tag : hashTag) {
						if (_text.contains(tag)) {
							_text = _text.replaceAll(tag, "").trim();
						}
					}
					if (!_text.equals(""))
						text_NoTag.add(_text);
				}

				// ============================================================= Korean posts / posts count < Possibility => foreigner
				double post_K_cnt = 0.d;
				double post_cnt = 0.d;

				
				// 해쉬태그 제거한 내용을 바탕으로 한글의 비율이 어느정도인지 계산
				for (int k = 0; k < text_NoTag.size(); k++) {
					str = text_NoTag.get(k);
					double string_K_cnt = 0.d;
					double string_cnt = 0.d;
					string_cnt = str.length();

					if (str.equals(""))
						continue;
					for (int count = 0; count < string_cnt; count++) {		// 한 게시글에서 한글의 비율이 어느정도인지 계산
						char syllable = str.charAt(count);
						if ((HANGUL_UNICODE_START <= syllable) && (syllable <= HANGUL_UNICODE_END)) {
							string_K_cnt++;
						}
					}
					if ((string_K_cnt / string_cnt) >= Possibility)		// 한 게시글에서 한글의 비율이 possibility 이상이면 한글 게시글이라 판단
						post_K_cnt++;

					post_cnt++;
				}

				// 전체 게시글에서 필터링된 게시글의 비율이 possibility 이상인 경우 유효한 계정이라 판단
				if ((post_K_cnt / post_cnt) < Possibility) {	// 유효하지 않은경우 외국계정이라 판단
					String inTime = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
					System.err.println(
							i + "\t" + inTime + "\tFOREIGNER\t" + String.format("%.2f", post_K_cnt / post_cnt));
					CrawlingApplication.filteringcnt = i + 1;
					continue;
				} else {	// 유효한 경유 최종 결과 리스트에 계정을 저장
					// ============================================================= if Korean => File Write
					CrawlingApplication.Filtered_Fanpages.add(new FacebookData(name, url, number));
					String inTime = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
					System.out.println(i + "\t" + inTime);
					CrawlingApplication.filteringcnt = i + 1;
				}

			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("Error!");
				continue;
			}
		}
	}
}
