package com.study.crawling;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.scheduling.annotation.Async;

import com.study.model.InputData;

/*
 * 3개의 크롤링 객체들의 상속하고 있는 추상클래스
 * 상속받은 객체들은 비동기 함수인 crawling을 구현해야한다
 */
public abstract class Crawler implements Runnable {
	static AtomicBoolean isRunning = new AtomicBoolean();	// 현재 크롤러가 실행중인지 나타내는 변수	true면 실행중 false면 대기중
	
	private InputData input;
	
	public String getDriverPath() throws URISyntaxException, IOException {
		return "D:\\chromedriver.exe";
	}
	
	public void setInputData(InputData input) {
		this.input = input;
	}
	public InputData getInputData() {
		return input;
	}
	
	public Crawler() {
		isRunning.set(false);
	}
	
	public AtomicBoolean getState() {
		return isRunning;
	}
	
	public void setState(Boolean b) {
		isRunning.set(b);
	}
	
	@Async
	public abstract void crawling(InputData input) throws Exception;
	
	@Override
	public void run() throws NullPointerException {
		try {
			crawling(input);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			setState(false);
		}
	}
}
