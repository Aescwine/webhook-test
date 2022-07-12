package com.deanhayden.webhook.controller;

import com.deanhayden.webhook.model.ClockSubscription;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.time.Duration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClockSubscriptionControllerTest {

    @Autowired
    private WebTestClient client;

    @Test
    public void whenRegisteringWebhook_shouldReturnCreatedStatus() {
        // given
        ClockSubscription clockSubscription = new ClockSubscription("http://createtesturl", Duration.ofSeconds(10));

        // when
        WebTestClient.ResponseSpec exchange = client.post()
                .uri("/clocks/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(clockSubscription))
                .exchange();

        // then
        exchange.expectStatus().isCreated();
    }

    @Test
    public void whenUnregisteringWebhook_shouldReturnNoContentStatus() {
        // given
        ClockSubscription clockSubscription = new ClockSubscription("http://deletetesturl", null);

        client.post()
                .uri("/clocks/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(clockSubscription))
                .exchange();

        // when
        WebTestClient.ResponseSpec exchange = client.method(HttpMethod.DELETE)
                .uri("/clocks/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(clockSubscription))
                .exchange();

        // then
        exchange.expectStatus().isNoContent();
    }

    @Test
    public void whenUpdatingWebhookFrequency_shouldReturnOkStatus() {
        // given
        ClockSubscription clockSubscription = new ClockSubscription("http://updatetesturl", Duration.ofSeconds(10));

        client.post()
                .uri("/clocks/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(clockSubscription))
                .exchange();

        // when
        WebTestClient.ResponseSpec exchange = client.put()
                .uri("/clocks/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(clockSubscription))
                .exchange();

        // then
        exchange.expectStatus().isOk();
    }
}