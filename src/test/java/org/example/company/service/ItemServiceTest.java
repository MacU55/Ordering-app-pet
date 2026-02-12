package org.example.company.service;

import org.example.company.dto.request.RequestItem;
import org.example.company.dto.response.ResponseItem;
import org.example.company.models.ItemType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.util.Optional;


@SpringBootTest
public class ItemServiceTest extends BaseTestConfig{

    private final ItemService itemService;
    private RequestItem baseRequestItem;

    @Autowired
    public ItemServiceTest(ItemService itemService) {
        this.itemService = itemService;
    }

    @BeforeEach
    public void beforeEach() {
        this.baseRequestItem = new RequestItem("Base testItem",
            "Base testItemDescription",
            new BigDecimal("48.59"),
            ItemType.CLOTHES);
    }

    @Test
    public void testExistsById() {
        long notExistItemId = 1001;
        ResponseItem responseItem = itemService.saveItem(baseRequestItem);
        long itemId = responseItem.id();
        assertTrue(itemService.existsById(itemId));
        assertFalse(itemService.existsById(notExistItemId));
    }

    @Test
    public void testFindItemByName() {
        ResponseItem responseItem = itemService.saveItem(baseRequestItem);
        Optional<ResponseItem> responseItemName = itemService.findItemByName(responseItem.name());
        assertTrue(responseItemName.isPresent());
        assertEquals(baseRequestItem.name(), responseItemName.get().name());
    }

    @Test
    public void testUpdateItem() {
        ResponseItem responseItem = itemService.saveItem(baseRequestItem);
        RequestItem updateRequestItem = new RequestItem("New testItem",
            "New testItemDescription",
            new BigDecimal("20.15"),
            ItemType.EQUIPMENT);
        ResponseItem updatedItem = itemService.updateItem(responseItem.id(), updateRequestItem);
        assertEquals(updateRequestItem.name(), updatedItem.name());
        assertEquals(updateRequestItem.description(), updatedItem.description());
        assertEquals(updateRequestItem.price(), updatedItem.price());
        assertEquals(updateRequestItem.itemType(), updatedItem.itemType());
    }



}
