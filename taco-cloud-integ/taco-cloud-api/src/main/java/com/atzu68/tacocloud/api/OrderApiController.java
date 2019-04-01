package com.atzu68.tacocloud.api;

import com.atzu68.tacocloud.api.exception.OrderNotFoundException;
import com.atzu68.tacocloud.data.OrderRepository;
import com.atzu68.tacocloud.data.TacoRepository;
import com.atzu68.tacocloud.domain.Order;
import com.atzu68.tacocloud.domain.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(
        path = "orders",
        produces = "application/json")
@CrossOrigin(origins = "*")
public class OrderApiController {

    private OrderRepository orderRepository;
    private TacoRepository tacoRepository;

    public OrderApiController(
            OrderRepository orderRepository,
            TacoRepository tacoRepository) {

        this.orderRepository = orderRepository;
        this.tacoRepository = tacoRepository;
    }

    @GetMapping
    public Iterable<Order> allOrders() {
        return orderRepository.findAll();
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Order postOrder(
            @RequestBody Order order,
            @AuthenticationPrincipal User user) {

        order.setUser(user);
        order.setTacos(order.getTacos().stream().map(taco ->
                tacoRepository.findById(taco.getId()).get())
                .collect(Collectors.toList()));

        return orderRepository.save(order);
    }

    @PutMapping(
            path = "/{orderId}",
            consumes = "application/json")
    public Order putOrder(
            @PathVariable("orderId") Long orderId,
            @RequestBody Order order,
            @AuthenticationPrincipal User user) {

        if (!orderRepository.existsById(orderId)) {
            throw new OrderNotFoundException();
        }

        orderRepository.deleteById(orderId);

        order.setUser(user);
        order.setId(orderId);
        order.setTacos(order.getTacos().stream().map(taco ->
                tacoRepository.findById(taco.getId()).get())
                .collect(Collectors.toList()));

        return orderRepository.save(order);
    }

    @PatchMapping(
            path = "/{orderId}",
            consumes = "application/json")
    public Order patchOrder(
            @PathVariable("orderId") Long orderId,
            @RequestBody Order patchOrder) {

        var order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        Optional.ofNullable(patchOrder.getDeliveryName())
                .ifPresent(order::setDeliveryName);
        Optional.ofNullable(patchOrder.getDeliveryStreet())
                .ifPresent(order::setDeliveryStreet);
        Optional.ofNullable(patchOrder.getDeliveryCity())
                .ifPresent(order::setDeliveryCity);
        Optional.ofNullable(patchOrder.getDeliveryState())
                .ifPresent(order::setDeliveryState);
        Optional.ofNullable(patchOrder.getDeliveryZip())
                .ifPresent(order::setDeliveryZip);
        Optional.ofNullable(patchOrder.getCcNumber())
                .ifPresent(order::setCcNumber);
        Optional.ofNullable(patchOrder.getCcExpiration())
                .ifPresent(order::setCcExpiration);
        Optional.ofNullable(patchOrder.getCcCVV())
                .ifPresent(order::setCcCVV);

        return orderRepository.save(order);
    }

    @DeleteMapping("/{orderId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteOrder(
            @PathVariable("orderId") Long orderId) {

        if (!orderRepository.existsById(orderId)) {
            throw new OrderNotFoundException();
        }

        orderRepository.deleteById(orderId);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public void handleOrderNotFound() {
    }
}
