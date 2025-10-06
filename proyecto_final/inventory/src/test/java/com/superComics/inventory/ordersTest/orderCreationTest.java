package com.superComics.inventory.ordersTest;

import com.superComics.inventory.inventory.service.inventoryServices;
import com.superComics.inventory.orders.events.orderCompleteEvent;
import com.superComics.inventory.orders.model.order;
import com.superComics.inventory.orders.service.orderCreation;
import com.superComics.inventory.orders.repository.orderRepo;
import com.superComics.inventory.shared.BusinessException;
import com.superComics.inventory.shared.orderCreationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class orderCreationTest {

    @Mock
    private orderRepo orderRepository;

    @InjectMocks
    private orderCreation orderCreation;

    private inventoryServices inventoryService;
    private ApplicationEventPublisher eventPublisher;

    @BeforeEach
    void setUp() {
        // Inicializar Mocks
        orderRepository = mock(orderRepo.class);
        inventoryService = mock(inventoryServices.class);
        eventPublisher = mock(ApplicationEventPublisher.class);
        orderCreation = new orderCreation(orderRepository, inventoryService, eventPublisher);

        // Mock para simular la asignación de ID de la base de datos al guardar
        when(orderRepository.save(any(order.class))).thenAnswer(invocation -> {
            order o = invocation.getArgument(0);
            if (o.getId() == null) o.setId(1L);
            return o;
        });
    }

    private orderCreationDTO createOrderDTO(String type, int quantity) {
        orderCreationDTO.OrderItemRequest item = new orderCreationDTO.OrderItemRequest();
        item.setComicItemId(101L);
        item.setQuantity(quantity);
        item.setUnitPrice(50.0);

        orderCreationDTO dto = new orderCreationDTO();
        dto.setType(type);
        dto.setTraderId(50L);
        dto.setItems(Arrays.asList(item));
        return dto;
    }

    // --- Sale Trade (Venta - Reducir Stock) Tests ---
    @Test
    void processNewOrder_SaleTradeSuccess_ShouldVerifyStockReduceStockAndPublishEvent() {
        // Arrange
        orderCreationDTO request = createOrderDTO("sale_trade", 5);
        when(inventoryService.verifyStock(101L, 5)).thenReturn(true);

        // Act
        order result = orderCreation.processNewOrder(request);

        // Assert
        verify(inventoryService).verifyStock(101L, 5); // Verificación obligatoria
        verify(inventoryService).byeStocks(101L, 5); // Reducción de stock
        verify(orderRepository, times(2)).save(any(order.class)); // Se guarda como PENDING y luego como COMPLETE
        assertEquals("complete", result.getStatus());
        verify(eventPublisher).publishEvent(any(orderCompleteEvent.class));
    }

    @Test
    void processNewOrder_SaleTradeInsufficientStock_ShouldThrowExceptionAndNotSave() {
        // Arrange
        orderCreationDTO request = createOrderDTO("sale_trade", 10);
        when(inventoryService.verifyStock(101L, 10)).thenReturn(false);

        // Act & Assert
        assertThrows(BusinessException.class, () -> {
            orderCreation.processNewOrder(request);
        });

        // Assert: No debe haber interacciones de persistencia después de la validación inicial
        verify(orderRepository, never()).save(any());
        verify(inventoryService, never()).byeStocks(any(), anyInt());
    }

    // --- Acquisition (Compra - Aumentar Stock) Tests ---
    @Test
    void processNewOrder_AcquisitionSuccess_ShouldIncreaseStockAndPublishEvent() {
        // Arrange
        orderCreationDTO request = createOrderDTO("acquisition", 8);

        // Act
        order result = orderCreation.processNewOrder(request);

        // Assert
        // NO debe verificar stock (porque es compra)
        verify(inventoryService, never()).verifyStock(any(), anyInt());
        verify(inventoryService).plusStocks(101L, 8); // Aumento de stock

        verify(orderRepository, times(2)).save(any(order.class));
        assertEquals("complete", result.getStatus());

        ArgumentCaptor<orderCompleteEvent> eventCaptor = ArgumentCaptor.forClass(orderCompleteEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        assertEquals("acquisition", eventCaptor.getValue().getOrderType());
    }
}
