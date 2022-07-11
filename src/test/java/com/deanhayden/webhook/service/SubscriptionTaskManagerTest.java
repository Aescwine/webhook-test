package com.deanhayden.webhook.service;

import com.deanhayden.webhook.model.ClockSubscription;
import com.deanhayden.webhook.repository.ClockSubscriptionTaskRepository;
import com.deanhayden.webhook.service.util.ClockSubscriptionTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.time.Duration;
import java.util.concurrent.Future;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class SubscriptionTaskManagerTest {

    private static final String URL_FOR_TEST = "http://testurl.com";

    private final ClockSubscription clockSubscription = new ClockSubscription(URL_FOR_TEST, Duration.ofSeconds(5));

    private SubscriptionTaskManager subscriptionTaskManager;

    @Mock
    private ClockSubscriptionTaskRepository taskRepository;

    @Mock
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Mock
    private SubscriptionWebClient subscriptionWebClient;

    @BeforeEach
    void init() {
        subscriptionTaskManager = new SubscriptionTaskManager(taskRepository, threadPoolTaskScheduler, subscriptionWebClient);
    }

    @Test
    void whenCreatingSubscription_shouldScheduleAtFixedRateForGivenFrequency() {
        // given
        Duration expectedFrequency = Duration.ofSeconds(5);

        // when
        subscriptionTaskManager.createSubscription(clockSubscription);

        // then
        then(threadPoolTaskScheduler).should().scheduleAtFixedRate(any(ClockSubscriptionTask.class), eq(expectedFrequency));
    }

    @Test
    void whenCreatingSubscription_shouldAddTaskToTaskRepositoryAfterScheduling() {
        // when
        subscriptionTaskManager.createSubscription(clockSubscription);

        // then
        InOrder inOrder = inOrder(threadPoolTaskScheduler, taskRepository);

        inOrder.verify(threadPoolTaskScheduler).scheduleAtFixedRate(any(ClockSubscriptionTask.class), eq(clockSubscription.getFrequency()));
        inOrder.verify(taskRepository).addTask(eq(clockSubscription.getUrl()), any());
    }

    @Test
    @SuppressWarnings("unchecked")
    void whenUpdatingSubscription_shouldCancelExistingSubscriptionTask() {
        // given
        Future future = mock(Future.class);

        given(taskRepository.hasSubscription(URL_FOR_TEST)).willReturn(true);
        given(taskRepository.getTask(URL_FOR_TEST)).willReturn(future);

        // when
        subscriptionTaskManager.updateSubscription(clockSubscription);

        // then
        then(future).should().cancel(false);
    }

    @Test
    @SuppressWarnings("unchecked")
    void whenUpdatingSubscription_shouldScheduleNewTaskAtFixedRateForGivenFrequency() {
        // given
        Future future = mock(Future.class);
        given(taskRepository.hasSubscription(URL_FOR_TEST)).willReturn(true);
        given(taskRepository.getTask(URL_FOR_TEST)).willReturn(future);

        // when
        subscriptionTaskManager.updateSubscription(clockSubscription);

        // then
        InOrder inOrder = inOrder(threadPoolTaskScheduler, taskRepository);

        inOrder.verify(threadPoolTaskScheduler).scheduleAtFixedRate(any(ClockSubscriptionTask.class), eq(clockSubscription.getFrequency()));
        inOrder.verify(taskRepository).addTask(eq(clockSubscription.getUrl()), any());
    }

    @Test
    void whenUpdatingSubscription_shouldNotUpdateTaskIfExistingTaskDoesNotExist() {
        // given
        given(taskRepository.hasSubscription(URL_FOR_TEST)).willReturn(false);

        // when
        subscriptionTaskManager.updateSubscription(clockSubscription);

        // then
       then(threadPoolTaskScheduler).shouldHaveNoInteractions();
    }

    @Test
    @SuppressWarnings("unchecked")
    void whenCancellingSubscription_shouldCancelExistingSubscriptionTask() {
        // given
        Future future = mock(Future.class);

        given(taskRepository.hasSubscription(URL_FOR_TEST)).willReturn(true);
        given(taskRepository.getTask(URL_FOR_TEST)).willReturn(future);

        // when
        subscriptionTaskManager.cancelSubscription(URL_FOR_TEST);

        // then
        then(future).should().cancel(false);
    }

    @Test
    void whenCancellingSubscription_shouldNotCancelTaskIfExistingTaskDoesNotExist() {
        // given
        given(taskRepository.hasSubscription(URL_FOR_TEST)).willReturn(false);

        // when
        subscriptionTaskManager.cancelSubscription(URL_FOR_TEST);

        // then
        then(threadPoolTaskScheduler).shouldHaveNoInteractions();
    }
}