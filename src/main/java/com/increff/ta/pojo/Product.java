package com.increff.ta.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "assure_product", uniqueConstraints = @UniqueConstraint(columnNames = {"clientId", "clientSkuId"}))
@Getter
@Setter
public class Product extends AbstractModel {

    @NotBlank
    private String clientSkuId;

    @NotBlank
    private String name;

    @NotBlank
    private String brandId;

    @NotNull
    @Min(0)
    private Double mrp;

    @NotBlank
    private String description;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long globalSkuId;

    @NotNull
    private Long clientId;

}