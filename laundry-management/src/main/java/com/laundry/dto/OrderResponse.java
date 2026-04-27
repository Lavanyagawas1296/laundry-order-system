package com.laundry.dto;

import com.laundry.entity.GarmentItem;
import com.laundry.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private String id;

    private String customerName;

    private String phoneNumber;

    private OrderStatus status;

    private Double totalBill;

    private LocalDateTime createdAt;

    private LocalDateTime estimatedDelivery;

    private List<GarmentItem> garments;
}
