package com.atzu68.tacocloud.api;

import com.atzu68.tacocloud.data.TacoRepository;
import com.atzu68.tacocloud.domain.Taco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController("apiDesignTacoController")
@RequestMapping(
        path = "/design",
        produces = "application/json")
@CrossOrigin(origins = "*")
public class DesignTacoController {

    private TacoRepository tacoRepository;

    @Autowired
    public DesignTacoController(
            TacoRepository tacoRepository) {

        this.tacoRepository = tacoRepository;
    }

    @GetMapping("/recent")
    public Flux<Taco> recentTacos() {

        return tacoRepository.findAll().take(12);
    }

    @GetMapping("/{id}")
    public Mono<Taco> tacoById(@PathVariable("id") String id) {

        return tacoRepository.findById(UUID.fromString(id));
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Taco> postTaco(@RequestBody Mono<Taco> taco) {

        return taco.flatMap(t -> tacoRepository.save(t));
    }
}
