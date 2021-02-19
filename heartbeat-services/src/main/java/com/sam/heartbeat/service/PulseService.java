//package com.sam.heartbeat.service;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//import com.sam.heartbeat.client.PulseClient;
//import com.sam.heartbeat.model.Pulse;
//import com.sam.heartbeat.repository.PulseRepository;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class PulseService {
//
//    private final PulseClient pulseClient;
//    private final PulseRepository pulseRepository;
//    private final HeartbeatAppService heartbeatAppService;
//
//    //TODO test dataBuffer toString()
////    public Flux<String> getAppLogs(String appName, Integer lines) {
////        return heartbeatAppService.findByName(appName)
////                .flatMapMany(pulseClient::subscribe)
////                .map(dataBuffer -> new BufferedReader(new InputStreamReader(dataBuffer.asInputStream())))
////                .flatMap(bufferedReader -> Mono.fromCallable(bufferedReader::readLine))
////                .take(lines);
////                .takeLast(lines);
////        return heartbeatAppService.findByName(appName)
////                .flatMap(app -> {
////                    System.out.println("yo sup dawg");
////                        return pulseClient.pulseAppLogs(app);
////                })
////                .map(dataBuffer -> dataBuffer.toString(Charset.defaultCharset()));
//    }
//
//    public Flux<Pulse> findAllPulses() {
//        return pulseRepository.findAll();
//    }
//
//}
