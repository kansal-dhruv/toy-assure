package com.increff.ta.dto;

import com.increff.ta.api.ChannelApi;
import com.increff.ta.api.ProductApi;
import com.increff.ta.api.UserApi;
import com.increff.ta.commons.exception.ApiException;
import com.increff.ta.commons.model.ChannelForm;
import com.increff.ta.commons.model.ChannelListingCSV;
import com.increff.ta.commons.model.enums.UserType;
import com.increff.ta.constants.Constants;
import com.increff.ta.dto.helper.ChannelDtoHelper;
import com.increff.ta.pojo.ChannelPojo;
import com.increff.ta.pojo.ProductPojo;
import com.increff.ta.pojo.UserPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Service
public class ChannelDto {

  private final ChannelApi channelApi;

  private final UserApi userApi;

  private final ProductApi productApi;

  @Autowired
  public ChannelDto(ChannelApi channelApi, UserApi userApi, ProductApi productApi) {
    this.channelApi = channelApi;
    this.userApi = userApi;
    this.productApi = productApi;
  }


  public void createChannel(ChannelForm channelForm) {
    if (Objects.isNull(channelApi.getChannelByName(channelForm.getName()))) {
      ChannelPojo channelPojo = ChannelDtoHelper.convertChannelFormToPojo(channelForm);
      channelApi.addChannel(channelPojo);
    } else {
      throw new ApiException(Constants.CODE_INAVLID_CHANNEL, Constants.MSG_INVALID_CHANNEL,
          "ChannelPojo with name: " + channelForm.getName() + " already exists");
    }
  }

  public void addChannelListing(String channelName, String clientName, MultipartFile csvFile) {
    List<ChannelListingCSV> channelListingDetails = ChannelDtoHelper.validateAndParseCSV(csvFile);
    for (ChannelListingCSV channelListingCSV : channelListingDetails) {
      UserPojo client = userApi.getUserByNameAndType(clientName, UserType.CLIENT);
      ChannelPojo channelPojo = channelApi.getChannelByName(channelName);
      if (Objects.isNull(client) || client.getType() != UserType.CLIENT) {
        throw new ApiException(Constants.CODE_INVALID_USER, Constants.MSG_INVALID_USER,
            "Client with name: " + clientName + "doesn't exist");
      }
      if (Objects.isNull(channelPojo)) {
        throw new ApiException(Constants.CODE_INAVLID_CHANNEL, Constants.MSG_INVALID_CHANNEL,
            "ChannelPojo with name: " + channelName + "doesn't exist");
      }
      ProductPojo productPojo = productApi.getProductByClientSkuID(channelListingCSV.getClientSkuId());
      if (Objects.isNull(productPojo)) {
        throw new ApiException(Constants.CODE_PRODUCT_NOT_FOUND, Constants.MSG_PRODUCT_NOT_FOUND,
            "ProductPojo with clientSkuId" + channelListingCSV.getClientSkuId() + "not present");
      }
      if (Objects.nonNull(channelApi.findByChannelSkuId(channelListingCSV.getChannelSkuId()))) {
        throw new ApiException(Constants.CODE_DUPLICATE_CHANNEL_ORDER_ID, Constants.MSG_DUPLICATE_CHANNEL_ORDER_ID,
            "ChannelPojo Listing already exists");
      }
      channelApi.addChannelListing(ChannelDtoHelper.convertCSVtoPojo(channelListingCSV, channelPojo, client, productPojo));
    }
  }
}