package com.atzu68.tacocloud.api;

import com.atzu68.tacocloud.data.TacoRepository;
import com.atzu68.tacocloud.domain.Ingredient;
import com.atzu68.tacocloud.domain.IngredientUDT;
import com.atzu68.tacocloud.domain.Taco;
import com.datastax.driver.core.utils.UUIDs;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@WebFluxTest(controllers = DesignTacoController.class)
public class DesignTacoControllerTestEx {

    @Autowired
    private WebTestClient testClient;

    @MockBean
    private TacoRepository tacoRepository;

    @Test
    @WithMockUser(roles = "USER")
    public void shouldReturnRecentTacos() throws IOException {
        var tacos = new Taco[]{
                testTaco(1L),
                testTaco(2L),
                testTaco(3L),
                testTaco(4L),
                testTaco(5L),
                testTaco(6L),
                testTaco(7L),
                testTaco(8L),
                testTaco(9L),
                testTaco(10L),
                testTaco(11L),
                testTaco(12L)
        };

        Mockito.when(tacoRepository.findAll())
                .thenReturn(Flux.fromArray(tacos));

        testClient.get().uri("/design/recent")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Taco.class).contains(tacos);
    }

    private Taco testTaco(Long number) {

        var taco = new Taco();
        taco.setId(UUIDs.timeBased());
        taco.setName("Taco " + number);
        taco.setIngredients(List.of(
                new IngredientUDT(
                        "Ingredient A",
                        Ingredient.Type.WRAP),
                new IngredientUDT(
                        "Ingredient B",
                        Ingredient.Type.PROTEIN)
        ));

        return taco;
    }
}
