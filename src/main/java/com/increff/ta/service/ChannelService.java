package com.increff.ta.service;

import com.increff.ta.constants.Constants;
import com.increff.ta.dao.ChannelDao;
import com.increff.ta.dao.ChannelListingDao;
import com.increff.ta.dao.ProductDao;
import com.increff.ta.dao.UserDao;
import com.increff.ta.model.ChannelListingCSV;
import com.increff.ta.pojo.Channel;
import com.increff.ta.pojo.ChannelListing;
import com.increff.ta.pojo.Product;
import com.increff.ta.pojo.User;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class ChannelService {

    @Autowired
    private ChannelDao channelDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ChannelListingDao channelListingDao;

    public void addChannel(Channel channel) {
        if (channelDao.findByChannelName(channel.getName()) != null) {
            throw new ApiException(Constants.CODE_INAVLID_CHANNEL, Constants.MSG_INVALID_CHANNEL, "Channel:" + channel.getName() + "already exists");
        } else {
            channelDao.insertOrUpdate(channel);
        }
    }

    public void addChannelListing(String channelName, String clientName, MultipartFile csvFile) {
        List<ChannelListingCSV> channelListingDetails = null;
        try {
            channelListingDetails = new CsvToBeanBuilder(new InputStreamReader(new ByteArrayInputStream(csvFile.getBytes()), "UTF8"))
                    .withType(ChannelListingCSV.class).withSkipLines(1).build().parse();
        } catch (IOException e) {
            throw new ApiException(Constants.CODE_ERROR_PARSING_CSV_FILE, Constants.MSG_ERROR_PARSING_CSV_FILE);
        }
        User client = userDao.selectByName(clientName);
        Channel channel = channelDao.findByChannelName(channelName);
        for (ChannelListingCSV channelListingDetail : channelListingDetails) {
            Product product = productDao.findByClientSkuId(channelListingDetail.getClientSkuId());
            if (product == null) {
                throw new ApiException(Constants.CODE_PRODUCT_NOT_FOUND, Constants.MSG_PRODUCT_NOT_FOUND,
                        "Product with clientSkuId" + channelListingDetail.getClientSkuId() + "not present");
            }
            if (channelListingDao.findByChannelSkuIdAndChannelIdAndGlobalSkuId(channel.getId(), channelListingDetail.getChannelSkuId(), product.getGlobalSkuId()) != null) {
                throw new ApiException(Constants.CODE_DUPLICATE_CHANNEL_ORDER_ID, Constants.MSG_DUPLICATE_CHANNEL_ORDER_ID,
                        "Channel Listing already exists");
            }
            ChannelListing channelListing = new ChannelListing();
            channelListing.setChannel(channel);
            channelListing.setUser(client);
            channelListing.setChannelSkuId(channelListingDetail.getChannelSkuId());
            channelListing.setProduct(product);
            channelListingDao.saveOrUpdate(channelListing);
        }
    }
}
