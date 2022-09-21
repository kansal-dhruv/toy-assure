package com.increff.ta.pojo;

import com.increff.ta.commons.model.enums.ChannelInvoiceType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
public class ChannelPojo extends AbstractPojo {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  private String name;

  @Enumerated(value = EnumType.STRING)
  @NotNull
  private ChannelInvoiceType invoiceType;

}