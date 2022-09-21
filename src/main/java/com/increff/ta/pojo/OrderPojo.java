package com.increff.ta.pojo;

import com.increff.ta.commons.model.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
public class OrderPojo extends AbstractPojo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  private Long clientId;

  @NotNull
  private Long customerId;

  @NotNull
  private Long channelId;

  @Column(unique = true)
  private String channelOrderId;

  @Enumerated(value = EnumType.STRING)
  private OrderStatus status;
}