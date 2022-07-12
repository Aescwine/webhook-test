package com.deanhayden.webhook.repository;

import com.deanhayden.webhook.model.ClockSubscription;
import reactor.core.publisher.Mono;

public interface ClockSubscriptionRepository {

    Mono<ClockSubscription> saveClockSubscription(ClockSubscription clockSubscription);

    Mono<ClockSubscription> updateClockSubscription(ClockSubscription clockSubscription);

    Mono<Void> deleteClockSubscriptionByUrl(String url);

    Mono<ClockSubscription> getClockSubscriptionByUrl(String url);
}
