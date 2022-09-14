package com.increff.ta.api;

import com.increff.ta.dao.ChannelDao;
import com.increff.ta.dao.ChannelListingDao;
import com.increff.ta.pojo.Channel;
import com.increff.ta.pojo.ChannelListing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChannelApi {

  @Autowired
  private ChannelDao channelDao;

  @Autowired
  private ChannelListingDao channelListingDao;

  public void addChannel(Channel channel) {
    channelDao.insertOrUpdate(channel);
  }

  public Channel getChannelByName(String channelName) {
    return channelDao.findByChannelName(channelName);
  }

  public void addChannelListing(ChannelListing channelListing) {
    channelListingDao.saveOrUpdate(channelListing);
  }

  public ChannelListing findByChannelSkuId(String channelSkuId) {
    return channelListingDao.findByChannelSkuId(channelSkuId);
  }

  public ChannelListing findByChannelIdAndChannelSkuidAndClientId(Long channelId, String channelSkuId, Long clientId) {
    return channelListingDao.findByChannelIdAndChannelSkuidAndClientId(channelId, channelSkuId, clientId);
  }
}