package com.atzu68.spia5.reactive.reactor;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxMergingTests {

    @Test
    public void mergeFluxes() {

        var characterFlux = Flux
                .just("Garfield", "Kojak", "Barbossa")
                .delayElements(Duration.ofMillis(500));
        var foodFlux = Flux
                .just("Lasagna", "Lollipops", "Apples")
                .delaySubscription(Duration.ofMillis(250))
                .delayElements(Duration.ofMillis(500));

        var mergedFlux =
                characterFlux.mergeWith(foodFlux);

        StepVerifier.create(mergedFlux)
                .expectNext("Garfield")
                .expectNext("Lasagna")
                .expectNext("Kojak")
                .expectNext("Lollipops")
                .expectNext("Barbossa")
                .expectNext("Apples")
                .verifyComplete();
    }

    @Test
    public void zipFluxes() {

        var characterFlux = Flux
                .just("Garfield", "Kojak", "Barbossa");
        var foodFlux = Flux
                .just("Lasagna", "Lollipops", "Apples");

        var zippedFlux =
                Flux.zip(characterFlux, foodFlux);

        StepVerifier.create(zippedFlux)
                .expectNextMatches(p ->
                        p.getT1().equals("Garfield") &&
                                p.getT2().equals("Lasagna"))
                .expectNextMatches(p ->
                        p.getT1().equals("Kojak") &&
                                p.getT2().equals("Lollipops"))
                .expectNextMatches(p ->
                        p.getT1().equals("Barbossa") &&
                                p.getT2().equals("Apples"))
                .verifyComplete();
    }

    @Test
    public void zipFluxesToObject() {

        var characterFlux = Flux
                .just("Garfield", "Kojak", "Barbossa");
        var foodFlux = Flux
                .just("Lasagna", "Lollipops", "Apples");

        var zippedFlux =
                Flux.zip(characterFlux, foodFlux, (c, f) ->
                        String.format("%s eats %s", c, f));

        StepVerifier.create(zippedFlux)
                .expectNext("Garfield eats Lasagna")
                .expectNext("Kojak eats Lollipops")
                .expectNext("Barbossa eats Apples")
                .verifyComplete();
    }

    @Test
    public void firstFlux() {

        var slowFlux = Flux
                .just("tortoise", "snail", "sloth")
                .delaySubscription(Duration.ofMillis(100));
        var fastFlux = Flux
                .just("hare", "cheetah", "squirrel");

        var firstFlux = Flux.first(slowFlux, fastFlux);

        StepVerifier.create(firstFlux)
                .expectNext("hare")
                .expectNext("cheetah")
                .expectNext("squirrel")
                .verifyComplete();
    }
}
