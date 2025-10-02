package com.superComics.inventory.orders.service;

import com.superComics.inventory.orders.model.order;
import com.superComics.inventory.shared.orderCreationDTO;

import java.util.List;

public interface orderService {

    order createOrder(orderCreationDTO request);


    order getOrderById(Long orderId);


    List<order> getOrdersByTrader(Long traderId);

}
