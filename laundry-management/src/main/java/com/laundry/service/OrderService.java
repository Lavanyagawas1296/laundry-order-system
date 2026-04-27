package com.laundry.service;

import com.laundry.dto.OrderRequest;
import com.laundry.dto.OrderResponse;
import com.laundry.entity.GarmentItem;
import com.laundry.entity.Order;
import com.laundry.enums.OrderStatus;
import com.laundry.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderResponse createOrder(OrderRequest request) {
        Order order = Order.builder()
                .customerName(request.getCustomerName())
                .phoneNumber(request.getPhoneNumber())
                .garments(request.getGarments())
                .totalBill(calculateTotalBill(request.getGarments()))
                .status(OrderStatus.RECEIVED)
                .estimatedDelivery(getEstimatedDelivery(request.getEstimatedDelivery()))
                .build();

        Order savedOrder = orderRepository.save(order);
        return mapToResponse(savedOrder);
    }

    public OrderResponse updateOrderStatus(String orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(newStatus);
        Order savedOrder = orderRepository.save(order);
        return mapToResponse(savedOrder);
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<OrderResponse> filterOrders(OrderStatus status, String customerName, String phoneNumber) {
        List<Order> orders;

        if (status != null && hasText(customerName)) {
            orders = orderRepository.findByStatusAndCustomerNameContainingIgnoreCase(status, customerName);
        } else if (status != null) {
            orders = orderRepository.findByStatus(status);
        } else if (hasText(customerName)) {
            orders = orderRepository.findByCustomerNameContainingIgnoreCase(customerName);
        } else if (hasText(phoneNumber)) {
            orders = orderRepository.findByPhoneNumber(phoneNumber);
        } else {
            orders = orderRepository.findAll();
        }

        return orders.stream()
                .filter(order -> !hasText(phoneNumber) || phoneNumber.equals(order.getPhoneNumber()))
                .map(this::mapToResponse)
                .toList();
    }

    public Map<String, Object> getDashboard() {
        Map<String, Object> dashboard = new HashMap<>();
        Map<String, Long> ordersByStatus = new HashMap<>();

        for (Object[] row : orderRepository.countOrdersByStatus()) {
            OrderStatus status = (OrderStatus) row[0];
            Long count = (Long) row[1];
            ordersByStatus.put(status.name(), count);
        }

        Double totalRevenue = orderRepository.sumTotalRevenue();

        dashboard.put("totalOrders", orderRepository.count());
        dashboard.put("totalRevenue", totalRevenue != null ? totalRevenue : 0.0);
        dashboard.put("ordersByStatus", ordersByStatus);

        return dashboard;
    }

    private Double calculateTotalBill(List<GarmentItem> garments) {
        if (garments == null) {
            return 0.0;
        }

        return garments.stream()
                .mapToDouble(garment -> safeQuantity(garment) * safePricePerItem(garment))
                .sum();
    }

    private int safeQuantity(GarmentItem garment) {
        if (garment == null) {
            return 0;
        }

        return garment.getQuantity() != null ? garment.getQuantity() : 0;
    }

    private double safePricePerItem(GarmentItem garment) {
        if (garment == null) {
            return 0.0;
        }

        return garment.getPricePerItem() != null ? garment.getPricePerItem() : 0.0;
    }

    private LocalDateTime getEstimatedDelivery(LocalDateTime estimatedDelivery) {
        return estimatedDelivery != null ? estimatedDelivery : LocalDateTime.now().plusDays(3);
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private OrderResponse mapToResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .customerName(order.getCustomerName())
                .phoneNumber(order.getPhoneNumber())
                .status(order.getStatus())
                .totalBill(order.getTotalBill())
                .createdAt(order.getCreatedAt())
                .estimatedDelivery(order.getEstimatedDelivery())
                .garments(order.getGarments())
                .build();
    }
}
