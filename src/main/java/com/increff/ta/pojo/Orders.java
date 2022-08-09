package com.increff.ta.pojo;

import com.increff.ta.enums.OrderStatus;

import javax.persistence.*;

@Entity
@Table(name = "assure_order")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "clientId")
    private User client;

    @ManyToOne
    @JoinColumn(name = "customerId")
    private User customer;

    @ManyToOne
    @JoinColumn(name = "channelId")
    private Channel channel;

    @Column(unique = true)
    private String channelOrderId;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getChannelOrderId() {
        return channelOrderId;
    }

    public void setChannelOrderId(String channelOrderId) {
        this.channelOrderId = channelOrderId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
