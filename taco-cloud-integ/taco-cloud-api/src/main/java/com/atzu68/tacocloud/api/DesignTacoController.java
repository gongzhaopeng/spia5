package com.atzu68.tacocloud.api;

import com.atzu68.tacocloud.api.exception.TacoNotFoundException;
import com.atzu68.tacocloud.api.resource.TacoResourceAssembler;
import com.atzu68.tacocloud.data.IngredientRepository;
import com.atzu68.tacocloud.data.TacoRepository;
import com.atzu68.tacocloud.domain.Taco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController("apiDesignTacoController")
@RequestMapping(
        path = "/design",
        produces = "application/json")
@CrossOrigin(origins = "*")
public class DesignTacoController {

    private TacoRepository tacoRepository;
    private IngredientRepository ingredientRepository;
    private TacoResourceAssembler tacoResourceAssembler;

    @Autowired
    public DesignTacoController(
            TacoRepository tacoRepository,
            IngredientRepository ingredientRepository,
            TacoResourceAssembler tacoResourceAssembler) {

        this.tacoRepository = tacoRepository;
        this.ingredientRepository = ingredientRepository;
        this.tacoResourceAssembler = tacoResourceAssembler;
    }

    @GetMapping("/recent")
    public Iterable<?> recentTacos(
            @RequestParam(
                    name = "hateous",
                    defaultValue = "false") Boolean hateous,
            @RequestParam(
                    name = "resource",
                    defaultValue = "false") Boolean resource) {

        var pageRequest = PageRequest.of(0, 12,
                Sort.by("createdAt").descending());

        var tacos = tacoRepository.findAll(pageRequest).getContent();

        if (hateous) {

            if (resource) {

                var tacoResources =
                        tacoResourceAssembler.toResources(tacos);
                var recentResources =
                        new Resources<>(tacoResources);
                recentResources.add(linkTo(
                        methodOn(DesignTacoController.class)
                                .recentTacos(true,
                                        true))
                        .withRel("recents"));

                return recentResources;
            } else {

                var recentResources = Resources.wrap(tacos);

                var recentsLink =
//                    ControllerLinkBuilder
//                            .linkTo(DesignTacoController.class)
//                            .slash("recent")
//                            .withRel("recents");
                        ControllerLinkBuilder.linkTo(
                                methodOn(DesignTacoController.class)
                                        .recentTacos(true,
                                                false))
                                .withRel("recents");

                recentResources.add(recentsLink);

                return recentResources;
            }
        } else {
            return tacos;
        }
    }

    @GetMapping("/{id}")
    public Taco tacoById(@PathVariable("id") Long id) {

        return tacoRepository.findById(id)
                .orElseThrow(
                        TacoNotFoundException::new);
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Taco postTaco(@RequestBody Taco taco) {
        taco.setIngredients(taco.getIngredients().stream().map(ingredient ->
                ingredientRepository.findById(ingredient.getId())
                        .get()).collect(Collectors.toList()));
        return tacoRepository.save(taco);
    }

    @ExceptionHandler(TacoNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleTacoNotFound() {
    }
}
