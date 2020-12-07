package com.sam.heartbeat.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import com.sam.heartbeat.model.Pulse;

public interface PulseRepository extends ReactiveMongoRepository<Pulse, String> {
    Flux<Pulse> findAll();
}
