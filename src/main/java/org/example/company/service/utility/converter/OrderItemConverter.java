package org.example.company.service.utility.converter;

import org.example.company.dto.request.RequestOrderItem;
import org.example.company.dto.response.ResponseOrderItem;
import org.example.company.model.OrderItem;
import org.springframework.stereotype.Component;

@Component
public class OrderItemConverter extends ConverterImpl<OrderItem, ResponseOrderItem, RequestOrderItem> {

    @Override
    public ResponseOrderItem convertToDTO(OrderItem orderItem) {
        return new ResponseOrderItem(orderItem.getId(),
            orderItem.getOrder().getId(), orderItem.getItem().getId(), orderItem.getItem().getName(),
            orderItem.getQuantity(), orderItem.getPriceAtOrderTime());
    }

    @Override
    public OrderItem convertToEntity(RequestOrderItem requestOrderItem) {
        return new OrderItem(requestOrderItem.itemId(), requestOrderItem.orderId(), requestOrderItem.quantity());
    }
}
