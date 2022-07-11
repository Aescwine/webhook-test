package com.deanhayden.webhook.repository;

import com.deanhayden.webhook.exception.SubscriptionError;
import com.deanhayden.webhook.exception.SubscriptionException;
import com.deanhayden.webhook.model.ClockSubscription;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowableOfType;


class HashMapClockSubscriptionRepositoryTest {

    private final ClockSubscription clockSubscription = new ClockSubscription("http://testurl.com", Duration.ofSeconds(5));

    private final ClockSubscriptionRepository clockSubscriptionMapRepository = new HashMapClockSubscriptionRepository();

    @Test
    void whenSavingClockSubscription_shouldSaveClockSubscriptionToRepository() {
        // when
        clockSubscriptionMapRepository.saveClockSubscription(clockSubscription).block();

        // then
        Mono<ClockSubscription> clockSubscriptionByUrl = clockSubscriptionMapRepository.getClockSubscriptionByUrl("http://testurl.com");

        StepVerifier.create(clockSubscriptionByUrl)
                .expectNextMatches(m -> m.getUrl().equals("http://testurl.com") && m.getFrequency().equals(Duration.ofSeconds(5)))
                .verifyComplete();
    }

    @Test
    void whenSavingClockSubscription_shouldThrowSubscriptionExceptionIfUrlAlreadyRegistered() {
        // given
        clockSubscriptionMapRepository.saveClockSubscription(clockSubscription).block();

        // when
        Mono<ClockSubscription> clockSubscriptionMono = clockSubscriptionMapRepository.saveClockSubscription(clockSubscription);

        // then
        StepVerifier.create(clockSubscriptionMono)
                .expectErrorMatches(throwable -> throwable instanceof SubscriptionException &&
                        throwable.getMessage().equals("Subscription exists for URL"))
                .verify();
    }

    @Test
    void whenUpdatingClockSubscription_shouldUpdateSubscriptionFrequency() {
        // given
        clockSubscriptionMapRepository.saveClockSubscription(clockSubscription).block();

        // when
        clockSubscriptionMapRepository.updateClockSubscription(new ClockSubscription("http://testurl.com", Duration.ofSeconds(60))).block();

        // then
        Mono<ClockSubscription> clockSubscriptionByUrl = clockSubscriptionMapRepository.getClockSubscriptionByUrl("http://testurl.com");

        StepVerifier.create(clockSubscriptionByUrl)
                .expectNextMatches(m -> m.getUrl().equals("http://testurl.com") && m.getFrequency().equals(Duration.ofSeconds(60)))
                .verifyComplete();
    }

    @Test
    void whenUpdatingClockSubscription_shouldThrowSubscriptionExceptionIfNotRegistered() {
        // when
        Mono<ClockSubscription> clockSubscriptionMono = clockSubscriptionMapRepository.updateClockSubscription(clockSubscription);

        // then
        StepVerifier.create(clockSubscriptionMono)
                .expectErrorMatches(throwable -> throwable instanceof SubscriptionException &&
                        throwable.getMessage().equals("Subscription does not exist for URL"))
                .verify();
    }

    @Test
    void whenDeletingClockSubscription_shouldDeleteSubscriptionFromRepository() {
        // given
        clockSubscriptionMapRepository.saveClockSubscription(clockSubscription).block();

        // when
        clockSubscriptionMapRepository.deleteClockSubscriptionByUrl("http://testurl.com");

        // then
        Mono<ClockSubscription> clockSubscriptionByUrl = clockSubscriptionMapRepository.getClockSubscriptionByUrl("http://testurl.com");

        StepVerifier.create(clockSubscriptionByUrl)
                .verifyComplete();
    }

    @Test
    void whenDeletingClockSubscription_shouldThrowSubscriptionExceptionIfUrlNotRegistered() {
        // given
        clockSubscriptionMapRepository.saveClockSubscription(clockSubscription).block();

        // when
        SubscriptionException throwable = catchThrowableOfType(() ->
                        clockSubscriptionMapRepository.deleteClockSubscriptionByUrl("http://unregistered-url.com"),
                SubscriptionException.class);

        // then
        assertThat(throwable.getMessage()).isEqualTo("Subscription does not exist for URL");
        assertThat(throwable.getSubscriptionError()).isEqualTo(SubscriptionError.URL_NOT_REGISTERED_EXCEPTION);
    }
}