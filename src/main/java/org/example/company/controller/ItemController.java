package org.example.company.controller;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Map;
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
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/by-name")
    @PreAuthorize("hasAnyRole(" +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_LAB.name()," +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_STORE.name()," +
        "T(org.example.company.security.model.RoleTypes).CUSTOMER.name())")
    public ResponseEntity<ResponseItem> getItemByName(@RequestParam String name) {
        return itemService.findItemByName(name)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/id-exists")
    @PreAuthorize("hasAnyRole(T(org.example.company.security.model.RoleTypes).EMPLOYEE_LAB.name()," +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_STORE.name())")
    public Map<String, Boolean> checkIdExists(@RequestParam long id) {
        return Map.of("exists", itemService.existsById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole(" +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_LAB.name()," +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_STORE.name()," +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_ACCOUNTING.name()," +
        "T(org.example.company.security.model.RoleTypes).CUSTOMER.name())")
    public List<ResponseItem> getAllItems() {
        return itemService.getAllItems();
    }

    @GetMapping("/{id}/discount-price")
    @PreAuthorize("hasAnyRole(" +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_LAB.name()," +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_STORE.name()," +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_ACCOUNTING.name())")
    public BigDecimal getDiscountPrice(@PathVariable long id) {
        return itemService.checkDiscountPrice(id);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole(" +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_LAB.name()," +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_STORE.name()," +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_ACCOUNTING.name()," +
        "T(org.example.company.security.model.RoleTypes).CUSTOMER.name())")
    public ResponseItem getItemById(@PathVariable long id) {
        return itemService.getItem(id);
    }

    @PostMapping
    @PreAuthorize("hasRole(T(org.example.company.security.model.RoleTypes).EMPLOYEE_LAB.name())")
    public ResponseEntity<ResponseItem> createItem(@Valid @RequestBody RequestItem requestItem) {
        ResponseItem responseItem = itemService.saveItem(requestItem);
        URI location = URI.create("/items/" + responseItem.id());
        return ResponseEntity.created(location).body(responseItem);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole(" +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_LAB.name()," +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_ACCOUNTING.name())")
    public ResponseItem updateItem(@PathVariable long id, @Valid @RequestBody RequestItem requestItem) {
        return itemService.updateItem(id, requestItem);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole(T(org.example.company.security.model.RoleTypes).EMPLOYEE_LAB.name())")
    public ResponseEntity<Void> deleteItem(@PathVariable long id) {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}
