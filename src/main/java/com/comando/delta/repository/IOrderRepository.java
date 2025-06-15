package com.comando.delta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.comando.delta.interfaces.IStatistic;
import com.comando.delta.model.Order;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.isActive = true ORDER BY o.id DESC")
    List<Order> findActiveOrderList();

    @Query("SELECT o FROM Order o WHERE o.isActive = false ORDER BY o.id DESC")
    List<Order> findInactiveOrderList();

    @Query("SELECT o FROM Order o WHERE o.orderNumber = :number")
    Order findOrderByNumber(@Param("number") String number);

    @Query("SELECT COUNT(o) > 0 FROM Order o WHERE o.orderNumber = :orderNumber")
    Boolean existenceOrder(@Param("orderNumber") String orderNumber);  
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.orderNumber = :orderNumber")
    Long existenceEditedOrder(@Param("orderNumber") String orderNumber);

    @Query("SELECT os AS serviceType, COUNT(os) AS total FROM Order o JOIN o.serviceType os GROUP BY os")
    List<IStatistic> findStatisticsList();

} 
