package com.atzu68.tacocloud.api;

import com.atzu68.tacocloud.api.exception.OrderNotFoundException;
import com.atzu68.tacocloud.data.OrderRepository;
import com.atzu68.tacocloud.domain.Order;
import com.atzu68.tacocloud.domain.User;
import com.atzu68.tacocloud.domain.UserUDT;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(
        path = "orders",
        produces = "application/json")
@CrossOrigin(origins = "*")
public class OrderApiController {

    private OrderRepository orderRepository;

    public OrderApiController(
            OrderRepository orderRepository) {

        this.orderRepository = orderRepository;
    }

    @GetMapping
    public Flux<Order> allOrders() {
        return orderRepository.findAll();
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Order> postOrder(
            @RequestBody Mono<Order> order,
            @AuthenticationPrincipal User user) {

        return order.flatMap(o -> {

            o.setUser(UserUDT.of(user));

            return orderRepository.save(o);
        });
    }

    @PutMapping(
            path = "/{orderId}",
            consumes = "application/json")
    public Mono<Order> putOrder(
            @PathVariable("orderId") String orderId,
            @RequestBody Mono<Order> order,
            @AuthenticationPrincipal User user) {

        var uOrderId = UUID.fromString(orderId);

        return order.zipWith(orderRepository.existsById(uOrderId))
                .flatMap(t -> {

                    var o = t.getT1();
                    var exists = t.getT2();

                    if (exists) {

                        o.setId(uOrderId);
                        o.setUser(UserUDT.of(user));

                        return orderRepository.save(o);
                    } else {
                        return Mono.error(
                                new OrderNotFoundException());
                    }
                });
    }

    @PatchMapping(
            path = "/{orderId}",
            consumes = "application/json")
    public Mono<Order> patchOrder(
            @PathVariable("orderId") String orderId,
            @RequestBody Mono<Order> patchOrder) {

        return Mono.zip(patchOrder,
                orderRepository.findById(UUID.fromString(orderId)))
                .flatMap(t -> {

                    var o = t.getT1();
                    var oo = t.getT2();

                    Optional.ofNullable(o.getDeliveryName())
                            .ifPresent(oo::setDeliveryName);
                    Optional.ofNullable(o.getDeliveryStreet())
                            .ifPresent(oo::setDeliveryStreet);
                    Optional.ofNullable(o.getDeliveryCity())
                            .ifPresent(oo::setDeliveryCity);
                    Optional.ofNullable(o.getDeliveryState())
                            .ifPresent(oo::setDeliveryState);
                    Optional.ofNullable(o.getDeliveryZip())
                            .ifPresent(oo::setDeliveryZip);
                    Optional.ofNullable(o.getCcNumber())
                            .ifPresent(oo::setCcNumber);
                    Optional.ofNullable(o.getCcExpiration())
                            .ifPresent(oo::setCcExpiration);
                    Optional.ofNullable(o.getCcCVV())
                            .ifPresent(oo::setCcCVV);

                    return orderRepository.save(oo);
                });
    }

    @DeleteMapping("/{orderId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<?> deleteOrder(
            @PathVariable("orderId") String orderId) {

        var uOrderId = UUID.fromString(orderId);

        return orderRepository.existsById(uOrderId).flatMap(exists -> {
            if (exists) {

                return orderRepository.deleteById(uOrderId);
            } else {

                return Mono.error(new OrderNotFoundException());
            }
        });
    }

    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public void handleOrderNotFound() {
    }
}
