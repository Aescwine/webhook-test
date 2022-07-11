package com.deanhayden.webhook.service;

import com.deanhayden.webhook.model.ClockSubscription;
import com.deanhayden.webhook.repository.ClockSubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;

@ExtendWith(MockitoExtension.class)
class ClockSubscriptionServiceTest {

    private final ClockSubscription clockSubscription = new ClockSubscription("http://testurl.com", Duration.ofSeconds(5));

    private ClockSubscriptionService clockSubscriptionService;

    @Mock
    private ClockSubscriptionRepository clockSubscriptionRepository;

    @Mock
    private SubscriptionTaskManager subscriptionTaskManager;

    @BeforeEach
    void init() {
        clockSubscriptionService = new ClockSubscriptionService(clockSubscriptionRepository, subscriptionTaskManager);
    }

    @Test
    void whenSavingClockSubscription_shouldSaveAndThenCreateSubscriptionTask() {
        // given
        given(clockSubscriptionRepository.saveClockSubscription(any(ClockSubscription.class))).willReturn(Mono.just(clockSubscription));

        // when
        clockSubscriptionService.registerSubscription(clockSubscription).block();

        // then
        InOrder inOrder = inOrder(clockSubscriptionRepository, subscriptionTaskManager);

        inOrder.verify(clockSubscriptionRepository).saveClockSubscription(clockSubscription);
        inOrder.verify(subscriptionTaskManager).createSubscription(clockSubscription);
    }

    @Test
    void whenRemovingClockSubscription_shouldCancelTaskAndThenDeleteSubscription() {
        // given
        String url = "http://testurl.com";

        given(subscriptionTaskManager.cancelSubscription(url)).willReturn(Mono.just(url));

        // when
        clockSubscriptionService.removeSubscription(url).block();

        // then
        InOrder inOrder = inOrder(clockSubscriptionRepository, subscriptionTaskManager);

        inOrder.verify(subscriptionTaskManager).cancelSubscription(url);
        inOrder.verify(clockSubscriptionRepository).deleteClockSubscriptionByUrl(url);
    }

    @Test
    void whenUpdatingClockSubscription_shouldUpdateSubscriptionAndThenUpdateSubscriptionTask() {
        // given
        given(clockSubscriptionRepository.updateClockSubscription(any(ClockSubscription.class))).willReturn(Mono.just(clockSubscription));

        // when
        clockSubscriptionService.updateSubscriptionFrequency(clockSubscription).block();

        // then
        InOrder inOrder = inOrder(clockSubscriptionRepository, subscriptionTaskManager);

        inOrder.verify(clockSubscriptionRepository).updateClockSubscription(clockSubscription);
        inOrder.verify(subscriptionTaskManager).updateSubscription(clockSubscription);
    }
}