package com.increff.ta.api;

import com.increff.ta.constants.Constants;
import com.increff.ta.dao.ChannelDao;
import com.increff.ta.dao.ChannelListingDao;
import com.increff.ta.dao.ProductDao;
import com.increff.ta.dao.UserDao;
import com.increff.ta.enums.UserType;
import com.increff.ta.pojo.Channel;
import com.increff.ta.pojo.ChannelListing;
import com.increff.ta.pojo.Product;
import com.increff.ta.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ChannelApi {

    @Autowired
    private ChannelDao channelDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ChannelListingDao channelListingDao;

    @Transactional
    public void addChannel(Channel channel) {
        if (channelDao.findByChannelName(channel.getName()) != null) {
            throw new ApiException(Constants.CODE_INAVLID_CHANNEL, Constants.MSG_INVALID_CHANNEL, "Channel:" + channel.getName() + "already exists");
        } else {
            channelDao.insertOrUpdate(channel);
        }
    }

    @Transactional
    public void addChannelListing(String channelName, String clientName, String channelSkuId, String clientSkuId) {
        User client = userDao.selectByNameAndType(clientName, UserType.CLIENT);
        Channel channel = channelDao.findByChannelName(channelName);
        if (client == null || client.getType() != UserType.CLIENT) {
            throw new ApiException(Constants.CODE_INVALID_USER, Constants.MSG_INVALID_USER,
                    "Client with name: " + clientName + "doesn't exist");
        }
        if (channel == null) {
            throw new ApiException(Constants.CODE_INAVLID_CHANNEL, Constants.MSG_INVALID_CHANNEL,
                    "Channel with name: " + channelName + "doesn't exist");
        }
        Product product = productDao.findByClientSkuId(clientSkuId);
        if (product == null) {
            throw new ApiException(Constants.CODE_PRODUCT_NOT_FOUND, Constants.MSG_PRODUCT_NOT_FOUND,
                    "Product with clientSkuId" + clientSkuId + "not present");
        }
        if (channelListingDao.findByChannelSkuId(channelSkuId) != null) {
            throw new ApiException(Constants.CODE_DUPLICATE_CHANNEL_ORDER_ID, Constants.MSG_DUPLICATE_CHANNEL_ORDER_ID,
                    "Channel Listing already exists");
        }
        ChannelListing channelListing = new ChannelListing();
        channelListing.setChannelId(channel.getId());
        channelListing.setClientId(client.getId());
        channelListing.setChannelSkuId(channelSkuId);
        channelListing.setGlobalSkuId(product.getGlobalSkuId());
        channelListingDao.saveOrUpdate(channelListing);
    }

}