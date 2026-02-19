package org.example.company.model;

import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(
    name = "order_items",
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"order_id", "item_id"}
    )
)
@Getter
@Setter
@NoArgsConstructor
@ToString
public class OrderItem extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(optional = false)
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private BigDecimal priceAtOrderTime;

    public OrderItem(Order order, Item item, int quantity, BigDecimal priceAtOrderTime) {
        this.order = order;
        this.item = item;
        this.quantity = quantity;
        this.priceAtOrderTime = priceAtOrderTime;
    }

    public OrderItem(Long itemId, Long orderId, int quantity) {
        this.item.id = itemId;
        this.order.id = orderId;
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderItem other)) return false;
        return id != 0 && id == other.id;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
