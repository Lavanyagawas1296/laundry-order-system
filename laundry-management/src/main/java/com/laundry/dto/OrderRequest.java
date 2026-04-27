package com.laundry.dto;

import com.laundry.entity.GarmentItem;
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
public class OrderRequest {

    private String customerName;

    private String phoneNumber;

    private List<GarmentItem> garments;

    private LocalDateTime estimatedDelivery;
}
