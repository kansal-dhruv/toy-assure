package com.increff.ta.dao;

import com.increff.ta.pojo.ChannelListingPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;

@Repository
public class ChannelListingDao extends AbstractDao {

  private final String select_by_channelSku_channelId_globalSku_id =
      "SELECT e FROM ChannelListingPojo e where e.channelSkuId=:channelSkuId";

  private final String select_by_channelSku_channelId_clientId =
      "SELECT e from ChannelListingPojo e where e.channelId=:channelId and e.channelSkuId=:channelSkuId and e.clientId=:clientId";

  public ChannelListingPojo saveOrUpdate(ChannelListingPojo channelListingPojo) {
    return em.merge(channelListingPojo);
  }

  public ChannelListingPojo findByChannelSkuId(String channelSkuId) {
    TypedQuery<ChannelListingPojo> query = getQuery(select_by_channelSku_channelId_globalSku_id, ChannelListingPojo.class);
    query.setParameter("channelSkuId", channelSkuId);
    return query.getResultList().stream().findFirst().orElse(null);
  }

  public ChannelListingPojo findByChannelIdAndChannelSkuIdAndClientId(Long channelId, String channelSkuId, Long clientId) {
    TypedQuery<ChannelListingPojo> query = getQuery(select_by_channelSku_channelId_clientId, ChannelListingPojo.class);
    query.setParameter("channelId", channelId);
    query.setParameter("channelSkuId", channelSkuId);
    query.setParameter("clientId", clientId);
    return query.getResultList().stream().findFirst().orElse(null);
  }
}