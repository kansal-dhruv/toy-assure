package com.increff.ta.pojo;

import javax.persistence.*;

@Entity
@Table(name = "assure_channel_lisitng")
public class ChannelListing extends AbstractModel{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "channelId")
    private Channel channel;

    private String channelSkuId;

    @ManyToOne
    @JoinColumn(name = "clientId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "globalSkuId")
    private Product product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getChannelSkuId() {
        return channelSkuId;
    }

    public void setChannelSkuId(String channelSkuId) {
        this.channelSkuId = channelSkuId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
