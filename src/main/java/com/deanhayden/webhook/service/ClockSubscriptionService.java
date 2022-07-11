package com.deanhayden.webhook.service;

import com.deanhayden.webhook.model.ClockSubscription;
import com.deanhayden.webhook.repository.ClockSubscriptionRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ClockSubscriptionService {

    private final ClockSubscriptionRepository clockSubscriptionRepository;
    private final SubscriptionTaskManager subscriptionTaskManager;

    public ClockSubscriptionService(ClockSubscriptionRepository clockSubscriptionRepository,
                                    SubscriptionTaskManager subscriptionTaskManager) {
        this.clockSubscriptionRepository = clockSubscriptionRepository;
        this.subscriptionTaskManager = subscriptionTaskManager;
    }

    public Mono<ClockSubscription> registerSubscription(ClockSubscription clockSubscription) {
        return clockSubscriptionRepository.saveClockSubscription(clockSubscription)
                .doOnNext(subscriptionTaskManager::createSubscription);
    }

    public Mono<Void> removeSubscription(String url) {
        return subscriptionTaskManager.cancelSubscription(url).doOnNext(
                clockSubscriptionRepository::deleteClockSubscriptionByUrl).then();
    }

    public Mono<ClockSubscription> updateSubscriptionFrequency(ClockSubscription clockSubscription) {
        return clockSubscriptionRepository.updateClockSubscription(clockSubscription)
                .doOnNext(subscriptionTaskManager::updateSubscription);
    }
}
