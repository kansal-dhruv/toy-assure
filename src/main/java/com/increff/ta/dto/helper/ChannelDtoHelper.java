package com.increff.ta.dto.helper;

import com.increff.ta.commons.exception.ApiException;
import com.increff.ta.commons.model.ChannelForm;
import com.increff.ta.commons.model.ChannelListingCSV;
import com.increff.ta.constants.Constants;
import com.increff.ta.pojo.ChannelListingPojo;
import com.increff.ta.pojo.ChannelPojo;
import com.increff.ta.pojo.ProductPojo;
import com.increff.ta.pojo.UserPojo;
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

  public static ChannelPojo convertChannelFormToPojo(ChannelForm channelForm) {
    ChannelPojo channelPojo = new ChannelPojo();
    channelPojo.setName(channelForm.getName());
    channelPojo.setInvoiceType(channelForm.getInvoiceType());
    return channelPojo;
  }

  public static ChannelListingPojo convertCSVtoPojo(ChannelListingCSV channelListingCSV, ChannelPojo channelPojo, UserPojo client,
                                                    ProductPojo productPojo) {
    ChannelListingPojo channelListingPojo = new ChannelListingPojo();
    channelListingPojo.setChannelId(channelPojo.getId());
    channelListingPojo.setClientId(client.getId());
    channelListingPojo.setChannelSkuId(channelListingCSV.getChannelSkuId());
    channelListingPojo.setGlobalSkuId(productPojo.getGlobalSkuId());
    return channelListingPojo;
  }
}