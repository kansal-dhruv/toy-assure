package com.increff.ta.pojo;

import com.increff.ta.enums.ChannelInvoiceType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "assure_channel")
@Getter
@Setter
public class Channel extends AbstractModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @Enumerated(value = EnumType.STRING)
    @NotNull
    private ChannelInvoiceType invoiceType;

}