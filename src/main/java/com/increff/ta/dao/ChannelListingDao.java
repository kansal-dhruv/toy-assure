package com.increff.ta.dao;

import com.increff.ta.pojo.ChannelListing;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

@Repository
@Transactional
public class ChannelListingDao extends AbstractDao {

    private final String select_by_channelSku_channelId_globalSku_id =
            "SELECT e FROM ChannelListing e where e.channelSkuId=:channelSkuId";

    private final String select_by_channelSku_channelId_clientId =
            "SELECT e from ChannelListing e where e.channelId=:channelId and e.channelSkuId=:channelSkuId and e.clientId=:clientId";

    public ChannelListing saveOrUpdate(ChannelListing channelListing) {
        return em.merge(channelListing);
    }

    public ChannelListing findByChannelSkuId(String channelSkuId) {
        TypedQuery<ChannelListing> query = getQuery(select_by_channelSku_channelId_globalSku_id, ChannelListing.class);
        query.setParameter("channelSkuId", channelSkuId);
        return query.getResultList().stream().findFirst().orElse(null);
    }

    public ChannelListing findByChannelIdAndChannelSkuidAndClientId(Long channelId, String channelSkuId, Long clientId) {
        TypedQuery<ChannelListing> query = getQuery(select_by_channelSku_channelId_clientId, ChannelListing.class);
        query.setParameter("channelId", channelId);
        query.setParameter("channelSkuId", channelSkuId);
        query.setParameter("clientId", clientId);
        return query.getResultList().stream().findFirst().orElse(null);
    }
}