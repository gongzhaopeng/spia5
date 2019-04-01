package com.atzu68.spia5.eureka.client.ingredient.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Profile("feign")
@Controller
@RequestMapping("/ingredients")
@Slf4j
public class IngredientController {

    private final IngredientClient client;

    @Autowired
    public IngredientController(
            IngredientClient client) {
        this.client = client;
    }

    @GetMapping
    public String ingredientList(Model model) {

        log.info("Fetched all ingredients from a Feign client.");

        model.addAttribute(
                "ingredients",
                client.getAllIngredients());

        return "ingredientList";
    }

    @GetMapping("/{id}")
    public String ingredientDetailPage(
            @PathVariable("id") String id,
            Model model) {

        log.info("Fetched an ingredient from a Feign client.");

        model.addAttribute(
                "ingredient",
                client.getIngredient(id));

        return "ingredientDetail";
    }
}
