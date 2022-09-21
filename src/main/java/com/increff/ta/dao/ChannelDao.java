package com.increff.ta.dao;

import com.increff.ta.pojo.ChannelPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;

@Repository
public class ChannelDao extends AbstractDao {

  private final String select_by_channel_name = "SELECT e from ChannelPojo e where e.name=:channelName";

  public ChannelPojo insertOrUpdate(ChannelPojo channelPojo) {
    return em.merge(channelPojo);
  }

  public ChannelPojo findByChannelName(String channelName) {
    TypedQuery<ChannelPojo> query = getQuery(select_by_channel_name, ChannelPojo.class);
    query.setParameter("channelName", channelName);
    return query.getResultList().stream().findFirst().orElse(null);
  }

  ;
}