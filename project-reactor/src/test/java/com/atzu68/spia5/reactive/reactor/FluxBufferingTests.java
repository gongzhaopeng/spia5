package com.atzu68.spia5.reactive.reactor;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.List;

public class FluxBufferingTests {

    @Test
    public void buffer() {

        var fruitFlux = Flux.just(
                "apple", "orange", "banana",
                "kiwi", "strawberry");

        var bufferedFlux = fruitFlux.buffer(3);

        StepVerifier.create(bufferedFlux)
                .expectNext(List.of(
                        "apple", "orange", "banana"))
                .expectNext(List.of(
                        "kiwi", "strawberry"))
                .verifyComplete();
    }

    @Test
    public void bufferAndFlatMap() {

        Flux.just("apple", "orange", "banana",
                "kiwi", "strawberry")
                .buffer(3)
                .flatMap(x -> Flux.fromIterable(x)
                        .map(String::toUpperCase)
                        .subscribeOn(Schedulers.parallel())
                        .log()
                ).subscribe();
    }

    @Test
    public void collectList() {

        var fruitFlux = Flux.just(
                "apple", "orange", "banana",
                "kiwi", "strawberry");

        var fruitListMono = fruitFlux.collectList();

        StepVerifier.create(fruitListMono)
                .expectNext(List.of("apple", "orange",
                        "banana", "kiwi", "strawberry"))
                .verifyComplete();
    }

    @Test
    public void collectMap() {

        var animalFlux = Flux.just(
                "aardvark", "elephant", "koala",
                "eagle", "kangaroo");

        var animalMapMono = animalFlux
                .collectMap(a -> a.charAt(0));

        StepVerifier.create(animalMapMono)
                .expectNextMatches(map -> map.size() == 3 &&
                        map.get('a').equals("aardvark") &&
                        map.get('e').equals("eagle") &&
                        map.get('k').equals("kangaroo")
                )
                .verifyComplete();
    }

    @Test
    public void all() {

        var animalFlux = Flux.just(
                "aardvark", "elephant", "koala",
                "eagle", "kangaroo");

        var hasAMono = animalFlux
                .all(a -> a.contains("a"));
        StepVerifier.create(hasAMono)
                .expectNext(true)
                .verifyComplete();

        var hasKMono = animalFlux
                .all(a -> a.contains("k"));
        StepVerifier.create(hasKMono)
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    public void any() {

        var animalFlux = Flux.just(
                "aardvark", "elephant", "koala",
                "eagle", "kangaroo");

        var hasAMono = animalFlux
                .any(a -> a.contains("a"));
        StepVerifier.create(hasAMono)
                .expectNext(true)
                .verifyComplete();

        var hasZMono = animalFlux
                .any(a -> a.contains("z"));
        StepVerifier.create(hasZMono)
                .expectNext(false)
                .verifyComplete();
    }
}
