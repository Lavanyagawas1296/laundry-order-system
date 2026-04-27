package com.laundry.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GarmentItem {

    private String garmentType;

    private Integer quantity;

    private Double pricePerItem;
}
