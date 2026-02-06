package org.example.company.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.company.dto.request.RequestItem;
import org.example.company.dto.response.ResponseItem;
import org.example.company.models.Item;
import org.example.company.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {

    private final PriceCalculatorService priceCalculatorService;
    private final ItemRepository itemRepository;

    @Transactional(readOnly = true)
    public Optional<ResponseItem> findItemByName(String name) {
        return itemRepository.findByName(name).map(ResponseItem::fromItem);
    }

    @Transactional(readOnly = true)
    public boolean existsById(long id) {
        return itemRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    public ResponseItem getItem(long id) {
        var item = itemRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Item not found for id= " + id));
        return ResponseItem.fromItem(item);
    }

    @Transactional(readOnly = true)
    public List<ResponseItem> getAllItems() {
        List<Item> all = itemRepository.findAll();
        return all.stream().map(ResponseItem::fromItem).collect(Collectors.toList());
    }

    @Transactional
    public ResponseItem saveItem(RequestItem requestItem) {
        Item item = new Item(requestItem.name(), requestItem.description(), requestItem.price(), requestItem.itemType());
        itemRepository.save(item);
        return ResponseItem.fromItem(item);
    }

    @Transactional
    public ResponseItem updateItem(long id, RequestItem requestItem) {
        var item = itemRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Item not found for id= " + id));
        item.updateItem(requestItem);
        return ResponseItem.fromItem(item);
    }


    @Transactional(readOnly = true)
    public BigDecimal checkDiscountPrice(long itemId) {
        var item = itemRepository.findById(itemId).orElseThrow(() -> new EntityNotFoundException("Item not found for id= " + itemId));
        return priceCalculatorService.getDiscountPrice(item.getPrice(), item.getType());
    }

    @Transactional
    public void deleteItem(long itemId) {
        itemRepository.findById(itemId).ifPresentOrElse(
            itemRepository::delete,() -> {
                log.error("Item not found for id= {}", itemId);
                throw new EntityNotFoundException("Item not found for id= " + itemId);
            }
        );
    }


}
