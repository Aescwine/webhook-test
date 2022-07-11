package com.deanhayden.webhook.repository;

import com.deanhayden.webhook.exception.SubscriptionError;
import com.deanhayden.webhook.exception.SubscriptionException;
import com.deanhayden.webhook.model.ClockSubscription;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class HashMapClockSubscriptionRepository implements ClockSubscriptionRepository{

    private final Map<String, ClockSubscription> subscriptions = new ConcurrentHashMap<>();

    public Mono<ClockSubscription> saveClockSubscription(ClockSubscription clockSubscription) {
        if (!subscriptions.containsKey(clockSubscription.getUrl())) {
            subscriptions.put(clockSubscription.getUrl(), clockSubscription);
            return Mono.just(clockSubscription);
        }

        return Mono.error(new SubscriptionException(
                SubscriptionError.URL_ALREADY_REGISTERED_EXCEPTION,
                "Subscription exists for URL"));
    }

    public Mono<ClockSubscription> updateClockSubscription(ClockSubscription clockSubscription) {
        if (subscriptions.containsKey(clockSubscription.getUrl())) {
            subscriptions.put(clockSubscription.getUrl(), clockSubscription);
            return Mono.just(clockSubscription);
        }
        return Mono.error(new SubscriptionException(
                SubscriptionError.URL_NOT_REGISTERED_EXCEPTION,
                "Subscription does not exist for URL"));
    }

    public void deleteClockSubscriptionByUrl(String url) {
        if (!subscriptions.containsKey(url)) {
            throw new SubscriptionException(
                    SubscriptionError.URL_NOT_REGISTERED_EXCEPTION,
                    "Subscription does not exist for URL");
        }

        subscriptions.remove(url);
    }

    public Mono<ClockSubscription> getClockSubscriptionByUrl(String url) {
        return Mono.justOrEmpty(subscriptions.get(url));
    }
}
