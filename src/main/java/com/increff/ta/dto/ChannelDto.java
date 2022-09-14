package com.increff.ta.dto;

import com.increff.ta.api.ApiException;
import com.increff.ta.api.ChannelApi;
import com.increff.ta.api.ProductApi;
import com.increff.ta.api.UserApi;
import com.increff.ta.constants.Constants;
import com.increff.ta.enums.UserType;
import com.increff.ta.dto.helper.ChannelDtoHelper;
import com.increff.ta.model.ChannelForm;
import com.increff.ta.model.ChannelListingCSV;
import com.increff.ta.pojo.Channel;
import com.increff.ta.pojo.Product;
import com.increff.ta.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ChannelDto {

  @Autowired
  private ChannelApi channelApi;

  @Autowired
  private UserApi userApi;

  @Autowired
  private ProductApi productApi;


  public void createChannel(ChannelForm channelForm) {

    if (channelApi.getChannelByName(channelForm.getName()) == null) {
      Channel channel = ChannelDtoHelper.convertChannelFormToPojo(channelForm);
      channelApi.addChannel(channel);
    } else {
      throw new ApiException(Constants.CODE_INAVLID_CHANNEL, Constants.MSG_INVALID_CHANNEL,
          "Channel with name: " + channelForm.getName() + " already exists");
    }
  }

  public void addChannelListing(String channelName, String clientName, MultipartFile csvFile) {
    List<ChannelListingCSV> channelListingDetails = ChannelDtoHelper.validateAndParseCSV(csvFile);
    for (ChannelListingCSV channelListingCSV : channelListingDetails) {
      User client = userApi.getUserByNameAndType(clientName, UserType.CLIENT);
      Channel channel = channelApi.getChannelByName(channelName);
      if (client == null || client.getType() != UserType.CLIENT) {
        throw new ApiException(Constants.CODE_INVALID_USER, Constants.MSG_INVALID_USER,
            "Client with name: " + clientName + "doesn't exist");
      }
      if (channel == null) {
        throw new ApiException(Constants.CODE_INAVLID_CHANNEL, Constants.MSG_INVALID_CHANNEL,
            "Channel with name: " + channelName + "doesn't exist");
      }
      Product product = productApi.getProductByClientSkuID(channelListingCSV.getClientSkuId());
      if (product == null) {
        throw new ApiException(Constants.CODE_PRODUCT_NOT_FOUND, Constants.MSG_PRODUCT_NOT_FOUND,
            "Product with clientSkuId" + channelListingCSV.getClientSkuId() + "not present");
      }
      if (channelApi.findByChannelSkuId(channelListingCSV.getChannelSkuId()) != null) {
        throw new ApiException(Constants.CODE_DUPLICATE_CHANNEL_ORDER_ID, Constants.MSG_DUPLICATE_CHANNEL_ORDER_ID,
            "Channel Listing already exists");
      }
      channelApi.addChannelListing(ChannelDtoHelper.convertCSVtoPojo(channelListingCSV, channel, client, product));
    }
  }
}