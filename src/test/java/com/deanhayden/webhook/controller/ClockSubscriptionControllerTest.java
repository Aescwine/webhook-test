package com.deanhayden.webhook.controller;

import com.deanhayden.webhook.model.ClockSubscription;
import com.github.tomakehurst.wiremock.WireMockServer;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClockSubscriptionControllerTest {

    @Autowired
    private WebTestClient client;

    private MockWebServer mockWebServer;

    private String webhookUrl;

    @BeforeEach
    void init() {
        mockWebServer = new MockWebServer();
        webhookUrl = mockWebServer.getHostName() + mockWebServer.getPort();
    }


    @Test
    public void test() throws InterruptedException {
        // given
        ClockSubscription clockSubscription = new ClockSubscription(webhookUrl, Duration.ofSeconds(10));

        mockWebServer.enqueue(new MockResponse().setResponseCode(200));

        // when
        client.post()
                .uri("/clocks/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(clockSubscription))
                .exchange()
                .expectStatus().isCreated();

        // then
        RecordedRequest request = mockWebServer.takeRequest();
        assertThat(request.getMethod()).isEqualTo("POST");
        assertThat(request.getPath()).isEqualTo("/info");
    }
}