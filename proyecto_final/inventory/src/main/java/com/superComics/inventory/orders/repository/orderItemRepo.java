package com.superComics.inventory.orders.repository;

import com.superComics.inventory.orders.model.orderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface orderItemRepo extends JpaRepository<orderItem, Long> {

    List<orderItem> findAllByOrderId(Long orderId);

}
