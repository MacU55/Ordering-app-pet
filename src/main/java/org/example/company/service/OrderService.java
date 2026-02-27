package org.example.company.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.company.dto.request.RequestOrder;
import org.example.company.dto.response.ResponseOrder;
import org.example.company.model.Employee;
import org.example.company.model.Order;
import org.example.company.repository.CustomerRepository;
import org.example.company.repository.EmployeeRepository;
import org.example.company.repository.ItemRepository;
import org.example.company.repository.OrderRepository;
import org.example.company.service.utility.converter.OrderConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final OrderConverter orderConverter;
    private final OrderItemService orderItemService;


    @Transactional
    public ResponseOrder createOrder(RequestOrder requestOrder) {

        var customer = customerRepository.findById(requestOrder.customerId())
            .orElseThrow(() -> new EntityNotFoundException("Customer not found: " + requestOrder.customerId()));
        var order = new Order();
        order.setCustomer(customer);
        this.processOrder(requestOrder, order);
        orderRepository.save(order);
        return orderConverter.convertToDTO(order);
    }

    @Transactional(readOnly = true)
    public ResponseOrder getOrder(long id) {
        Order order =
            orderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Order not found: " + id));
        return orderConverter.convertToDTO(order);
    }

    @Transactional(readOnly = true)
    public List<ResponseOrder> findAll() {
        List<Order> orderList = orderRepository.findAll();
        return orderList.stream().map(orderConverter::convertToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ResponseOrder> findCreatedAfter(LocalDateTime date) {
        List<Order> orderList = orderRepository.findByCreatedAtAfter(date);
        return orderList.stream().map(orderConverter::convertToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ResponseOrder> findCreatedBetween(LocalDateTime start, LocalDateTime end) {
        List<Order> orderList = orderRepository.findByCreatedAtBetween(start, end);
        return orderList.stream().map(orderConverter::convertToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ResponseOrder> findUndeliveredOrders() {
        List<Order> orderList = orderRepository.findByOrderDateDeliveredIsNull();
        return orderList.stream().map(orderConverter::convertToDTO).collect(Collectors.toList());
    }

    @Transactional
    public ResponseOrder updateOrder(long orderId, RequestOrder requestOrder) {
        Order existingOrder = orderRepository.findById(orderId)
            .orElseThrow(() -> new EntityNotFoundException("Order not found. Order uuid= " + orderId));
        var customer = customerRepository.findById(requestOrder.customerId())
            .orElseThrow(() -> new EntityNotFoundException("Customer not found: " + requestOrder.customerId()));
        this.clearOrderItems(existingOrder);
        existingOrder.setCustomer(customer);
        this.processOrder(requestOrder, existingOrder);
        return orderConverter.convertToDTO(orderRepository.save(existingOrder));
    }


    @Transactional
    public void delete(long id) {
        if (!orderRepository.existsById(id)) {
            throw new EntityNotFoundException("Order not found for uuid " + id);
        }
        orderRepository.deleteById(id);
    }

    @Transactional
    public void deliverOrder(long id) {
        if (!orderRepository.existsById(id)) {
            throw new EntityNotFoundException("Order not found for uuid " + id);
        } else {
            LocalDateTime localDateTime = LocalDateTime.now();
            orderRepository.updateOrderDateDeliveredByOrderId(id, localDateTime);
        }
    }

    @Transactional
    public void setEmployeeToOrder(long orderId, long employeeId)  {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new EntityNotFoundException("Order not found: " + orderId));
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new EntityNotFoundException("Employee not found: " + employeeId));
        order.getEmployees().add(employee);
    }

    @Transactional
    public void removeEmployeeFromOrder(long orderId, long employeeId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new EntityNotFoundException("Order not found: " + orderId));
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new EntityNotFoundException("Employee not found: " + employeeId));
        order.getEmployees().remove(employee);
    }

    private void processOrder(RequestOrder requestOrder, Order order) {
        for (var orderItem : requestOrder.items()) {
            var item = itemRepository.findById(orderItem.itemId())
                .orElseThrow(() -> new EntityNotFoundException("Item not found. Item uuid= " + orderItem.itemId()));
            orderItemService.addOrderItem(order, order.getOrderItemList(), item, orderItem.quantity(), item.getPrice());
        }
    }

    private void clearOrderItems(Order order) {
        order.getOrderItemList().clear();
    }

}
