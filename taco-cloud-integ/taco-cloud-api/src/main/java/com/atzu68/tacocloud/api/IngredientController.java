package com.atzu68.tacocloud.api;

import com.atzu68.tacocloud.data.IngredientRepository;
import com.atzu68.tacocloud.domain.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.stream.StreamSupport;

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
    public Object allIngredientsByType(
            @RequestParam(value = "byType",
                    defaultValue = "false") Boolean byType) {

        var ingredients = ingredientRepository.findAll();

        if (byType) {
            return StreamSupport.stream(
                    ingredients.spliterator(), false)
                    .collect(groupingBy(
                            ingredient -> ingredient.getType().name().toLowerCase(),
                            toList()));
        } else {
            return ingredients;
        }
    }

    @GetMapping("/{ingredientId}")
    public ResponseEntity<Ingredient> ingredientById(
            @PathVariable("ingredientId") String ingredientId) {
        return ingredientRepository.findById(ingredientId)
                .map(ingredient ->
                        new ResponseEntity<>(ingredient, HttpStatus.OK))
                .orElse(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{ingredientId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateIngredient(
            @PathVariable("ingredientId") String ingredientId,
            @RequestBody Ingredient ingredient) {

        if (!ingredientId.equals(ingredient.getId())) {
            throw new IllegalStateException(
                    "Given ingredient's ID doesn't match the ID in the path.");
        }

        ingredientRepository.save(ingredient);
    }

    @PostMapping
    public ResponseEntity<Ingredient> postIngredient(
            @RequestBody Ingredient ingredient,
            UriComponentsBuilder uriComponentsBuilder) {

        Ingredient saved =
                ingredientRepository.save(ingredient);

        var headers = new HttpHeaders();
        var location = uriComponentsBuilder
                .pathSegment("ingredients",
                        ingredient.getId())
                .build().toUri();
        headers.setLocation(location);

        return new ResponseEntity<>(saved,
                headers, HttpStatus.CREATED);
    }

    @DeleteMapping("/{ingredientId}")
    public ResponseEntity<?> deleteIngredient(
            @PathVariable("ingredientId") String ingredientId) {

        if (!ingredientRepository.existsById(ingredientId)) {
            return new ResponseEntity<>(
                    null,
                    HttpStatus.NOT_FOUND);
        } else {
            ingredientRepository.deleteById(ingredientId);

            return new ResponseEntity<>(
                    null,
                    HttpStatus.NO_CONTENT);
        }
    }
}
