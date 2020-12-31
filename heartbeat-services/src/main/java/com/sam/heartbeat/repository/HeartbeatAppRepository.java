package com.sam.heartbeat.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.sam.heartbeat.model.HeartbeatApp;
import com.sam.heartbeat.model.type.Status;

@Repository
public interface HeartbeatAppRepository extends ReactiveMongoRepository<HeartbeatApp, String> {
    Flux<HeartbeatApp> findAll();
    Mono<HeartbeatApp> findByName(String name);
    @Tailable
    Flux<HeartbeatApp> findAllByStatus(Status status);
}
