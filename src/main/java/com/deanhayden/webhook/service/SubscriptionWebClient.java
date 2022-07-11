package com.deanhayden.webhook.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class SubscriptionWebClient {

    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionWebClient.class);

    private final WebClient webClient;

    public SubscriptionWebClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<Object> sendTimeRequest(String url, LocalDateTime time) {
        LOG.info(String.format("Sending time to URL '%s'", url));

        return webClient
                .post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(time.toString()), String.class).exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return Mono.empty();
                    } else {
                        LOG.error(String.format("Error occurred sending time to URL '%s'", url));

                        // TODO: handle these errors better
                        // TODO: disable task for webhook call after a specific number of failed requests
                        return response.createException()
                                .flatMap(Mono::error);
                    }
                });
    }
}
