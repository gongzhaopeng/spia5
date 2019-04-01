package com.atzu68.tacocloud.configuration;

import com.atzu68.tacocloud.data.IngredientRepository;
import com.atzu68.tacocloud.data.PaymentMethodRepository;
import com.atzu68.tacocloud.data.TacoRepository;
import com.atzu68.tacocloud.data.UserRepository;
import com.atzu68.tacocloud.domain.*;
import com.atzu68.tacocloud.domain.Ingredient.Type;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Profile("!prod")
@Configuration
public class DevelopmentConfig {

    @Bean
    public CommandLineRunner dataLoader(IngredientRepository repo,
                                        UserRepository userRepo,
                                        PasswordEncoder encoder,
                                        TacoRepository tacoRepo,
                                        PaymentMethodRepository paymentMethodRepo) {

        return new CommandLineRunner() {

            @Override
            public void run(String... args) {

                var flourTortilla = saveAnIngredient("FLTO", "Flour Tortilla", Type.WRAP);
                var cornTortilla = saveAnIngredient("COTO", "Corn Tortilla", Type.WRAP);
                var groundBeef = saveAnIngredient("GRBF", "Ground Beef", Type.PROTEIN);
                var carnitas = saveAnIngredient("CARN", "Carnitas", Type.PROTEIN);
                var tomatoes = saveAnIngredient("TMTO", "Diced Tomatoes", Type.VEGGIES);
                var lettuce = saveAnIngredient("LETC", "Lettuce", Type.VEGGIES);
                var cheddar = saveAnIngredient("CHED", "Cheddar", Type.CHEESE);
                var jack = saveAnIngredient("JACK", "Monterrey Jack", Type.CHEESE);
                var salsa = saveAnIngredient("SLSA", "Salsa", Type.SAUCE);
                var sourCream = saveAnIngredient("SRCR", "Sour Cream", Type.SAUCE);

                userRepo.save(new User(
                        "habuma", encoder.encode("password"),
                        "Craig Walls", "123 North Street", "Cross Roads",
                        "TX", "76227", "123-123-1234",
                        "craig@habuma.com"
                )).map(user -> new UserUDT(
                        user.getUsername(),
                        user.getFullname(),
                        user.getPhoneNumber()
                )).flatMap(userUDT -> paymentMethodRepo.save(new PaymentMethod(
                                userUDT, "4111111111111111",
                                "321", "10/25"
                        ))
                ).subscribe();

                var taco1 = new Taco();
                taco1.setName("Carnivore");
                taco1.setIngredients(
                        List.of(flourTortilla, groundBeef, carnitas,
                                sourCream, salsa, cheddar));
                tacoRepo.save(taco1).subscribe();

                var taco2 = new Taco();
                taco2.setName("Bovine Bounty");
                taco2.setIngredients(
                        List.of(cornTortilla, groundBeef, cheddar,
                                jack, sourCream));
                tacoRepo.save(taco2).subscribe();

                var taco3 = new Taco();
                taco3.setName("Veg-Out");
                taco3.setIngredients(
                        List.of(flourTortilla, cornTortilla,
                                tomatoes, lettuce, salsa));
                tacoRepo.save(taco3).subscribe();

            }

            private IngredientUDT saveAnIngredient(
                    String id,
                    String name,
                    Type type) {

                var ingredient =
                        new Ingredient(id, name, type);
                repo.save(ingredient).subscribe();

                return new IngredientUDT(name, type);
            }
        };
    }
}
