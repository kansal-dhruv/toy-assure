package com.increff.ta.dto.helper;

import com.increff.ta.commons.exception.ApiException;
import com.increff.ta.commons.model.BinClientSkuCSV;
import com.increff.ta.constants.Constants;
import com.increff.ta.utils.CSVUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BinDtoHelper {
  public static List<BinClientSkuCSV> parseAndValidateCSVFile(MultipartFile csvFile) {
    if (!FilenameUtils.isExtension(csvFile.getOriginalFilename(), "csv")) {
      throw new ApiException(Constants.CODE_ERROR_PARSING_CSV_FILE, Constants.MSG_ERROR_PARSING_CSV_FILE,
          "Input file is not a valid CSV file");
    }
    List<BinClientSkuCSV> binQuantityList;
    try {
      binQuantityList = CSVUtils.parseCSV(csvFile.getBytes(), BinClientSkuCSV.class);
    }
    catch (IOException e) {
      throw new ApiException(Constants.CODE_ERROR_PARSING_CSV_FILE, Constants.MSG_ERROR_PARSING_CSV_FILE);
    }
    Set<String> binIdClientSkuIdsMapping = binQuantityList.stream().map(binQuantity -> binQuantity.getBinId() + binQuantity.getClientSkuId()).collect(Collectors.toSet());
    if (binIdClientSkuIdsMapping.size() != binQuantityList.size()) {
      throw new ApiException(Constants.CODE_BINID_CLIENTSKUID_SHOULD_BE_UNIQUE, Constants.MSG_BINID_CLIENTSKUID_SHOULD_BE_UNIQUE);
    }
    return binQuantityList;
  }
}