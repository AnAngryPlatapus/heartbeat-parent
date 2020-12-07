package com.sam.heartbeat.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import com.sam.heartbeat.model.Pulse;
import com.sam.heartbeat.service.PulseService;

@RestController
@RequestMapping("/pulse")
@RequiredArgsConstructor
public class PulseResource {

    private final PulseService pulseService;

    @GetMapping("all")
    public Flux<Pulse> findAll() {
        return pulseService.findAllPulses();
    }

}
