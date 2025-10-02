package com.superComics.inventory.orders.service;


import com.superComics.inventory.orders.model.order;
import com.superComics.inventory.orders.repository.orderRepo;
import com.superComics.inventory.shared.BusinessException;
import com.superComics.inventory.shared.orderCreationDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class orderServiceImpl implements orderService {

    private final orderRepo orderRepository;
    // Clase de soporte inyectada
    private final orderCreation orderCreation;

    public orderServiceImpl(orderRepo orderRepository, orderCreation orderCreation) {
        this.orderRepository = orderRepository;
        this.orderCreation = orderCreation;
    }

    @Override
    public order createOrder(orderCreationDTO request) {
        // La lógica de negocio y transacción se delega a OrderCreation
        return orderCreation.processNewOrder(request);
    }

    @Override
    public order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("Orden con ID " + orderId + " no encontrada."));
    }

    @Override
    public List<order> getOrdersByTrader(Long traderId) {
        return orderRepository.findAllByTraderId(traderId);
    }

}
