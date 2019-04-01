package com.atzu68.spia5.tacocloud.controller;

import com.atzu68.spia5.tacocloud.configuration.DiscountCodeProps;
import com.atzu68.spia5.tacocloud.configuration.OrderProps;
import com.atzu68.spia5.tacocloud.repository.IngredientRepository;
import com.atzu68.spia5.tacocloud.repository.OrderRepository;
import com.atzu68.spia5.tacocloud.repository.TacoRepository;
import com.atzu68.spia5.tacocloud.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IngredientRepository ingredientRepository;

    @MockBean
    private TacoRepository designRepository;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private DiscountCodeProps discountCodeProps;

    @MockBean
    private OrderProps orderProps;

    @Test
    public void testHomePage() throws Exception {

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(content().string(
                        containsString("Welcome to...")));
    }
}
