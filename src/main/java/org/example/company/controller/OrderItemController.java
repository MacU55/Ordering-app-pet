package org.example.company.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.company.dto.request.RequestOrderItemCreate;
import org.example.company.dto.request.RequestOrderItemUpdate;
import org.example.company.dto.response.ResponseOrderItem;
import org.example.company.service.OrderItemService;
import org.springframework.http.ResponseEntity;
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
public class OrderItemController {

    private final OrderItemService orderItemService;

    @GetMapping("/exists")
    public Map<String, Boolean> checkOrderItemExists(
            @RequestParam long orderId,
            @RequestParam long itemId) {
        return Map.of("exists", orderItemService.existsByOrderIdAndItemId(orderId, itemId));
    }

    @GetMapping
    public List<ResponseOrderItem> getAllOrderItems() {
        return orderItemService.getAll();
    }

    @GetMapping("/order/{orderId}")
    public List<ResponseOrderItem> getOrderItemsByOrderId(@PathVariable long orderId) {
        return orderItemService.getByOrderId(orderId);
    }

    @GetMapping("/{id}")
    public ResponseOrderItem getOrderItemById(@PathVariable long id) {
        return orderItemService.getById(id);
    }

    @PostMapping
    public ResponseEntity<ResponseOrderItem> createOrderItem(@Valid @RequestBody RequestOrderItemCreate request) {
        ResponseOrderItem response = orderItemService.create(request);
        URI location = URI.create("/order-items/" + response.id());
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public ResponseOrderItem updateOrderItem(@PathVariable long id, @Valid @RequestBody RequestOrderItemUpdate request) {
        return orderItemService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable long id) {
        orderItemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
