package com.deanhayden.webhook.service;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class WebClientServiceTest {

    private ClockSubscriptionWebClient subscriptionWebClient;

    private MockWebServer mockWebServer;

    private HttpUrl webhookUrl;

    @BeforeEach
    void init() {
        mockWebServer = new MockWebServer();
        webhookUrl = mockWebServer.url("/clockSubscriber");

        subscriptionWebClient = new ClockSubscriptionWebClient(WebClient.builder().baseUrl(webhookUrl.url().toString()).build());
    }

    @Test
    void whenSendingSubscriptionRequest_shouldSendTimeToSubscriber() throws InterruptedException {
        // given
        mockWebServer.enqueue(new MockResponse().setResponseCode(200));

        // when
        LocalDateTime localDateTime = LocalDateTime.now();
        subscriptionWebClient.sendTimeRequest(webhookUrl.url().toString(), localDateTime).block();

        // then
        RecordedRequest request = mockWebServer.takeRequest();
        assertThat(request.getMethod()).isEqualTo("POST");
        assertThat(request.getPath()).isEqualTo("/clockSubscriber");
        assertThat(request.getBody().readUtf8()).isEqualTo(localDateTime.toString());
    }
}