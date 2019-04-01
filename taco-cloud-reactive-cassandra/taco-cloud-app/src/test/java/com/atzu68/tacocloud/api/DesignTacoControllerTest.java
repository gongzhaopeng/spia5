package com.atzu68.tacocloud.api;

import com.atzu68.tacocloud.data.TacoRepository;
import com.atzu68.tacocloud.domain.Ingredient.Type;
import com.atzu68.tacocloud.domain.IngredientUDT;
import com.atzu68.tacocloud.domain.Taco;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.StreamUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Slf4j
public class DesignTacoControllerTest {

    private static Taco[] tacos = {
            testTaco("022c9695-9daa-4c29-ba6e-75a4469de9f5",
                    1L, 1547475625865L),
            testTaco("4654f065-765d-4092-9f58-f13c239dafd5",
                    2L, 1547475625894L),
            testTaco("2fb17d68-2866-4233-9c28-f3ec4136df2a",
                    3L, 1547475625894L),
            testTaco("7f3f2790-61ac-4665-8239-f3531ed8ecea",
                    4L, 1547475625895L),
            testTaco("e7ad6f8f-e11d-4a11-b04b-f087d9f67c24",
                    5L, 1547475625895L),
            testTaco("9bd036d9-7d6f-412c-83bd-090fb03e9969",
                    6L, 1547475625895L),
            testTaco("ac998cab-d94b-4744-bdd1-c9905cc1b3b5",
                    7L, 1547475625895L),
            testTaco("96c947c2-256a-4239-b495-4473268dbda2",
                    8L, 1547475625895L),
            testTaco("e56edecf-4360-498a-a3a7-7ab50dd50c99",
                    9L, 1547475625895L),
            testTaco("5d1e5bfd-cbc2-4c32-97e2-f3c4c8c3b839",
                    10L, 1547475625895L),
            testTaco("a742934c-78a9-4629-b698-da64c799a467",
                    11L, 1547475625895L),
            testTaco("75a003d4-4cc9-4ee5-8959-c094a6a7480c",
                    12L, 1547475625895L),
            testTaco("0bb53a27-d037-413b-a08c-4288343ece2b",
                    13L, 1547475625895L),
            testTaco("6be3a988-3d31-45f7-8c73-8e3d6e574155",
                    14L, 1547475625895L),
            testTaco("56fdfc11-5528-4dc0-8929-82b0c20447eb",
                    15L, 1547475625895L),
            testTaco("f3a2ce6c-19de-445a-90a1-cfc55bf17df4",
                    16L, 1547475625895L)
    };

    private static TacoRepository tacoRepo;

    private static WebTestClient testClient;

    private static Taco testTaco(String id, Long number, Long createdAt) {

        var taco = new Taco();
        taco.setId(UUID.fromString(id));
        taco.setName("Taco " + number);
        taco.setCreatedAt(new Date(createdAt));
        taco.setIngredients(List.of(
                new IngredientUDT(
                        "Ingredient A",
                        Type.WRAP),
                new IngredientUDT(
                        "Ingredient B",
                        Type.PROTEIN)
        ));

        return taco;
    }

    @BeforeClass
    public static void init() throws Exception {

        log.info(new ObjectMapper().writeValueAsString(tacos));

        var tacoFlux = Flux.just(tacos);

        tacoRepo = Mockito.mock(TacoRepository.class);
        when(tacoRepo.findAll()).thenReturn(tacoFlux);

        testClient = WebTestClient.bindToController(
                new DesignTacoController(tacoRepo))
                .build();
    }

    @Test
    public void recentTacos() {

        testClient.get().uri("/design/recent")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$").isNotEmpty()
                .jsonPath("$[0].id").isEqualTo(tacos[0].getId().toString())
                .jsonPath("$[0].name").isEqualTo("Taco 1")
                .jsonPath("$[1].id").isEqualTo(tacos[1].getId().toString())
                .jsonPath("$[1].name").isEqualTo("Taco 2")
                .jsonPath("$[11].id").isEqualTo(tacos[11].getId().toString())
                .jsonPath("$[11].name").isEqualTo("Taco 12")
                .jsonPath("$[12]").doesNotExist();
    }

    @Test
    public void recentTacos_json() throws Exception {

        var recentsResource =
                new ClassPathResource("/tacos/recent-tacos.json");
        var recentsJson = StreamUtils.copyToString(
                recentsResource.getInputStream(),
                Charset.forName("UTF-8"));

        testClient.get().uri("/design/recent")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json(recentsJson);
    }

    @Test
    public void recentsTacos_bodyList() {

        testClient.get().uri("/design/recent")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Taco.class)
                .contains(Arrays.copyOf(tacos, 12));
    }

    @Test
    public void saveATaco() {

        var unsavedTacoMono = Mono.just(
                testTaco(
                        "022c9695-9daa-4c29-ba6e-75a4469de9f5",
                        1L,
                        1547475625865L)
        );
        var savedTaco = testTaco(
                "022c9695-9daa-4c29-ba6e-75a4469de9f5",
                1L,
                1547475625865L);
        var savedTacoMono = Mono.just(savedTaco);
        when(tacoRepo.save(any())).thenReturn(savedTacoMono);

        testClient.post().uri("/design")
                .contentType(MediaType.APPLICATION_JSON)
                .body(unsavedTacoMono, Taco.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Taco.class)
                .isEqualTo(savedTaco);
    }
}
