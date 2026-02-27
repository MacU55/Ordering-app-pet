package org.example.company.service;

import java.math.BigDecimal;
import lombok.extern.slf4j.Slf4j;
import org.example.company.model.ItemType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PriceCalculatorService {

    private DiscountService discountService;

    @Autowired(required = false)
    public void setDiscountService(DiscountService discountService) {
        this.discountService = discountService;
    }

    public BigDecimal getDiscountPrice(BigDecimal basePrice, ItemType itemType) {
        if (discountService != null) {
            double discountValue = this.getDiscountValue(itemType);
            BigDecimal discountRate = discountService.calculateDiscount(discountValue);
            BigDecimal result = basePrice.subtract(basePrice.multiply(discountRate));
            log.info("DiscountService is ON. Calculated price with discount: {}", result);
            return result;
        } else {
            log.debug("Base price is not changed, DiscountService is null. DiscountService is available only for prod environment.");
            return basePrice;
        }
    }

    private double getDiscountValue(ItemType itemType) {
        return switch (itemType) {
            case FOOD, EQUIPMENT, CLOTHES, UNDEFINED -> itemType.getDiscountValue();
        };
    }
}
