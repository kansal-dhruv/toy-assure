package com.increff.ta.dto.helper;

import com.increff.ta.api.ApiException;
import com.increff.ta.constants.Constants;
import com.increff.ta.enums.OrderStatus;
import com.increff.ta.model.OrderUploadCSV;
import com.increff.ta.pojo.Channel;
import com.increff.ta.pojo.Orders;
import com.increff.ta.utils.CSVUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class OrderDtoHelper {

  public static Orders createOrderPojo(Long clientId, String channelOrderId, Channel channel, Long customerId) {
    Orders orders = new Orders();
    orders.setClientId(clientId);
    orders.setChannelOrderId(channelOrderId);
    orders.setChannelId(channel.getId());
    orders.setCustomerId(customerId);
    orders.setStatus(OrderStatus.CREATED);
    return orders;
  }

  public static List<OrderUploadCSV> validateAndParseCSV(MultipartFile csvFile) {
    if(!FilenameUtils.isExtension(csvFile.getOriginalFilename(), "csv")){
      throw new ApiException(Constants.CODE_ERROR_PARSING_CSV_FILE, Constants.MSG_ERROR_PARSING_CSV_FILE,
          "Input file is not a valid CSV file");
    }
    List<OrderUploadCSV> orderUploadDetails = null;
    try {
      orderUploadDetails = CSVUtils.parseCSV(csvFile.getBytes(), OrderUploadCSV.class);
    } catch (IOException e) {
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