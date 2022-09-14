package com.increff.ta.dto.helper;

import com.increff.ta.api.ApiException;
import com.increff.ta.constants.Constants;
import com.increff.ta.model.ChannelForm;
import com.increff.ta.model.ChannelListingCSV;
import com.increff.ta.pojo.Channel;
import com.increff.ta.pojo.ChannelListing;
import com.increff.ta.pojo.Product;
import com.increff.ta.pojo.User;
import com.increff.ta.utils.CSVUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public class ChannelDtoHelper {
  public static List<ChannelListingCSV> validateAndParseCSV(MultipartFile csvFile) {
    if (!FilenameUtils.isExtension(csvFile.getOriginalFilename(), "csv")) {
      throw new ApiException(Constants.CODE_ERROR_PARSING_CSV_FILE, Constants.MSG_ERROR_PARSING_CSV_FILE,
          "Input file is not a valid CSV file");
    }
    List<ChannelListingCSV> channelListingDetails = null;
    try {
      channelListingDetails = CSVUtils.parseCSV(csvFile.getBytes(), ChannelListingCSV.class);
    }
    catch (IOException e) {
      throw new ApiException(Constants.CODE_ERROR_PARSING_CSV_FILE, Constants.MSG_ERROR_PARSING_CSV_FILE);
    }
    return channelListingDetails;
  }

  public static Channel convertChannelFormToPojo(ChannelForm channelForm) {
    Channel channel = new Channel();
    channel.setName(channelForm.getName());
    channel.setInvoiceType(channelForm.getInvoiceType());
    return channel;
  }

  public static ChannelListing convertCSVtoPojo(ChannelListingCSV channelListingCSV, Channel channel, User client,
                                          Product product) {
    ChannelListing channelListing = new ChannelListing();
    channelListing.setChannelId(channel.getId());
    channelListing.setClientId(client.getId());
    channelListing.setChannelSkuId(channelListingCSV.getChannelSkuId());
    channelListing.setGlobalSkuId(product.getGlobalSkuId());
    return channelListing;
  }
}