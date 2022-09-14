package com.increff.ta.pojo;

import com.increff.ta.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "assure_order")
@Getter
@Setter
public class Orders extends AbstractModel {

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