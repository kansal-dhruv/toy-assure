package com.increff.ta.api;

import com.increff.ta.dao.ChannelDao;
import com.increff.ta.dao.ChannelListingDao;
import com.increff.ta.pojo.ChannelListingPojo;
import com.increff.ta.pojo.ChannelPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ChannelApi {

  private final ChannelDao channelDao;

  private final ChannelListingDao channelListingDao;

  @Autowired
  public ChannelApi(ChannelDao channelDao, ChannelListingDao channelListingDao) {
    this.channelDao = channelDao;
    this.channelListingDao = channelListingDao;
  }

  public void addChannel(ChannelPojo channelPojo) {
    channelDao.insertOrUpdate(channelPojo);
  }

  public ChannelPojo getChannelByName(String channelName) {
    return channelDao.findByChannelName(channelName);
  }

  public void addChannelListing(ChannelListingPojo channelListingPojo) {
    channelListingDao.saveOrUpdate(channelListingPojo);
  }

  public ChannelListingPojo findByChannelSkuId(String channelSkuId) {
    return channelListingDao.findByChannelSkuId(channelSkuId);
  }

  public ChannelListingPojo findByChannelIdAndChannelSkuIdAndClientId(Long channelId, String channelSkuId, Long clientId) {
    return channelListingDao.findByChannelIdAndChannelSkuIdAndClientId(channelId, channelSkuId, clientId);
  }
}