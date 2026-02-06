package org.example.company.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.company.dto.request.RequestOrderItemCreate;
import org.example.company.dto.request.RequestOrderItemUpdate;
import org.example.company.dto.response.ResponseOrderItem;
import org.example.company.models.Order;
import org.example.company.models.OrderItem;
import org.example.company.models.Item;
import org.example.company.repository.OrderItemRepository;
import org.example.company.repository.OrderRepository;
import org.example.company.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;

    @Transactional(readOnly = true)
    public ResponseOrderItem getById(long id) {
        OrderItem orderItem = orderItemRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("OrderItem not found for id= " + id));
        return ResponseOrderItem.fromOrderItem(orderItem);
    }

    @Transactional(readOnly = true)
    public List<ResponseOrderItem> getAll() {
        return orderItemRepository.findAll().stream()
            .map(ResponseOrderItem::fromOrderItem)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ResponseOrderItem> getByOrderId(long orderId) {
        return orderItemRepository.findByOrder_Id(orderId).stream()
            .map(ResponseOrderItem::fromOrderItem)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public boolean existsByOrderIdAndItemId(long orderId, long itemId) {
        return orderItemRepository.existsByOrder_IdAndItem_Id(orderId, itemId);
    }

    @Transactional
    public ResponseOrderItem create(RequestOrderItemCreate request) {
        Order order = orderRepository.findById(request.orderId())
            .orElseThrow(() -> new EntityNotFoundException("Order not found for id= " + request.orderId()));
        Item item = itemRepository.findById(request.itemId())
            .orElseThrow(() -> new EntityNotFoundException("Item not found for id= " + request.itemId()));
        BigDecimal price = request.price() != null ? request.price() : item.getPrice();
        OrderItem orderItem = new OrderItem(order, item, request.quantity(), price);
        order.getOrderItemSet().add(orderItem);
        orderItemRepository.save(orderItem);
        return ResponseOrderItem.fromOrderItem(orderItem);
    }

    @Transactional
    public ResponseOrderItem update(long id, RequestOrderItemUpdate request) {
        OrderItem orderItem = orderItemRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("OrderItem not found for id= " + id));
        orderItem.setQuantity(request.quantity());
        if (request.price() != null) {
            orderItem.setPriceAtOrderTime(request.price());
        }
        return ResponseOrderItem.fromOrderItem(orderItemRepository.save(orderItem));
    }

    @Transactional
    public void delete(long id) {
        if (!orderItemRepository.existsById(id)) {
            throw new EntityNotFoundException("OrderItem not found for id " + id);
        }
        orderItemRepository.deleteById(id);
    }
}
