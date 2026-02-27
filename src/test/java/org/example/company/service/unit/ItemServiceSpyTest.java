package org.example.company.service.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import java.util.Optional;
import jakarta.persistence.EntityNotFoundException;
import org.example.company.dto.request.RequestItem;
import org.example.company.dto.response.ResponseItem;
import org.example.company.model.Item;
import org.example.company.model.ItemType;
import org.example.company.repository.ItemRepository;
import org.example.company.service.ItemService;
import org.example.company.service.PriceCalculatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ItemServiceSpyTest {

    @Mock
    private ItemRepository itemRepository;

    @Spy
    private PriceCalculatorService priceCalculatorService = new PriceCalculatorService();

    private ItemService itemService;

    @BeforeEach
    public void init() {
        itemService = spy(new ItemService(priceCalculatorService, itemRepository));
    }

    @Test
    public void testSaveItem() {
        RequestItem requestItem =
            new RequestItem("t-shirt", "cotton", new BigDecimal("78.5"), ItemType.CLOTHES);

        long itemId = 1L;
        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> {
            Item itemResult = invocation.getArgument(0);
            itemResult.setId(itemId);
            return itemResult;
        });

        ResponseItem responseItem = itemService.saveItem(requestItem);
        assertEquals(requestItem.name(), responseItem.name());
        assertEquals(requestItem.description(), responseItem.description());
        assertEquals(requestItem.price(), responseItem.price());
        assertEquals(requestItem.itemType(), responseItem.itemType());
        assertEquals(itemId, responseItem.id());
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    public void testCheckDiscountPrice() {
        long itemId = 1L;
        BigDecimal basePrice = new BigDecimal("100");
        BigDecimal discountedPrice = new BigDecimal("85");
        Item item = new Item(new RequestItem("shirt", "cotton", basePrice, ItemType.CLOTHES));
        item.setId(itemId);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        doReturn(discountedPrice).when(priceCalculatorService).getDiscountPrice(any(BigDecimal.class), any(ItemType.class));
        BigDecimal result = itemService.checkDiscountPrice(itemId);
        assertEquals(discountedPrice, result);
        verify(itemRepository).findById(itemId);
        verify(priceCalculatorService).getDiscountPrice(eq(basePrice), eq(ItemType.CLOTHES));
    }

    @Test
    public void testCheckDiscountPrice_itemNotFound() {
        long itemId = 999L;
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> itemService.checkDiscountPrice(itemId));
        verify(itemRepository).findById(itemId);
    }
}
