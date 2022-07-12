package com.deanhayden.webhook.service;

import com.deanhayden.webhook.model.ClockSubscription;
import com.deanhayden.webhook.repository.ClockSubscriptionRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ClockSubscriptionService {

    private final ClockSubscriptionRepository clockSubscriptionRepository;
    private final ClockSubscriptionTaskManager clockSubscriptionTaskManager;

    public ClockSubscriptionService(ClockSubscriptionRepository clockSubscriptionRepository,
                                    ClockSubscriptionTaskManager clockSubscriptionTaskManager) {
        this.clockSubscriptionRepository = clockSubscriptionRepository;
        this.clockSubscriptionTaskManager = clockSubscriptionTaskManager;
    }

    public Mono<ClockSubscription> registerSubscription(ClockSubscription clockSubscription) {
        return clockSubscriptionRepository.saveClockSubscription(clockSubscription)
                .doOnNext(clockSubscriptionTaskManager::createSubscription);
    }

    public Mono<String> removeSubscription(String url) {
        return clockSubscriptionTaskManager.cancelSubscription(url)
                .doOnNext(clockSubscriptionRepository::deleteClockSubscriptionByUrl);
    }

    public Mono<ClockSubscription> updateSubscriptionFrequency(ClockSubscription clockSubscription) {
        return clockSubscriptionRepository.updateClockSubscription(clockSubscription)
                .doOnNext(clockSubscriptionTaskManager::updateSubscription);
    }
}
