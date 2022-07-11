package com.deanhayden.webhook.repository;

import com.deanhayden.webhook.model.ClockSubscription;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClockSubscriptionRepository {

    Mono<ClockSubscription> saveClockSubscription(ClockSubscription clockSubscription);

    Mono<ClockSubscription> updateClockSubscription(ClockSubscription clockSubscription);

    void deleteClockSubscriptionByUrl(String url);

    Mono<ClockSubscription> getClockSubscriptionByUrl(String url);
}
