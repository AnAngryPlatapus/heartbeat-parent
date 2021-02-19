package com.sam.heartbeat.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.sam.heartbeat.model.HeartbeatApp;

@Repository
public interface HeartbeatAppRepository extends ReactiveMongoRepository<HeartbeatApp, String> {
    Flux<HeartbeatApp> findAll();
    Mono<HeartbeatApp> findByName(String name);
    Flux<HeartbeatApp> findAllBySlackConfigured(boolean configured);
}
