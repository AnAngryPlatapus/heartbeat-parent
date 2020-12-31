package com.sam.heartbeat.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

import com.sam.heartbeat.model.slack.OAuthToken;

public interface OAuthTokenRepository extends ReactiveMongoRepository<OAuthToken, String> {
    Mono<OAuthToken> findTopByOrderByCreatedAtDesc();
}
