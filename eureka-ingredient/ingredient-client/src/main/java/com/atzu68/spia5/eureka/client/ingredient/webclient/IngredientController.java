package com.atzu68.spia5.eureka.client.ingredient.webclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Profile("webclient")
@Controller
@RequestMapping("/ingredients")
@Slf4j
public class IngredientController {

    private final IngredientServiceClient client;

    @Autowired
    public IngredientController(
            IngredientServiceClient client) {
        this.client = client;
    }

    @GetMapping
    public String ingredientList(Model model) {

        log.info("Fetched all ingredients from a WebClient-based service.");
        model.addAttribute(
                "ingredients",
                client.getAllIngredients());

        return "ingredientList";
    }

    @GetMapping("/{id}")
    public String ingredientDetailPage(
            @PathVariable("id") String id,
            Model model) {

        log.info("Fetched an ingredient from a WebClient-based service.");
        model.addAttribute(
                "ingredient",
                client.getIngredientById(id));

        return "ingredientDetail";
    }
}
