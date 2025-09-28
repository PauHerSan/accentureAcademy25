package com.superComics.inventory.orders.api;


import com.superComics.inventory.orders.model.order;
import com.superComics.inventory.orders.service.orderService;
import com.superComics.inventory.shared.orderCreationDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class orderController {

    private final orderService orderService;

    public orderController(orderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<order> createOrder(@RequestBody orderCreationDTO request) {
        order newOrder = orderService.createOrder(request);
        return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<order> getOrderById(@PathVariable Long id) {
        order order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }


    @GetMapping("/trader/{traderId}")
    public ResponseEntity<List<order>> getOrdersByTrader(@PathVariable Long traderId) {
        List<order> orders = orderService.getOrdersByTrader(traderId);
        return ResponseEntity.ok(orders);
    }
}
