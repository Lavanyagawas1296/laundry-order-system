package com.laundry.entity;

import com.laundry.enums.OrderStatus;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "laundry_orders",
        indexes = {
                @Index(name = "idx_laundry_orders_status", columnList = "status"),
                @Index(name = "idx_laundry_orders_phone_number", columnList = "phoneNumber")
        }
)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String customerName;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Double totalBill;

    private LocalDateTime createdAt;

    private LocalDateTime estimatedDelivery;

    @ElementCollection
    @CollectionTable(name = "order_garments", joinColumns = @JoinColumn(name = "order_id"))
    private List<GarmentItem> garments;

    @PrePersist
    void prePersist() {
        createdAt = LocalDateTime.now();
    }
}
