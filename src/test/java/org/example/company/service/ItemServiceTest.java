package org.example.company.service;

import org.example.company.dto.request.RequestItem;
import org.example.company.dto.response.ResponseItem;
import org.example.company.models.Item;
import org.example.company.models.ItemType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;


@Testcontainers
@SpringBootTest
public class ItemServiceTest {

    private final ItemService itemService;

    @Autowired
    public ItemServiceTest(ItemService itemService) {
        this.itemService = itemService;
    }

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.1")
        .withDatabaseName("test")
        .withUsername("test")
        .withPassword("test");

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }




    @Test
    public void testExistsById() {
        RequestItem requestItem = new RequestItem("testItem",
            "testItemDescription",
            BigDecimal.valueOf(55.59),
            ItemType.CLOTHES);
        Item item = new Item(requestItem.name(), requestItem.description(), requestItem.price(), requestItem.itemType());
        assertFalse(itemService.existsById(5L));
        ResponseItem responseItem = itemService.saveItem(requestItem);
        long itemId = responseItem.id();
        boolean itemExist = itemService.existsById(itemId);
        assertTrue(itemExist);
    }



}
