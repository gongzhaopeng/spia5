package com.atzu68.spia5.tacocloud.configuration;

import com.atzu68.spia5.tacocloud.model.Ingredient;
import com.atzu68.spia5.tacocloud.model.Ingredient.Type;
import com.atzu68.spia5.tacocloud.model.User;
import com.atzu68.spia5.tacocloud.repository.IngredientRepository;
import com.atzu68.spia5.tacocloud.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Profile("!prod")
@Configuration
public class DevelopmentConfig {

//    @Bean
//    public CommandLineRunner dataLoader(
//            IngredientRepository ingredientRepository,
//            UserRepository userRepository,
//            PasswordEncoder passwordEncoder) {
//
//        return args -> {
//
//            ingredientRepository.save(new Ingredient("FLTO", "Flour Tortilla", Type.WRAP));
//            ingredientRepository.save(new Ingredient("COTO", "Corn Tortilla", Type.WRAP));
//            ingredientRepository.save(new Ingredient("GRBF", "Ground Beef", Type.PROTEIN));
//            ingredientRepository.save(new Ingredient("CARN", "Carnitas", Type.PROTEIN));
//            ingredientRepository.save(new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES));
//            ingredientRepository.save(new Ingredient("LETC", "Lettuce", Type.VEGGIES));
//            ingredientRepository.save(new Ingredient("CHED", "Cheddar", Type.CHEESE));
//            ingredientRepository.save(new Ingredient("JACK", "Monterrey Jack", Type.CHEESE));
//            ingredientRepository.save(new Ingredient("SLSA", "Salsa", Type.SAUCE));
//            ingredientRepository.save(new Ingredient("SRCR", "Sour Cream", Type.SAUCE));
//
//            userRepository.save(new User("habuma", passwordEncoder.encode("password"),
//                    "Craig Walls", "123 North Street", "Cross Roads", "TX",
//                    "76227", "123-123-1234"));
//        };
//    }
}
