package com.increff.ta.pojo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
public class InventoryPojo extends AbstractPojo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  @NotNull
  private Long globalSkuId;

  @NotNull
  @Min(0)
  @ColumnDefault("0")
  private Long availableQuantity = 0L;

  @NotNull
  @Min(0)
  @ColumnDefault("0")
  private Long allocatedQuantity = 0L;

  @NotNull
  @Min(0)
  @ColumnDefault("0")
  private Long fulfilledQuantity = 0L;

}