package com.atzu68.tacocloud.web;

import com.atzu68.tacocloud.data.TacoRepository;
import com.atzu68.tacocloud.domain.Order;
import com.atzu68.tacocloud.domain.Taco;
import com.atzu68.tacocloud.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Controller("webDesignTacoController")
@RequestMapping("/design")
@SessionAttributes("order")
public class DesignTacoController {

    private final TacoRepository tacoRepository;

    @Autowired
    public DesignTacoController(
            TacoRepository tacoRepository) {
        this.tacoRepository = tacoRepository;
    }

    @ModelAttribute(name = "order")
    public Order order(@AuthenticationPrincipal User user) {

        var order = new Order();

        order.setDeliveryName(user.getFullname());
        order.setDeliveryStreet(user.getStreet());
        order.setDeliveryCity(user.getCity());
        order.setDeliveryState(user.getState());
        order.setDeliveryZip(user.getZip());

        return order;
    }

    @ModelAttribute(name = "design")
    public Taco design() {
        return new Taco();
    }

    @GetMapping
    public String showDesignForm() {

        return "design";
    }

    @PostMapping
    public String processDesign(
            @Valid @ModelAttribute("design") Taco design,
            Errors errors,
            @ModelAttribute("order") Order order) {

        if (errors.hasErrors()) {
            return "design";
        }

        Taco savedDesign = tacoRepository.save(design);
        order.addDesign(savedDesign);

        return "redirect:/orders/current";
    }
}
