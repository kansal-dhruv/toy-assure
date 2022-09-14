package com.increff.ta.pojo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "assure_order_item",
    uniqueConstraints = @UniqueConstraint(columnNames = {"orderId", "globalSkuId"}) )
@Getter
@Setter
public class OrderItem extends AbstractModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long orderId;

    @NotNull
    private Long globalSkuId;

    @NotNull
    @Min(0)
    private Long orderedQuantity = 0L;

    @NotNull
    @Min(0)
    @ColumnDefault(value = "0")
    private Long allocatedQuanity = 0L;

    @NotNull
    @Min(0)
    @ColumnDefault(value = "0")
    private Long fullfilledQuanity = 0L;

    @NotNull
    @Min(0)
    private Double sellingPricePerUnit;

}