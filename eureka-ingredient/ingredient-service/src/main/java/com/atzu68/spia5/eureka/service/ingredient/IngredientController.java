package com.atzu68.spia5.eureka.service.ingredient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(
        path = "/ingredients",
        produces = "application/json")
@CrossOrigin(origins = "*")
public class IngredientController {

    private IngredientRepository repo;

    @Autowired
    public IngredientController(
            IngredientRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public Flux<Ingredient> allIngredients() {
        return Flux.fromIterable(repo.findAll());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Ingredient>> byId(
            @PathVariable("id") String id) {
        return Mono.justOrEmpty(repo.findById(id))
                .map(ingredient ->
                        new ResponseEntity<>(
                                ingredient,
                                HttpStatus.OK)
                ).defaultIfEmpty(
                        new ResponseEntity<>(
                                null,
                                HttpStatus.NOT_FOUND));
    }

    @PutMapping(
            path = "/{id}",
            consumes = "application/json")
    public Mono<ResponseEntity<?>> updateIngredient(
            @PathVariable("id") String id,
            @RequestBody Mono<Ingredient> ingredient) {

        return ingredient.map(in -> {

            if (!id.equals(in.getId())) {
                throw new IllegalStateException(
                        "Given ingredient's ID doesn't match the ID in the path.");
            } else {
                if (repo.existsById(id)) {

                    repo.save(in);

                    return new ResponseEntity<>(
                            null,
                            HttpStatus.NO_CONTENT);
                } else {

                    return new ResponseEntity<>(
                            null,
                            HttpStatus.NOT_FOUND);
                }
            }
        });
    }

    @PostMapping(consumes = "application/json")
    public Mono<ResponseEntity<Ingredient>> postIngredient(
            @RequestBody Mono<Ingredient> ingredient,
            UriComponentsBuilder uriComponentsBuilder) {

        return ingredient.map(in -> {

            var saved = repo.save(in);

            var headers = new HttpHeaders();
            var location = uriComponentsBuilder
                    .path("ingredients/{id}")
                    .build(in.getId());
            headers.setLocation(location);

            return new ResponseEntity<>(
                    saved,
                    headers,
                    HttpStatus.CREATED);
        });
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<?>> deleteIngredient(
            @PathVariable("id") String id) {

        return Mono.just(repo.existsById(id)).map(exists -> {

            if (exists) {

                repo.deleteById(id);

                return new ResponseEntity<>(
                        null,
                        HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(
                        null,
                        HttpStatus.NOT_FOUND);
            }
        });
    }
}
