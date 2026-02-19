package org.example.company.service.converter;

import org.example.company.dto.request.RequestItem;
import org.example.company.dto.response.ResponseItem;
import org.example.company.model.Item;
import org.springframework.stereotype.Component;

@Component
public class ItemConverter extends ConverterImpl<Item, ResponseItem, RequestItem> {
    public ItemConverter() {
    }

    @Override
    public ResponseItem convertToDTO(Item item) {
        return new ResponseItem(item.getId(), item.getName(), item.getPrice(), item.getDescription(), item.getType());
    }

    @Override
    public Item convertToEntity(RequestItem requestItem) {
        return new Item(requestItem.name(), requestItem.description(), requestItem.price(), requestItem.itemType());
    }

//    @Override
//    public Item convertToEntity(RequestItem requestItem, Item item) {
//        return new Item(requestItem);
//    }

}
