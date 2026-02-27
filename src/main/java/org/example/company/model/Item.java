package org.example.company.model;

import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.example.company.dto.request.RequestItem;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "items")
public class Item extends BaseEntity {


    @Column(nullable = false)
    private String name;

    @Column(length = 350)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private ItemType type;

    public Item(RequestItem requestItem) {
        this.name = requestItem.name();
        this.description = requestItem.description();
        this.price = requestItem.price();
        this.type = requestItem.itemType();
    }

    public Item(String name, String description, BigDecimal price, ItemType type) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.type = type;
    }

    public void updateItem(RequestItem requestItem){
        this.name = requestItem.name();
        this.description = requestItem.description();
        this.price = requestItem.price();
        this.type = requestItem.itemType();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item other)) return false;
        return id != 0 && id == other.id;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


}

