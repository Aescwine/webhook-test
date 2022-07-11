package com.deanhayden.webhook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class WebhookApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebhookApplication.class, args);
	}

	// TODO: externalise ThreadPoolTaskScheduler properties such as pool size
	@Bean
	public ThreadPoolTaskScheduler threadPoolTaskScheduler(){
		ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
		threadPoolTaskScheduler.setPoolSize(5);
		threadPoolTaskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
		return threadPoolTaskScheduler;
	}

	@Bean
	public WebClient webClient() {
		return WebClient.create();
	}
}
