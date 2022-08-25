package com.increff.ta.dao;

import com.increff.ta.pojo.ChannelListing;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;

@Repository
public class ChannelListingDao extends AbstractDao {

    private final String select_by_channelSku_channelId_globalSku_id =
            "SELECT e FROM ChannelListing e where e.channel.id=:channelId and e.channelSkuId=:channelSkuId and e.product.globalSkuId=:globalSkuId";

    private final String select_by_channelSku_channelId_clientId =
            "SELECT e from ChannelListing e where e.channel.id=:channelId and e.channelSkuId=:channelSkuId and e.user.id=:clientId";

    public ChannelListing saveOrUpdate(ChannelListing channelListing) {
        return em.merge(channelListing);
    }

    public ChannelListing findByChannelSkuIdAndChannelIdAndGlobalSkuId(Long channelId, String channelSkuId, Long globalSkuId) {
        TypedQuery<ChannelListing> query = getQuery(select_by_channelSku_channelId_globalSku_id, ChannelListing.class);
        query.setParameter("channelId", channelId);
        query.setParameter("channelSkuId", channelSkuId);
        query.setParameter("globalSkuId", globalSkuId);
        return query.getResultList().stream().findFirst().orElse(null);
    }

    public ChannelListing findByChannelIdAndChannelSkuidAndClientId(Long clientId, Long channelId, String channelSkuId) {
        TypedQuery<ChannelListing> query = getQuery(select_by_channelSku_channelId_clientId, ChannelListing.class);
        query.setParameter("channelId", channelId);
        query.setParameter("channelSkuId", channelSkuId);
        query.setParameter("clientId", clientId);
        return query.getResultList().stream().findFirst().orElse(null);
    }
}
