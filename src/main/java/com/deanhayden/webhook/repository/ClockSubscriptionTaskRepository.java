package com.deanhayden.webhook.repository;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

@Repository
public class ClockSubscriptionTaskRepository {

    private final Map<String, Future<?>> scheduledTasks = new ConcurrentHashMap<>();

    public Future<?> getTask(String subscriptionUrl) {
       return scheduledTasks.get(subscriptionUrl);
    }

    public void addTask(String subscriptionUrl, Future<?> future) {
        scheduledTasks.put(subscriptionUrl, future);
    }

    public void removeTask(String subscriptionUrl) {
        scheduledTasks.remove(subscriptionUrl);
    }

    public boolean hasSubscription(String subscriptionUrl) {
        return scheduledTasks.containsKey(subscriptionUrl);
    }
}
