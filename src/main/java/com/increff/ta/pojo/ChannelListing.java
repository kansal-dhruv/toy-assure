package com.increff.ta.pojo;

import javax.persistence.*;

@Entity
@Table(name = "assure_channel_lisitng",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {
                        "channelSkuId",
                        "channelId",
                        "globalSkuId"
                }))
public class ChannelListing extends AbstractModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long channelId;

    private String channelSkuId;

    private Long clientId;

    private Long globalSkuId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getChannelSkuId() {
        return channelSkuId;
    }

    public void setChannelSkuId(String channelSkuId) {
        this.channelSkuId = channelSkuId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getGlobalSkuId() {
        return globalSkuId;
    }

    public void setGlobalSkuId(Long globalSkuId) {
        this.globalSkuId = globalSkuId;
    }
}
