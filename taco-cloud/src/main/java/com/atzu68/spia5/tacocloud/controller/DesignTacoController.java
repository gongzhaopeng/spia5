package com.atzu68.spia5.tacocloud.controller;

import com.atzu68.spia5.tacocloud.model.Order;
import com.atzu68.spia5.tacocloud.model.Taco;
import lombok.extern.slf4j.Slf4j;
import com.atzu68.spia5.tacocloud.model.Ingredient;
import com.atzu68.spia5.tacocloud.model.User;
import com.atzu68.spia5.tacocloud.repository.IngredientRepository;
import com.atzu68.spia5.tacocloud.repository.TacoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("order")
public class DesignTacoController {

    private final IngredientRepository ingredientRepository;

    private final TacoRepository tacoRepository;

    @Autowired
    public DesignTacoController(
            IngredientRepository ingredientRepository,
            TacoRepository tacoRepository) {

        this.ingredientRepository = ingredientRepository;
        this.tacoRepository = tacoRepository;
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

    @ModelAttribute(name = "design")
    public Taco design() {
        return new Taco();
    }

    @ModelAttribute
    public void addIngredientsToModel(Model model) {

        var ingredients = StreamSupport
                .stream(ingredientRepository.findAll().spliterator(),
                        false)
                .collect(toList());

        var ingredientsByType =
                ingredients.stream().collect(
                        groupingBy(Ingredient::getType, toList()));

        Arrays.stream(Ingredient.Type.values()).forEach((type) ->
                model.addAttribute(
                        type.toString().toLowerCase(),
                        ingredientsByType.getOrDefault(type, List.of())
                )
        );
    }

    @ModelAttribute
    public void addUserToModel(
            @AuthenticationPrincipal User user,
            Model model) {
        model.addAttribute("user", user);
    }

    @GetMapping
    public String showDesignForm() {
        
        return "design";
    }

    @PostMapping
    public String processDesign(
            @Valid @ModelAttribute("design") Taco design,
            Errors errors,
            @ModelAttribute("order") Order order) {

        if (errors.hasErrors()) {
            return "design";
        }

        Taco savedDesign = tacoRepository.save(design);
        order.addDesign(savedDesign);

        return "redirect:/orders/current";
    }
}
