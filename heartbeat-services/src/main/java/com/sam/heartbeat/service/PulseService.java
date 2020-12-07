package com.sam.heartbeat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import com.sam.heartbeat.model.Pulse;
import com.sam.heartbeat.repository.PulseRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class PulseService {

    private final PulseRepository pulseRepository;

    public Flux<Pulse> findAllPulses() {
        return pulseRepository.findAll();
    }

}
