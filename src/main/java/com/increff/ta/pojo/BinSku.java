package com.increff.ta.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "assure_bin_sku",
    uniqueConstraints = @UniqueConstraint(columnNames = {"binId", "globalSkuId"}))
@Getter
@Setter
public class BinSku extends AbstractModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long binId;

    @NotNull
    private Long globalSkuId;

    @Min(0)
    @NotNull
    private Long quantity;
}