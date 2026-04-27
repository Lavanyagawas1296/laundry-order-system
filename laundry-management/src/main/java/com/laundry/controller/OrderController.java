package com.laundry.controller;

import com.laundry.dto.OrderRequest;
import com.laundry.dto.OrderResponse;
import com.laundry.enums.OrderStatus;
import com.laundry.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(request));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable String id,
            @RequestParam OrderStatus newStatus
    ) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, newStatus));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<OrderResponse>> filterOrders(
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) String phoneNumber
    ) {
        return ResponseEntity.ok(orderService.filterOrders(status, customerName, phoneNumber));
    }
}
