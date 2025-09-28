package com.superComics.inventory.orders.service;


import com.superComics.inventory.inventory.service.inventoryServices;
import com.superComics.inventory.orders.events.orderCompleteEvent;
import com.superComics.inventory.orders.model.order;
import com.superComics.inventory.orders.model.orderItem;
import com.superComics.inventory.orders.repository.orderRepo;
import com.superComics.inventory.shared.BusinessException;
import com.superComics.inventory.shared.orderCreationDTO;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class orderCreation {

    private final orderRepo orderRepository;
    // Interacción con otro módulo: solo a través de la interfaz de servicio
    private final inventoryServices inventoryService;
    private final ApplicationEventPublisher eventPublisher;

    public orderCreation(orderRepo orderRepository, inventoryServices inventoryService, ApplicationEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.inventoryService = inventoryService;
        this.eventPublisher = eventPublisher;
    }


    @Transactional
    public order processNewOrder(orderCreationDTO request) {
        // 1. Validar Stock (solo para 'sale_trade')
        if ("sale_trade".equalsIgnoreCase(request.getType())) {
            // Verificar si hay stock para todos los ítems antes de crear la orden.
            request.getItems().forEach(item -> {
                if (!inventoryService.verifyStock(item.getComicItemId(), item.getQuantity())) {
                    throw new BusinessException("No hay suficiente stock para el cómic ID: " + item.getComicItemId());
                }
            });
        }

        // 2. Crear y Persistir la cabecera de la Orden
        order order = new order(
                null,
                request.getType(),
                "pending", // Inicialmente pendiente
                0.0, // El total se calculará después
                new Date(),
                request.getTraderId()
        );
        order = orderRepository.save(order);

        // 3. Crear y Persistir los ítems y calcular el total
        double totalAmount = 0.0;
        for (orderCreationDTO.OrderItemRequest itemRequest : request.getItems()) {
            orderItem item = new orderItem(
                    itemRequest.getComicItemId(),
                    itemRequest.getQuantity(),
                    itemRequest.getUnitPrice()
            );
            item.setOrder(order); // Establecer la relación bidireccional
            totalAmount += item.getSubtotal();
            // No es necesario guardar itemRepository, ya que CascadeType.ALL en Order lo hace.
            order.getItems().add(item);
        }

        // 4. Actualizar total y marcar como completa
        order.setTotalAmount(totalAmount);
        order.complete();
        order completedOrder = orderRepository.save(order);

        // 5. Ajustar el stock en el módulo Inventory y preparar datos para el evento
        List<orderCompleteEvent.OrderItemDetails> eventItems = request.getItems().stream()
                .map(item -> {
                    if ("acquisition".equalsIgnoreCase(request.getType())) {
                        // Si es adquisición (compra), aumentamos el stock.
                        inventoryService.plusStocks(item.getComicItemId(), item.getQuantity());
                    } else {
                        // Si es venta, reducimos el stock.
                        inventoryService.byeStocks(item.getComicItemId(), item.getQuantity());
                    }
                    return orderCompleteEvent.OrderItemDetails.builder()
                            .comicItemId(item.getComicItemId())
                            .quantity(item.getQuantity())
                            .unitPrice(item.getUnitPrice())
                            .build();
                })
                .collect(Collectors.toList());


        // 6. Publicar Evento de Dominio
        eventPublisher.publishEvent(orderCompleteEvent.builder()
                .orderId(completedOrder.getId())
                .traderId(completedOrder.getTraderId())
                .orderType(completedOrder.getType())
                .totalAmount(completedOrder.getTotalAmount())
                .items(eventItems)
                .build());

        return completedOrder;
    }
}
