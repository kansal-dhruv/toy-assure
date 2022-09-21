package com.increff.ta.dto.helper;

import com.increff.ta.commons.exception.ApiException;
import com.increff.ta.commons.model.OrderUploadCSV;
import com.increff.ta.commons.model.enums.OrderStatus;
import com.increff.ta.constants.Constants;
import com.increff.ta.pojo.ChannelPojo;
import com.increff.ta.pojo.OrderPojo;
import com.increff.ta.utils.CSVUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class OrderDtoHelper {

  public static OrderPojo createOrderPojo(Long clientId, String channelOrderId, ChannelPojo channelPojo, Long customerId) {
    OrderPojo orderPojo = new OrderPojo();
    orderPojo.setClientId(clientId);
    orderPojo.setChannelOrderId(channelOrderId);
    orderPojo.setChannelId(channelPojo.getId());
    orderPojo.setCustomerId(customerId);
    orderPojo.setStatus(OrderStatus.CREATED);
    return orderPojo;
  }

  public static List<OrderUploadCSV> validateAndParseCSV(MultipartFile csvFile) {
    if (!FilenameUtils.isExtension(csvFile.getOriginalFilename(), "csv")) {
      throw new ApiException(Constants.CODE_ERROR_PARSING_CSV_FILE, Constants.MSG_ERROR_PARSING_CSV_FILE,
          "Input file is not a valid CSV file");
    }
    List<OrderUploadCSV> orderUploadDetails = null;
    try {
      orderUploadDetails = CSVUtils.parseCSV(csvFile.getBytes(), OrderUploadCSV.class);
    }
    catch (IOException e) {
      throw new ApiException(Constants.CODE_ERROR_PARSING_CSV_FILE, Constants.MSG_ERROR_PARSING_CSV_FILE);
    }
    // .distinct()
    Set<String> clientSkuIds = orderUploadDetails.stream().map((OrderUploadCSV orderUploadDetail) -> orderUploadDetail.getClientSkuId()).collect(Collectors.toSet());
    if (clientSkuIds.size() != orderUploadDetails.size()) {
      throw new ApiException(Constants.CODE_DUPLICATE_CLIENT_SKU_ID, Constants.MSG_DUPLICATE_CLIENT_SKU_ID);
    }
    return orderUploadDetails;
  }

}