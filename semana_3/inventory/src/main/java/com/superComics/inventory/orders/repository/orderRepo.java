package com.superComics.inventory.orders.repository;

import com.superComics.inventory.orders.model.order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface orderRepo extends JpaRepository<order,Long>{

    List<order> findAllByTraderId(Long traderId);
}
