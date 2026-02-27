package org.example.company.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.company.dto.request.RequestOrderItemCreate;
import org.example.company.dto.request.RequestOrderItemUpdate;
import org.example.company.dto.response.ResponseOrderItem;
import org.example.company.service.OrderItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order-items")
@RequiredArgsConstructor
@Tag(name = "Order Items", description = "Order item management API")
@PreAuthorize("hasRole(T(org.example.company.security.model.RoleTypes).EMPLOYEE_ACCOUNTING.name())")
public class OrderItemController {

    private final OrderItemService orderItemService;

    @GetMapping("/exists")
    @Operation(summary = "Check if order item exists")
    public Map<String, Boolean> checkOrderItemExists(
            @Parameter(description = "Order ID") @RequestParam long orderId,
            @Parameter(description = "Item ID") @RequestParam long itemId) {
        return Map.of("exists", orderItemService.existsByOrderIdAndItemId(orderId, itemId));
    }

    @GetMapping
    @Operation(summary = "Get all order items")
    public List<ResponseOrderItem> getAllOrderItems() {
        return orderItemService.getAll();
    }

    @GetMapping("/order/{orderId}")
    @Operation(summary = "Get order items by order ID")
    public List<ResponseOrderItem> getOrderItemsByOrderId(
        @Parameter(description = "Order ID") @PathVariable long orderId) {
        return orderItemService.getByOrderId(orderId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order item by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Found"),
        @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseOrderItem getOrderItemById(
        @Parameter(description = "Order item ID") @PathVariable long id) {
        return orderItemService.getById(id);
    }

    @PostMapping
    @Operation(summary = "Create order item", description = "Requires EMPLOYEE_ACCOUNTING role.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Order item created successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied"),
        @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    public ResponseEntity<ResponseOrderItem> createOrderItem(
        @Parameter(description = "Order item data") @Valid @RequestBody RequestOrderItemCreate request) {
        ResponseOrderItem response = orderItemService.create(request);
        URI location = URI.create("/order-items/" + response.id());
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update order item")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Order item updated"),
        @ApiResponse(responseCode = "404", description = "Order item not found")
    })
    public ResponseOrderItem updateOrderItem(
        @Parameter(description = "Order item ID") @PathVariable long id,
        @Parameter(description = "Updated order item data") @Valid @RequestBody RequestOrderItemUpdate request) {
        return orderItemService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete order item")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Order item deleted"),
        @ApiResponse(responseCode = "404", description = "Order item not found")
    })
    public ResponseEntity<Void> deleteOrderItem(
        @Parameter(description = "Order item ID") @PathVariable long id) {
        orderItemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
