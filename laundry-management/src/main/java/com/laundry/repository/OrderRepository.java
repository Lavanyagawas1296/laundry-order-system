package com.laundry.repository;

import com.laundry.entity.Order;
import com.laundry.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> findByStatus(OrderStatus status);

    List<Order> findByCustomerNameContainingIgnoreCase(String customerName);

    List<Order> findByPhoneNumber(String phoneNumber);

    List<Order> findByStatusAndCustomerNameContainingIgnoreCase(OrderStatus status, String customerName);

    @Query("select o.status, count(o) from Order o group by o.status")
    List<Object[]> countOrdersByStatus();

    @Query("select sum(o.totalBill) from Order o")
    Double sumTotalRevenue();
}
