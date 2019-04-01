package com.atzu68.tacocloud.web;

import com.atzu68.tacocloud.data.IngredientRepository;
import com.atzu68.tacocloud.data.TacoRepository;
import com.atzu68.tacocloud.domain.Order;
import com.atzu68.tacocloud.domain.Taco;
import com.atzu68.tacocloud.domain.TacoUDT;
import com.atzu68.tacocloud.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Slf4j
@Controller("webDesignTacoController")
@RequestMapping("/design")
@SessionAttributes("order")
public class DesignTacoController {

    private final TacoRepository tacoRepository;
    private final IngredientRepository ingredientRepository;

    @Autowired
    public DesignTacoController(
            TacoRepository tacoRepository,
            IngredientRepository ingredientRepository) {

        this.tacoRepository = tacoRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @ModelAttribute(name = "order")
    public Order order(@AuthenticationPrincipal User user) {

        var order = new Order();

        order.setDeliveryName(user.getFullname());
        order.setDeliveryStreet(user.getStreet());
        order.setDeliveryCity(user.getCity());
        order.setDeliveryState(user.getState());
        order.setDeliveryZip(user.getZip());

        return order;
    }

    @GetMapping
    public String showDesignForm(Model model) {

        model.addAttribute("design",
                new Taco());

        return "design";
    }

    @PostMapping
    public Mono<String> processDesign(
            @Valid @ModelAttribute("design") Mono<Taco> design,
            @ModelAttribute("order") Order order) {

        return design
                .flatMap(d ->
                        Mono.zip(d.getIngredients().stream().map(ingredientUDT ->
                                ingredientRepository.findById(ingredientUDT.getId())
                                        .doOnSuccess(in -> {
                                            ingredientUDT.setName(in.getName());
                                            ingredientUDT.setType(in.getType());
                                        })
                        ).collect(Collectors.toList()), arr -> arr)
                                .flatMap(objs -> tacoRepository.save(d).map(sd -> {
                                            order.addDesign(TacoUDT.of(sd));
                                            return "redirect:/orders/current";
                                        })
                                ))
                .onErrorResume(ex -> {
                    if (ex instanceof WebExchangeBindException) {
                        return Mono.just("design");
                    } else {
                        throw new RuntimeException(ex);
                    }
                });
    }
}
