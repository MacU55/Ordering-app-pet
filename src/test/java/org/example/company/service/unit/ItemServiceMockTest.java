package org.example.company.service.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import org.example.company.dto.request.RequestItem;
import org.example.company.dto.response.ResponseItem;
import org.example.company.model.Item;
import org.example.company.model.ItemType;
import org.example.company.repository.ItemRepository;
import org.example.company.service.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ItemServiceMockTest {

    private RequestItem requestItem;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    @BeforeEach
    public void createRequestItem() {
        requestItem =
            new RequestItem("t-shirt", "cotton", new BigDecimal("78.5"), ItemType.CLOTHES);
    }

    @Test
    public void testSaveItem() {
        long itemId = 1;
        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> {
            Item itemResult = invocation.getArgument(0);
            itemResult.setId(itemId);
            return itemResult;
        });
        ResponseItem responseItem = itemService.saveItem(requestItem);
        assertEquals(responseItem.name(), requestItem.name());
        assertEquals(responseItem.description(), requestItem.description());
        assertEquals(responseItem.price(), requestItem.price());
        assertEquals(responseItem.itemType(), requestItem.itemType());
        assertEquals(itemId, responseItem.id());

        verify(itemRepository, times(1)).save(any(Item.class));
        verify(itemRepository).save(argThat(item ->
            (item.getName().equals(requestItem.name()) && item.getPrice().equals(requestItem.price()))
            && (item.getDescription().equals(requestItem.description()) && item.getType().equals(requestItem.itemType()))
        ));
    }

}
