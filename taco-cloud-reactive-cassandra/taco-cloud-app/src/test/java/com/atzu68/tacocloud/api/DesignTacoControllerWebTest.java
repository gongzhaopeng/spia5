package com.atzu68.tacocloud.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class DesignTacoControllerWebTest {

    @Autowired
    private WebTestClient testClient;

    @Test
    public void recentsTacos() {

        testClient.get().uri("/design/recent")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[?(@.name == 'Carnivore')].ingredients[4].name")
                .isEqualTo("Salsa")
                .jsonPath("$[?(@.name == 'Bovine Bounty')].ingredients[1].name")
                .isEqualTo("Ground Beef")
                .jsonPath("$[?(@.name == 'Veg-Out')].ingredients[2].name")
                .isEqualTo("Diced Tomatoes");
    }
}
