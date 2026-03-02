package org.example.company.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.company.dto.request.RequestOrderItemCreate;
import org.example.company.dto.request.RequestOrderItemUpdate;
import org.example.company.dto.response.ResponseOrderItem;
import org.example.company.model.Order;
import org.example.company.model.OrderItem;
import org.example.company.model.Item;
import org.example.company.repository.OrderItemRepository;
import org.example.company.repository.OrderRepository;
import org.example.company.repository.ItemRepository;
import org.example.company.service.utility.converter.OrderItemConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final OrderItemConverter orderItemConverter;

    @Transactional(readOnly = true)
    public ResponseOrderItem getById(long id) {
        OrderItem orderItem = orderItemRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("OrderItem not found for uuid= " + id));
        return orderItemConverter.convertToDTO(orderItem);
    }

    @Transactional(readOnly = true)
    public List<ResponseOrderItem> getAll() {
        return orderItemRepository.findAll().stream()
            .map(orderItemConverter::convertToDTO)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ResponseOrderItem> getByOrderId(long orderId) {
        return orderItemRepository.findByOrder_Id(orderId).stream()
            .map(orderItemConverter::convertToDTO)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public boolean existsByOrderIdAndItemId(long orderId, long itemId) {
        return orderItemRepository.existsByOrder_IdAndItem_Id(orderId, itemId);
    }

    @Transactional
    public ResponseOrderItem create(RequestOrderItemCreate request) {
        Order order = orderRepository.findById(request.orderId())
            .orElseThrow(() -> new EntityNotFoundException("Order not found for uuid= " + request.orderId()));
        Item item = itemRepository.findById(request.itemId())
            .orElseThrow(() -> new EntityNotFoundException("Item not found for uuid= " + request.itemId()));
        BigDecimal price = item.getPrice().multiply(new BigDecimal(request.quantity()));
        OrderItem orderItem = new OrderItem(order, item, request.quantity(), price);
        order.getOrderItemList().add(orderItem);
        orderItemRepository.save(orderItem);
         return  orderItemConverter.convertToDTO(orderItem);
    }

    @Transactional
    public ResponseOrderItem update(long id, RequestOrderItemUpdate request) {
        OrderItem orderItem = orderItemRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("OrderItem not found for uuid= " + id));
        orderItem.setQuantity(request.quantity());
        if (request.price() != null) {
            orderItem.setPriceAtOrderTime(request.price());
        }
        return orderItemConverter.convertToDTO(orderItemRepository.save(orderItem));
    }

    @Transactional
    public void delete(long id) {
        orderItemRepository.deleteById(id);
    }

    public void addOrderItem(Order order, List<OrderItem> orderItemList, Item item, int quantity, BigDecimal priceAtOrderTime) {
        OrderItem orderItem = new OrderItem(order, item, quantity, priceAtOrderTime);
        orderItemList.add(orderItem);
    }
}
