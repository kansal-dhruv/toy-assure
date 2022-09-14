package com.increff.ta.dao;

import com.increff.ta.pojo.Channel;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

@Repository
@Transactional
public class ChannelDao extends AbstractDao {

    private final String select_by_channel_name = "SELECT e from Channel e where e.name=:channelName";

    public Channel insertOrUpdate(Channel channel) {
        return em.merge(channel);
    }

    public Channel findByChannelName(String channelName) {
        TypedQuery<Channel> query = getQuery(select_by_channel_name, Channel.class);
        query.setParameter("channelName", channelName);
        return query.getResultList().stream().findFirst().orElse(null);
    }

    ;
}