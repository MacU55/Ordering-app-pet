package org.example.company.controller;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.company.dto.request.RequestOrder;
import org.example.company.dto.response.ResponseOrder;
import org.example.company.service.OrderService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Order management API")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    @Operation(summary = "Get all orders")
    @PreAuthorize("hasAnyRole(" +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_STORE.name()," +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_ADMIN.name()," +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_ACCOUNTING.name())")
    public List<ResponseOrder> getAllOrders() {
        return orderService.findAll();
    }

    @GetMapping("/undelivered")
    @Operation(summary = "Get undelivered orders")
    @PreAuthorize("hasAnyRole(" +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_STORE.name()," +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_ADMIN.name())")
    public List<ResponseOrder> getUndeliveredOrders() {
        return orderService.findUndeliveredOrders();
    }

    @GetMapping("/created-after")
    @Operation(summary = "Get orders created after date")
    @PreAuthorize("hasAnyRole(" +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_STORE.name()," +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_ADMIN.name()," +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_ACCOUNTING.name())")
    public List<ResponseOrder> getOrdersCreatedAfter(
            @Parameter(description = "Start date (ISO format)") @RequestParam @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime date) {
        return orderService.findCreatedAfter(date);
    }

    @GetMapping("/created-between")
    @Operation(summary = "Get orders created between dates")
    public List<ResponseOrder> getOrdersCreatedBetween(
            @Parameter(description = "Start date (ISO format)") @RequestParam @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime start,
            @Parameter(description = "End date (ISO format)") @RequestParam @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime end) {
        return orderService.findCreatedBetween(start, end);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Found"),
        @ApiResponse(responseCode = "404", description = "Not found")
    })
    @PreAuthorize("hasAnyRole(" +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_STORE.name()," +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_ADMIN.name()," +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_ACCOUNTING.name())")
    public ResponseOrder getOrderById(
        @Parameter(description = "Order ID") @PathVariable long id) {
        return orderService.getOrder(id);
    }

    @PostMapping
    @Operation(summary = "Create order",
        description = "Requires EMPLOYEE_STORE or CUSTOMER role.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Order created successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied"),
        @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @PreAuthorize("hasAnyRole(" +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_STORE.name()," +
        "T(org.example.company.security.model.RoleTypes).CUSTOMER.name())")
    public ResponseEntity<ResponseOrder> createOrder(
        @Parameter(description = "Order data") @Valid @RequestBody RequestOrder requestOrder) {
        ResponseOrder responseOrder = orderService.createOrder(requestOrder);
        URI location = URI.create("/orders/" + responseOrder.id());
        return ResponseEntity.created(location).body(responseOrder);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update order", description = "Requires EMPLOYEE_STORE role.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Order updated"),
        @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PreAuthorize("hasRole(T(org.example.company.security.model.RoleTypes).EMPLOYEE_STORE.name())")
    public ResponseOrder updateOrder(
        @Parameter(description = "Order ID") @PathVariable long id,
        @Parameter(description = "Updated order data") @Valid @RequestBody RequestOrder requestOrder) {
        return orderService.updateOrder(id, requestOrder);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete order")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Order deleted"),
        @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PreAuthorize("hasAnyRole(" +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_STORE.name()," +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_ADMIN.name())")
    public ResponseEntity<Void> deleteOrder(
        @Parameter(description = "Order ID") @PathVariable long id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/deliver")
    @Operation(summary = "Mark order as delivered", description = "Requires EMPLOYEE_STORE role.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Order delivered"),
        @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PreAuthorize("hasRole(T(org.example.company.security.model.RoleTypes).EMPLOYEE_STORE.name())")
    public ResponseEntity<Void> deliverOrder(
        @Parameter(description = "Order ID") @PathVariable long id) {
        orderService.deliverOrder(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/employee/{employeeId}")
    @Operation(summary = "Assign employee to order", description = "Requires EMPLOYEE_ADMIN role.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Employee assigned"),
        @ApiResponse(responseCode = "404", description = "Order or employee not found")
    })
    @PreAuthorize("hasRole(T(org.example.company.security.model.RoleTypes).EMPLOYEE_ADMIN.name())")
    public ResponseEntity<Void> setEmployeeForOrder(
        @Parameter(description = "Order ID") @PathVariable long id,
        @Parameter(description = "Employee ID") @PathVariable long employeeId) {
        orderService.setEmployeeToOrder(id, employeeId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/employee/{employeeId}")
    @Operation(summary = "Remove employee from order", description = "Requires EMPLOYEE_ADMIN role.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Employee removed"),
        @ApiResponse(responseCode = "404", description = "Order or employee not found")
    })
    @PreAuthorize("hasRole(T(org.example.company.security.model.RoleTypes).EMPLOYEE_ADMIN.name())")
    public ResponseEntity<Void> removeEmployeeForOrder(
        @Parameter(description = "Order ID") @PathVariable long id,
        @Parameter(description = "Employee ID") @PathVariable long employeeId) {
        orderService.removeEmployeeFromOrder(id, employeeId);
        return ResponseEntity.noContent().build();
    }
}
