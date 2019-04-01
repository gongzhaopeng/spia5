package com.atzu68.tacocloud.web;

import com.atzu68.tacocloud.configuration.OrderProps;
import com.atzu68.tacocloud.data.OrderRepository;
import com.atzu68.tacocloud.domain.Order;
import com.atzu68.tacocloud.domain.User;
import com.atzu68.tacocloud.domain.UserUDT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("order")
public class OrderController {

    private final OrderRepository orderRepository;
    private final OrderProps orderProps;

    @Autowired
    public OrderController(
            OrderRepository orderRepository,
            OrderProps orderProps) {

        this.orderRepository = orderRepository;
        this.orderProps = orderProps;
    }

    @GetMapping("/current")
    public String orderForm() {

        return "orderForm";
    }

    @PostMapping
    public Mono<String> processOrder(
            @Valid @ModelAttribute("order") Order order,
            Errors errors,
            SessionStatus sessionStatus,
            @AuthenticationPrincipal User user) {

        if (errors.hasErrors()) {
            return Mono.just("orderForm");
        }

        sessionStatus.setComplete();

        order.setUser(UserUDT.of(user));

        return orderRepository.save(order)
                .map(savedOrder -> "redirect:/");
    }

    @GetMapping
    public String ordersForUser(
            @AuthenticationPrincipal User user,
            Model model) {

        model.addAttribute("orders",
                orderRepository
                        .findByUser(UserUDT.of(user))
                        .take(orderProps.getPageSize())
        );

        return "orderList";
    }
}
