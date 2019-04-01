package com.atzu68.spia5.reactive.reactor;

import org.junit.Ignore;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class FluxLoggingTests {

    @Test
    @Ignore
    public void logSimple() {
        var beltColors = Flux.just(
                "white", "yellow", "orange",
                "green", "purple", "blue")
                .log();
        beltColors.subscribe();
    }

    @Test
    @Ignore
    public void logMapping() {
        var beltColors = Flux.just(
                "white", "yellow", "orange",
                "green", "purple", "blue")
                .map(String::toUpperCase)
                .log();
        beltColors.subscribe();
    }

    @Test
    public void logFlatMapping() throws Exception {
        var beltColors = Flux.just(
                "white", "yellow", "orange",
                "green", "purple", "blue")
                .flatMap(cb -> Mono.just(cb)
                        .map(String::toUpperCase)
                        .log()
                        .subscribeOn(Schedulers.parallel())
                );
        beltColors.subscribe();

        Thread.sleep(3000L);
    }

}
