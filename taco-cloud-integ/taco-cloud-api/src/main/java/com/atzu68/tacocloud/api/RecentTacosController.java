package com.atzu68.tacocloud.api;

import com.atzu68.tacocloud.api.resource.TacoResource;
import com.atzu68.tacocloud.api.resource.TacoResourceAssembler;
import com.atzu68.tacocloud.data.TacoRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RepositoryRestController
public class RecentTacosController {

    private TacoRepository tacoRepository;
    private TacoResourceAssembler tacoResourceAssembler;

    public RecentTacosController(
            TacoRepository tacoRepository,
            TacoResourceAssembler tacoResourceAssembler) {

        this.tacoRepository = tacoRepository;
        this.tacoResourceAssembler = tacoResourceAssembler;
    }

    @GetMapping(path = "/tacos/recent", produces = "application/hal+json")
    public ResponseEntity<Resources<TacoResource>> recentTacos() {

        var pageRequest = PageRequest.of(0, 12,
                Sort.by("createdAt").descending());
        var tacos = tacoRepository.findAll(pageRequest).getContent();
        var tacoResources = tacoResourceAssembler.toResources(tacos);
        var recentResources = new Resources<>(tacoResources);
        recentResources.add(linkTo(methodOn(RecentTacosController.class)
                .recentTacos()).withRel("recents"));

        return new ResponseEntity<>(recentResources, HttpStatus.OK);
    }
}
