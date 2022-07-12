package com.deanhayden.webhook.service;

import com.deanhayden.webhook.model.ClockSubscription;
import com.deanhayden.webhook.repository.ClockSubscriptionTaskRepository;
import com.deanhayden.webhook.service.util.ClockSubscriptionTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.Future;

@Service
public class ClockSubscriptionTaskManager {

    private static final Logger LOG = LoggerFactory.getLogger(ClockSubscriptionTaskManager.class);

    private final ClockSubscriptionTaskRepository taskRepository;
    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;
    private final ClockSubscriptionWebClient subscriptionWebClient;

    public ClockSubscriptionTaskManager(ClockSubscriptionTaskRepository clockSubscriptionTaskRepository,
                                        ThreadPoolTaskScheduler threadPoolTaskScheduler,
                                        ClockSubscriptionWebClient subscriptionWebClient) {
        this.taskRepository = clockSubscriptionTaskRepository;
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
        this.subscriptionWebClient = subscriptionWebClient;
    }

    public void createSubscription(ClockSubscription clockSubscription) {
        addSubscription(clockSubscription);

        LOG.info(String.format("Scheduled task for URL '%s' with frequency '%s'",
                clockSubscription.getUrl(),
                clockSubscription.getFrequency()));
    }

    private void addSubscription(ClockSubscription clockSubscription) {
        Future<?> scheduledFuture = threadPoolTaskScheduler.scheduleAtFixedRate(
                new ClockSubscriptionTask(clockSubscription, subscriptionWebClient), clockSubscription.getFrequency());

        taskRepository.addTask(clockSubscription.getUrl(), scheduledFuture);
    }

    public void updateSubscription(ClockSubscription clockSubscription) {
        if (taskRepository.hasSubscription(clockSubscription.getUrl())) {
            removeSubscription(clockSubscription.getUrl());
            addSubscription(clockSubscription);

            // TODO: log frequency in more user friendly format
            LOG.info("Scheduled frequency for URL '" + clockSubscription.getUrl() + "' updated to: " + clockSubscription.getFrequency());
        }
    }

    public Mono<String> cancelSubscription(String url) {
        if (taskRepository.hasSubscription(url)) {
            removeSubscription(url);

            LOG.info("Task for URL " + url + " cancelled.");
        }
        return Mono.just(url);
    }

    private void removeSubscription(String url) {
        Future<?> future = taskRepository.getTask(url);
        future.cancel(false);

        taskRepository.removeTask(url);
    }
}
