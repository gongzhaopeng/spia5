package com.atzu68.tacocloud.configuration;

import com.atzu68.tacocloud.data.IngredientRepository;
import com.atzu68.tacocloud.data.TacoRepository;
import com.atzu68.tacocloud.data.UserRepository;
import com.atzu68.tacocloud.domain.Ingredient;
import com.atzu68.tacocloud.domain.Ingredient.Type;
import com.atzu68.tacocloud.domain.User;
import com.atzu68.tacocloud.domain.Taco;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Profile("!prod")
@Configuration
public class DevelopmentConfig {

//    @Bean
//    public CommandLineRunner dataLoader(
//            IngredientRepository ingredientRepository,
//            UserRepository userRepository,
//            PasswordEncoder passwordEncoder,
//            TacoRepository tacoRepository) {
//
//        return args -> {
//
//            var flourTortilla =
//                    ingredientRepository.save(new Ingredient("FLTO", "Flour Tortilla", Type.WRAP));
//            var cornTortilla =
//                    ingredientRepository.save(new Ingredient("COTO", "Corn Tortilla", Type.WRAP));
//            var groundBeef =
//                    ingredientRepository.save(new Ingredient("GRBF", "Ground Beef", Type.PROTEIN));
//            var carnitas =
//                    ingredientRepository.save(new Ingredient("CARN", "Carnitas", Type.PROTEIN));
//            var tomatoes =
//                    ingredientRepository.save(new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES));
//            var lettuce =
//                    ingredientRepository.save(new Ingredient("LETC", "Lettuce", Type.VEGGIES));
//            var cheddar =
//                    ingredientRepository.save(new Ingredient("CHED", "Cheddar", Type.CHEESE));
//            var jack =
//                    ingredientRepository.save(new Ingredient("JACK", "Monterrey Jack", Type.CHEESE));
//            var salsa =
//                    ingredientRepository.save(new Ingredient("SLSA", "Salsa", Type.SAUCE));
//            var sourCream =
//                    ingredientRepository.save(new Ingredient("SRCR", "Sour Cream", Type.SAUCE));
//
//            userRepository.save(new User("habuma", passwordEncoder.encode("password"),
//                    "Craig Walls", "123 North Street", "Cross Roads", "TX",
//                    "76227", "123-123-1234"));
//
//            var taco1 = new Taco();
//            taco1.setName("Carnivore");
//            taco1.setIngredients(List.of(flourTortilla, groundBeef, carnitas, sourCream, salsa, cheddar));
//            tacoRepository.save(taco1);
//
//            Taco taco2 = new Taco();
//            taco2.setName("Bovine Bounty");
//            taco2.setIngredients(List.of(cornTortilla, groundBeef, cheddar, jack, sourCream));
//            tacoRepository.save(taco2);
//
//            Taco taco3 = new Taco();
//            taco3.setName("Veg-Out");
//            taco3.setIngredients(List.of(flourTortilla, cornTortilla, tomatoes, lettuce, salsa));
//            tacoRepository.save(taco3);
//        };
//    }
}
