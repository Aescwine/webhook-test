package com.deanhayden.webhook.controller;

import com.deanhayden.webhook.exception.SubscriptionError;
import com.deanhayden.webhook.model.ClockSubscription;
import com.deanhayden.webhook.service.ClockSubscriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/clocks/subscriptions")
public class ClockSubscriptionController {

    private final ClockSubscriptionService clockSubscriptionService;

    public ClockSubscriptionController(ClockSubscriptionService clockSubscriptionService) {
        this.clockSubscriptionService = clockSubscriptionService;
    }

    @PostMapping
    public Mono<ResponseEntity<ClockSubscription>> registerSubscription(@Valid @RequestBody ClockSubscription clockSubscription) {
        return clockSubscriptionService.registerSubscription(clockSubscription)
                .map(r -> ResponseEntity.status(HttpStatus.CREATED).body(r));
    }

    @PutMapping
    public Mono<ResponseEntity<ClockSubscription>> updateSubscription(@Valid @RequestBody ClockSubscription clockSubscription) {
        return clockSubscriptionService.updateSubscriptionFrequency(clockSubscription)
                .map(r -> ResponseEntity.status(HttpStatus.OK).body(r));
    }

    @DeleteMapping
    public Mono<ResponseEntity<?>> deleteSubscription(@RequestBody ClockSubscription clockSubscription) {
        return clockSubscriptionService.removeSubscription(clockSubscription.getUrl())
                .map(r -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }
}
