package com.increff.ta.utils;

import com.increff.ta.enums.ChannelInvoiceType;
import com.increff.ta.enums.OrderStatus;
import com.increff.ta.enums.UserType;
import com.increff.ta.pojo.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestUtils {
    public static User getClientUser(){
        User client = new User();
        client.setName("client");
        client.setType(UserType.CLIENT);
        return client;
    }
    public static User getCustomerUser(){
        User client = new User();
        client.setName("customer");
        client.setType(UserType.CUSTOMER);
        return client;
    }
    public static Product getProduct(){
        String sampleSkuId = "sku-2";
        Product product = new Product();
        product.setGlobalSkuId(1L);
        product.setClient(getClientUser());
        product.setName("Name One");
        product.setClientSkuId(sampleSkuId);
        product.setMrp(200D);
        product.setBrandId("123");
        product.setDescription("Description");
        return product;
    }

    public static Inventory getInventory(){
        Inventory inv = new Inventory();
        inv.setAllocatedQuantity(0L);
        inv.setAvailableQuantity(20L);
        inv.setId(1L);
        inv.setProduct(getProduct());
        inv.setFulfilledQuantity(0L);
        return inv;
    }

    public static Inventory getInventory2(){
        Inventory inv = new Inventory();
        inv.setAllocatedQuantity(0L);
        inv.setAvailableQuantity(20L);
        inv.setId(2L);
        inv.setProduct(getProduct());
        inv.setFulfilledQuantity(0L);
        return inv;
    }

    public static Product getProduct2(){
        String sampleSkuId = "sku-3";
        Product product = new Product();
        product.setGlobalSkuId(2L);
        product.setClient(getClientUser());
        product.setName("Name Two");
        product.setClientSkuId(sampleSkuId);
        product.setMrp(400D);
        product.setBrandId("123");
        product.setDescription("Description");
        return product;
    }
    public static Bin getBin(){
        Bin bin = new Bin();
        bin.setBinId(1L);
        return bin;
    }

    public static Bin getBin2(){
        Bin bin = new Bin();
        bin.setBinId(2L);
        return bin;
    }

    public static BinSku getBinSku(){
        BinSku binSku = new BinSku();
        binSku.setProduct(getProduct());
        binSku.setQuantity(40L);
        binSku.setBin(getBin());
        binSku.setId(1L);
        return binSku;
    }

    public static BinSku getBinSku2(){
        BinSku binSku = new BinSku();
        binSku.setProduct(getProduct2());
        binSku.setQuantity(10L);
        binSku.setBin(getBin());
        binSku.setId(1L);
        return binSku;
    }

    public static List<BinSku> getBinSkuListProduct2(){
        BinSku binSku = new BinSku();
        binSku.setProduct(getProduct2());
        binSku.setQuantity(15L);
        binSku.setBin(getBin());
        binSku.setId(1L);

        BinSku binSku2 = new BinSku();
        binSku.setProduct(getProduct2());
        binSku.setQuantity(15L);
        binSku.setBin(getBin2());
        binSku.setId(1L);
        return Arrays.asList(binSku, binSku2);
    }

    public static Channel getChannel(){
        Channel channel = new Channel();
        channel.setName("FLIPKART");
        channel.setInvoiceType(ChannelInvoiceType.CHANNEL);
        channel.setId(1L);
        return channel;
    }

    public static Channel getInternalChannel(){
        Channel channel = new Channel();
        channel.setName("INTERNAL");
        channel.setInvoiceType(ChannelInvoiceType.SELF);
        channel.setId(1L);
        return channel;
    }

    public static ChannelListing getChannelListing(){
        ChannelListing channelListing = new ChannelListing();
        channelListing.setChannel(getChannel());
        channelListing.setChannelSkuId("Channel-FLKRT-1");
        channelListing.setProduct(getProduct());
        channelListing.setUser(getClientUser());
        channelListing.setId(1L);
        return channelListing;
    }

    public static Orders getCreatedOrder(){
        Orders order = new Orders();
        order.setStatus(OrderStatus.CREATED);
        order.setChannelOrderId("flkrt-order-1");
        order.setChannel(getChannel());
        order.setId(1L);
        order.setClient(getClientUser());
        order.setCustomer(getCustomerUser());
        return order;
    }

    public static Orders getAllocatedOrder(){
        Orders order = new Orders();
        order.setStatus(OrderStatus.ALLOCATED);
        order.setChannelOrderId("flkrt-order-1");
        order.setChannel(getChannel());
        order.setId(1L);
        order.setClient(getClientUser());
        order.setCustomer(getCustomerUser());
        return order;
    }

    public static List<OrderItem> getOrderItems(){
        List<OrderItem> orderItems = new ArrayList<>();
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(getCreatedOrder());
        orderItem.setAllocatedQuanity(0L);
        orderItem.setOrderedQuantity(10L);
        orderItem.setProduct(getProduct());
        orderItem.setFullfilledQuanity(0L);
        orderItem.setSellingPricePerUnit(1000D);
        orderItem.setId(1L);

        OrderItem orderItem2 = new OrderItem();
        orderItem2.setOrder(getCreatedOrder());
        orderItem2.setAllocatedQuanity(0L);
        orderItem2.setOrderedQuantity(10L);
        orderItem2.setProduct(getProduct2());
        orderItem2.setFullfilledQuanity(0L);
        orderItem2.setSellingPricePerUnit(1000D);
        orderItem2.setId(1L);

        orderItems.add(orderItem);
        orderItems.add(orderItem2);
        return orderItems;
    }

    public static List<OrderItem> getOrderItems2(){
        List<OrderItem> orderItems = new ArrayList<>();
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(getCreatedOrder());
        orderItem.setAllocatedQuanity(0L);
        orderItem.setOrderedQuantity(20L);
        orderItem.setProduct(getProduct());
        orderItem.setFullfilledQuanity(0L);
        orderItem.setSellingPricePerUnit(1000D);
        orderItem.setId(1L);

        OrderItem orderItem2 = new OrderItem();
        orderItem2.setOrder(getCreatedOrder());
        orderItem2.setAllocatedQuanity(0L);
        orderItem2.setOrderedQuantity(30L);
        orderItem2.setProduct(getProduct2());
        orderItem2.setFullfilledQuanity(0L);
        orderItem2.setSellingPricePerUnit(1000D);
        orderItem2.setId(1L);

        orderItems.add(orderItem);
        orderItems.add(orderItem2);
        return orderItems;
    }

}
