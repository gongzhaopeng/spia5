package com.atzu68.tacocloud.restclient;

import com.atzu68.tacocloud.domain.Ingredient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@SpringBootConfiguration
@ComponentScan
@Slf4j
public class RestExamples {

    public static void main(String[] args) {
        SpringApplication.run(RestExamples.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public Traverson traverson() {

        return new Traverson(
                URI.create("http://localhost:8080/api"),
                MediaTypes.HAL_JSON);
    }

    @Bean
    public CommandLineRunner fetchIngredients(
            TacoCloudClient tacoCloudClient) {

        return args -> {
            log.info("----------------------- GET -------------------------");
            log.info("GETTING INGREDIENT BY IDE");
            log.info("Ingredient:  {}",
                    tacoCloudClient.getIngredientById("CHED"));
            log.info("GETTING ALL INGREDIENTS");
            log.info("All ingredients:");
            tacoCloudClient.getAllIngredients()
                    .forEach(ingredient ->
                            log.info("   - {}", ingredient));
        };
    }

    @Bean
    public CommandLineRunner putAnIngredient(
            TacoCloudClient tacoCloudClient) {

        return args -> {
            log.info("----------------------- PUT -------------------------");
            var before = tacoCloudClient.getIngredientById("LETC");
            log.info("BEFORE:  " + before);
            tacoCloudClient.updateIngredient(
                    new Ingredient(
                            "LETC",
                            "Shredded Lettuce",
                            Ingredient.Type.VEGGIES));
            var after = tacoCloudClient.getIngredientById("LETC");
            log.info("AFTER:  " + after);
        };
    }

    @Bean
    public CommandLineRunner addAnIngredient(
            TacoCloudClient tacoCloudClient) {

        return args -> {

            log.info("----------------------- POST -------------------------");

            var chix = new Ingredient(
                    "CHIX",
                    "Shredded Chicken",
                    Ingredient.Type.PROTEIN);
            var chixAfter =
                    tacoCloudClient.createIngredient(chix);
            log.info("AFTER-1:  {}", chixAfter);

            Ingredient beefFajita = new Ingredient(
                    "BFFJ",
                    "Beef Fajita",
                    Ingredient.Type.PROTEIN);
            var location =
                    tacoCloudClient.createIngredientThenLocation(beefFajita);
            log.info("AFTER-2:  {}", location);

            var shrimp = new Ingredient(
                    "SHMP",
                    "Shrimp",
                    Ingredient.Type.PROTEIN);
            var shrimpAfter =
                    tacoCloudClient.createIngredientThenResponseEntity(shrimp);
            log.info("AFTER-3:  {}", shrimpAfter);
        };
    }

    @Bean
    public CommandLineRunner deleteAnIngredient(TacoCloudClient tacoCloudClient) {
        return args -> {
            log.info("----------------------- DELETE -------------------------");

            var before = tacoCloudClient.getIngredientById("CHIX");
            log.info("BEFORE:  {}", before);
            tacoCloudClient.deleteIngredient(before);
            try {
                tacoCloudClient.getIngredientById("CHIX");
            } catch (HttpClientErrorException.NotFound notFound) {
                log.info("CHIX is deleted.");
            }

            before = tacoCloudClient.getIngredientById("BFFJ");
            log.info("BEFORE:  {}", before);
            tacoCloudClient.deleteIngredient(before);
            try {
                tacoCloudClient.getIngredientById("BFFJ");
            } catch (HttpClientErrorException.NotFound notFound) {
                log.info("BFFJ is deleted.");
            }

            before = tacoCloudClient.getIngredientById("SHMP");
            log.info("BEFORE:  {}", before);
            tacoCloudClient.deleteIngredient(before);
            try {
                tacoCloudClient.getIngredientById("SHMP");
            } catch (HttpClientErrorException.NotFound notFound) {
                log.info("SHMP is deleted.");
            }
        };
    }

    @Bean
    public CommandLineRunner traversonGetIngredients(
            TacoCloudClient tacoCloudClient) {

        return args -> {

            var ingredients =
                    tacoCloudClient.getAllIngredientsWithTraverson();
            log.info("----------------------- GET INGREDIENTS WITH TRAVERSON -------------------------");
            ingredients.forEach(ingredient ->
                    log.info("   -  {}", ingredient));
        };
    }

    @Bean
    public CommandLineRunner traversonSaveIngredient(
            TacoCloudClient tacoCloudClient) {

        return args -> {

            var requestPico = new Ingredient(
                    "PICO",
                    "Pico de Gallo",
                    Ingredient.Type.SAUCE);
            log.info("Ingredient[PICO] to post: {}",
                    requestPico);
            var responsePico = tacoCloudClient
                    .addIngredient(requestPico);
            log.info("Ingredient[PICO] returned: {}",
                    responsePico);
            var allIngredients =
                    tacoCloudClient.getAllIngredientsWithTraverson();
            log.info("----------------------- ALL INGREDIENTS AFTER SAVING PICO -------------------------");
            allIngredients.forEach(ingredient ->
                    log.info("   -  {}", ingredient));
            tacoCloudClient.deleteIngredient(requestPico);
        };
    }

    @Bean
    public CommandLineRunner traversonRecentTacos(
            TacoCloudClient tacoCloudClient) {

        return args -> {

            var recentTacos = tacoCloudClient.getRecentTacosWithTraverson();
            log.info("----------------------- GET RECENT TACOS WITH TRAVERSON -------------------------");
            recentTacos.forEach(taco ->
                    log.info("   -  {}", taco));
        };
    }
}

