package org.example.company.service.utility.converter;

import java.util.List;
import org.example.company.dto.request.RequestOrder;
import org.example.company.dto.response.ResponseOrder;
import org.example.company.dto.response.ResponseOrderItem;
import org.example.company.model.Customer;
import org.example.company.model.Order;
import org.example.company.model.OrderItem;
import org.example.company.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderConverter extends ConverterImpl<Order, ResponseOrder, RequestOrder> {

    @Autowired
    private OrderItemConverter orderItemConverter;

    @Autowired
    private CustomerRepository customerRepository;

    public OrderConverter() {
    }

    @Override
    public ResponseOrder convertToDTO(Order order) {
        List<ResponseOrderItem> responseOrderItemList =
            order.getOrderItemList().stream().map(orderItem -> orderItemConverter.convertToDTO(orderItem)).toList();
        return new ResponseOrder(order.getId(), order.getCreatedAt(), order.getOrderDateDelivered(),
            responseOrderItemList, order.getCustomer().getId());

    }

    @Override
    public Order convertToEntity(RequestOrder requestOrder) {
        List<OrderItem> orderItems =
            requestOrder.items().stream().map(requestOrderItem -> orderItemConverter.convertToEntity(requestOrderItem))
                .toList();
        Customer customer = customerRepository.findById(requestOrder.customerId()).get();
        return new Order(orderItems, customer);
    }

}
