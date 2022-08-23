package com.increff.ta.dao;

import com.increff.ta.pojo.Channel;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;

@Repository
public class ChannelDao extends AbstractDao {

    private final String select_by_channel_name = "SELECT e from Channel e where e.name=:channelName";

    public void insertOrUpdate(Channel channel) {
        em.persist(channel);
    }

    public Channel findByChannelName(String channelName) {
        TypedQuery<Channel> query = getQuery(select_by_channel_name, Channel.class);
        query.setParameter("channelName", channelName);
        return query.getResultList().stream().findFirst().orElse(null);
    }

    ;
}
