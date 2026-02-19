package org.example.company.controller;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.company.dto.request.RequestOrder;
import org.example.company.dto.response.ResponseOrder;
import org.example.company.service.OrderService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.example.company.security.annotation.IsAllowedByRole;
import org.example.company.security.model.Roles;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    @IsAllowedByRole({Roles.EMPLOYEE_STORE, Roles.EMPLOYEE_ADMINISTRATION, Roles.EMPLOYEE_ACCOUNTING})
    public List<ResponseOrder> getAllOrders() {
        return orderService.findAll();
    }

    @GetMapping("/undelivered")
    @IsAllowedByRole({Roles.EMPLOYEE_STORE, Roles.EMPLOYEE_ADMINISTRATION})
    public List<ResponseOrder> getUndeliveredOrders() {
        return orderService.findUndeliveredOrders();
    }

    @GetMapping("/created-after")
    @IsAllowedByRole({Roles.EMPLOYEE_STORE, Roles.EMPLOYEE_ADMINISTRATION, Roles.EMPLOYEE_ACCOUNTING})
    public List<ResponseOrder> getOrdersCreatedAfter(
            @RequestParam @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime date) {
        return orderService.findCreatedAfter(date);
    }

    @GetMapping("/created-between")
    @IsAllowedByRole({Roles.EMPLOYEE_STORE, Roles.EMPLOYEE_ADMINISTRATION, Roles.EMPLOYEE_ACCOUNTING})
    public List<ResponseOrder> getOrdersCreatedBetween(
            @RequestParam @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime end) {
        return orderService.findCreatedBetween(start, end);
    }

    @GetMapping("/{id}")
    @IsAllowedByRole({Roles.EMPLOYEE_STORE, Roles.EMPLOYEE_ADMINISTRATION, Roles.EMPLOYEE_ACCOUNTING, Roles.CUSTOMER})
    public ResponseOrder getOrderById(@PathVariable long id) {
        return orderService.getOrder(id);
    }

    @PostMapping
    @IsAllowedByRole({Roles.EMPLOYEE_STORE, Roles.CUSTOMER})
    public ResponseEntity<ResponseOrder> createOrder(@Valid @RequestBody RequestOrder requestOrder) {
        ResponseOrder responseOrder = orderService.createOrder(requestOrder);
        URI location = URI.create("/orders/" + responseOrder.id());
        return ResponseEntity.created(location).body(responseOrder);
    }

    @PutMapping("/{id}")
    @IsAllowedByRole({Roles.EMPLOYEE_STORE})
    public ResponseOrder updateOrder(@PathVariable long id, @Valid @RequestBody RequestOrder requestOrder) {
        return orderService.updateOrder(id, requestOrder);
    }

    @DeleteMapping("/{id}")
    @IsAllowedByRole({Roles.EMPLOYEE_STORE, Roles.EMPLOYEE_ADMINISTRATION})
    public ResponseEntity<Void> deleteOrder(@PathVariable long id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/deliver")
    @IsAllowedByRole({Roles.EMPLOYEE_STORE})
    public ResponseEntity<Void> deliverOrder(@PathVariable long id) {
        orderService.deliverOrder(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/employee/{employeeId}")
    @IsAllowedByRole({Roles.EMPLOYEE_ADMINISTRATION})
    public ResponseEntity<Void> setEmployeeForOrder(@PathVariable long id, @PathVariable long employeeId) {
        orderService.setEmployeeToOrder(id, employeeId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/employee/{employeeId}")
    @IsAllowedByRole({Roles.EMPLOYEE_ADMINISTRATION})
    public ResponseEntity<Void> removeEmployeeForOrder(@PathVariable long id, @PathVariable long employeeId) {
        orderService.removeEmployeeFromOrder(id, employeeId);
        return ResponseEntity.noContent().build();
    }
}
