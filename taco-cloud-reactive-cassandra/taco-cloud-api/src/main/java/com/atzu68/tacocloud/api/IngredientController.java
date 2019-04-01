package com.atzu68.tacocloud.api;

import com.atzu68.tacocloud.data.IngredientRepository;
import com.atzu68.tacocloud.domain.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping(path = "/ingredients", produces = "application/json")
@CrossOrigin(origins = "*")
public class IngredientController {

    private IngredientRepository ingredientRepository;

    @Autowired
    public IngredientController(
            IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @GetMapping
    public Mono<?> allIngredientsByType(
            @RequestParam(value = "byType",
                    defaultValue = "false") Boolean byType) {

        var ingredients = ingredientRepository.findAll();

        if (byType) {
            return ingredients.collectList().map(ins -> ins.stream()
                    .sorted(comparing(Ingredient::getId))
                    .collect(groupingBy(
                            ingredient -> ingredient.getType().name().toLowerCase(),
                            toList())));
        } else {
            return ingredients.collectList();
        }
    }

    @GetMapping("/{ingredientId}")
    public Mono<Ingredient> ingredientById(
            @PathVariable("ingredientId") String ingredientId) {
        return ingredientRepository.findById(ingredientId);
    }

    @PutMapping("/{ingredientId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Ingredient> updateIngredient(
            @PathVariable("ingredientId") String ingredientId,
            @RequestBody Mono<Ingredient> ingredient) {

        return ingredient.flatMap(i -> {
            if (!ingredientId.equals(i.getId())) {
                return Mono.error(new IllegalStateException(
                        "Given ingredient's ID doesn't match the ID in the path."));
            } else {
                return ingredientRepository.save(i).ignoreElement();
            }
        });
    }

    @PostMapping
    public Mono<ResponseEntity<Ingredient>> postIngredient(
            @RequestBody Mono<Ingredient> ingredient,
            UriComponentsBuilder uriComponentsBuilder) {

        return ingredient.flatMap(i ->
                ingredientRepository.save(i).map(si -> {
                    var headers = new HttpHeaders();
                    var location = uriComponentsBuilder
                            .pathSegment("ingredients",
                                    si.getId())
                            .build().toUri();
                    headers.setLocation(location);

                    return new ResponseEntity<>(si,
                            headers, HttpStatus.CREATED);
                })
        );
    }

    @DeleteMapping("/{ingredientId}")
    public Mono<ResponseEntity<?>> deleteIngredient(
            @PathVariable("ingredientId") String ingredientId) {

        return ingredientRepository.existsById(ingredientId)
                .flatMap(exists -> {
                    if (exists) {

                        return ingredientRepository.deleteById(ingredientId)
                                .map(v -> new ResponseEntity<>(
                                        null,
                                        HttpStatus.NO_CONTENT));
                    } else {
                        return Mono.just(new ResponseEntity<>(
                                null,
                                HttpStatus.NOT_FOUND));
                    }
                });
    }
}
