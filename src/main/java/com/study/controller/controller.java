package com.study.controller;

import java.io.IOException;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.study.CrawlingApplication;
import com.study.crawling.Crawler;
import com.study.model.InputData;

@Controller
public class controller {

	/*
	 * TYPE : GET
	 * URL : http://127.0.0.1:8080/
	 * return : index.jsp
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/")
	public String mainview() {
		return "index";
	}

	/*
	 * TYPE : POST
	 * URL : http://127.0.0.1:8080/crawling
	 * PARAMETER : 
	 * 		String startdate;
	 * 		String enddate;
	 * 		String facebook_id;
	 * 		String facebook_pw;
	 * 		String maxcnt;
	 * action : 비동기로 크롤링 실행
	 * return : crawling.jsp 
	 */
	@Async
	@RequestMapping(method = RequestMethod.POST, value = "/crawling")
	public String crawlingview(InputData input)
			throws NumberFormatException, IOException, InterruptedException {
		
		input.display();
		
		CrawlingApplication.Initailize();
		
		//crawler들의 상태가 false 즉, 실행되고 있지 않으면 state를 true로 바꾸고 run
		for (Crawler crawler : CrawlingApplication.crawler) {
			crawler.setInputData(input);
			try {
				if(!crawler.getState().get()) {
					crawler.setState(true);
					crawler.run();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "crawling";
	}
	
	/*
	 * TYPE : GET
	 * URL : http://127.0.0.1:8080/data
	 * action : 스태틱 변수에 담겨져 있는 현재 진행중인 크롤링 상황을 서버로 전송
	 * return : data.jsp
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/data")
	public String getdata(Model model) {
		model.addAttribute("loopingnumber", CrawlingApplication.loopingcnt);
		model.addAttribute("navercnt", CrawlingApplication.navercnt);
		model.addAttribute("fanpagecnt", CrawlingApplication.fanpagecnt);
		model.addAttribute("filteringcnt", CrawlingApplication.filteringcnt);
		model.addAttribute("naver", CrawlingApplication.NaverKeywords.size());
		model.addAttribute("fanpage", CrawlingApplication.Fanpages.size());
		model.addAttribute("filtering", CrawlingApplication.Filtered_Fanpages.size());
		model.addAttribute("list", CrawlingApplication.Filtered_Fanpages);
		return "data";
	}
}
