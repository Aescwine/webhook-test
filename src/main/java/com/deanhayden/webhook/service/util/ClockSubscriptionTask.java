package com.deanhayden.webhook.service.util;

import com.deanhayden.webhook.model.ClockSubscription;
import com.deanhayden.webhook.service.SubscriptionWebClient;

import java.time.LocalDateTime;

public class ClockSubscriptionTask implements Runnable {

    private final ClockSubscription clockSubscription;
    private final SubscriptionWebClient subscriptionWebClient;

    public ClockSubscriptionTask(ClockSubscription clockSubscription, SubscriptionWebClient subscriptionWebClient) {
        this.clockSubscription = clockSubscription;
        this.subscriptionWebClient = subscriptionWebClient;
    }

    @Override
    public void run() {
        LocalDateTime localDateTime = LocalDateTime.now();
        subscriptionWebClient.sendTimeRequest(clockSubscription.getUrl(), localDateTime).subscribe();
    }
}
