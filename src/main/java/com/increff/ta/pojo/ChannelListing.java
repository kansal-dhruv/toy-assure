package com.increff.ta.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "assure_channel_lisitng",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {
                        "channelSkuId",
                        "channelId",
                        "globalSkuId"
                }))
@Setter
@Getter
public class ChannelListing extends AbstractModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long channelId;

    @NotBlank
    private String channelSkuId;

    @NotNull
    private Long clientId;

    @NotNull
    private Long globalSkuId;
}