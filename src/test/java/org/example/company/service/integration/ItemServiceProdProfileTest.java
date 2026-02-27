package org.example.company.service.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import java.math.BigDecimal;
import java.net.http.HttpRequest;
import org.example.company.controller.EmployeeController;
import org.example.company.dto.request.RequestEmployee;
import org.example.company.dto.request.RequestItem;
import org.example.company.dto.response.ResponseItem;
import org.example.company.model.ItemType;
import org.example.company.service.DiscountService;
import org.example.company.service.ItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("prod")
public class ItemServiceProdProfileTest extends BaseTestConfig {

    private final ItemService itemService;
    private final DiscountService discountService;
    private final EmployeeController employeeController;

    @Autowired
    public ItemServiceProdProfileTest(ItemService itemService, DiscountService discountService, EmployeeController employeeController) {
        this.itemService = itemService;
        this.discountService = discountService;
        this.employeeController = employeeController;
    }

    @Test
    public void testCheckDiscountPriceForProdProfile() {
        RequestItem baseRequestItem = new RequestItem("Base testItem",
            "Base testItemDescription",
            new BigDecimal("48.59"),
            ItemType.CLOTHES);

        ResponseItem baseResponseItem = itemService.saveItem(baseRequestItem);
        BigDecimal updatedPrice = itemService.checkDiscountPrice(baseResponseItem.id());
        assertNotEquals(baseRequestItem.price(), updatedPrice);
        BigDecimal discountRate = discountService.calculateDiscount(baseRequestItem.itemType().getDiscountValue());
        BigDecimal expectedPrice = baseRequestItem.price().subtract(baseRequestItem.price().multiply(discountRate));
        assertEquals(expectedPrice, updatedPrice);
    }


}
