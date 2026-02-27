package org.example.company.controller;

import java.math.BigDecimal;
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
import org.example.company.dto.request.RequestItem;
import org.example.company.dto.response.ResponseItem;
import org.example.company.service.ItemService;
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
@RequestMapping("/items")
@RequiredArgsConstructor
@Tag(name = "Items", description = "Item catalog management API")
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/by-name")
    @Operation(summary = "Get item by name")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Found"),
        @ApiResponse(responseCode = "404", description = "Not found")
    })
    @PreAuthorize("hasAnyRole(" +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_LAB.name()," +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_STORE.name()," +
        "T(org.example.company.security.model.RoleTypes).CUSTOMER.name())")
    public ResponseEntity<ResponseItem> getItemByName(
        @Parameter(description = "Item name") @RequestParam String name) {
        return itemService.findItemByName(name)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/id-exists")
    @Operation(summary = "Check if item ID exists")
    @PreAuthorize("hasAnyRole(T(org.example.company.security.model.RoleTypes).EMPLOYEE_LAB.name()," +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_STORE.name())")
    public Map<String, Boolean> checkIdExists(
        @Parameter(description = "Item ID") @RequestParam long id) {
        return Map.of("exists", itemService.existsById(id));
    }

    @GetMapping
    @Operation(summary = "Get all items")
    @PreAuthorize("hasAnyRole(" +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_LAB.name()," +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_STORE.name()," +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_ACCOUNTING.name()," +
        "T(org.example.company.security.model.RoleTypes).CUSTOMER.name())")
    public List<ResponseItem> getAllItems() {
        return itemService.getAllItems();
    }

    @GetMapping("/{id}/discount-price")
    @Operation(summary = "Get discount price for item")
    @PreAuthorize("hasAnyRole(" +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_LAB.name()," +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_STORE.name()," +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_ACCOUNTING.name())")
    public BigDecimal getDiscountPrice(
        @Parameter(description = "Item ID") @PathVariable long id) {
        return itemService.checkDiscountPrice(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get item by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Found"),
        @ApiResponse(responseCode = "404", description = "Not found")
    })
    @PreAuthorize("hasAnyRole(" +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_LAB.name()," +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_STORE.name()," +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_ACCOUNTING.name()," +
        "T(org.example.company.security.model.RoleTypes).CUSTOMER.name())")
    public ResponseItem getItemById(
        @Parameter(description = "Item ID") @PathVariable long id) {
        return itemService.getItem(id);
    }

    @PostMapping
    @Operation(summary = "Create item", description = "Requires EMPLOYEE_LAB role.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Item created successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied"),
        @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @PreAuthorize("hasRole(T(org.example.company.security.model.RoleTypes).EMPLOYEE_LAB.name())")
    public ResponseEntity<ResponseItem> createItem(
        @Parameter(description = "Item data") @Valid @RequestBody RequestItem requestItem) {
        ResponseItem responseItem = itemService.saveItem(requestItem);
        URI location = URI.create("/items/" + responseItem.id());
        return ResponseEntity.created(location).body(responseItem);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update item")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Item updated"),
        @ApiResponse(responseCode = "404", description = "Item not found")
    })
    @PreAuthorize("hasAnyRole(" +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_LAB.name()," +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_ACCOUNTING.name())")
    public ResponseItem updateItem(
        @Parameter(description = "Item ID") @PathVariable long id,
        @Parameter(description = "Updated item data") @Valid @RequestBody RequestItem requestItem) {
        return itemService.updateItem(id, requestItem);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete item", description = "Requires EMPLOYEE_LAB role.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Item deleted"),
        @ApiResponse(responseCode = "404", description = "Item not found")
    })
    @PreAuthorize("hasRole(T(org.example.company.security.model.RoleTypes).EMPLOYEE_LAB.name())")
    public ResponseEntity<Void> deleteItem(
        @Parameter(description = "Item ID") @PathVariable long id) {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}
