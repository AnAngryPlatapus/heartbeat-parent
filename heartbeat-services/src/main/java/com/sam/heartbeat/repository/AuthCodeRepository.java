package com.sam.heartbeat.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

import com.sam.heartbeat.model.slack.AuthCode;

public interface AuthCodeRepository extends ReactiveMongoRepository<AuthCode, String> {
    Mono<AuthCode> findTopByOrderByCreatedAtDesc();
}
