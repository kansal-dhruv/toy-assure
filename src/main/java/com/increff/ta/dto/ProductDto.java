package com.increff.ta.dto;

import com.increff.ta.api.ProductApi;
import com.increff.ta.api.UserApi;
import com.increff.ta.commons.exception.ApiException;
import com.increff.ta.commons.model.ProductDetailCSV;
import com.increff.ta.commons.model.enums.UserType;
import com.increff.ta.constants.Constants;
import com.increff.ta.dto.helper.ProductDtoHelper;
import com.increff.ta.pojo.ProductPojo;
import com.increff.ta.pojo.UserPojo;
import com.increff.ta.utils.CSVUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductDto {

  private final ProductApi productApi;

  private final UserApi userApi;

  @Autowired
  public ProductDto(ProductApi productApi, UserApi userApi) {
    this.productApi = productApi;
    this.userApi = userApi;
  }

  public void addProductsFromCSV(MultipartFile csvFile, Long clientId) {
    List<ProductDetailCSV> productDetails = parseAndValidateCSV(csvFile);
    UserPojo client = userApi.getUserById(clientId);
    if (Objects.nonNull(client) && !client.getType().equals(UserType.CLIENT)) {
      throw new ApiException(Constants.CODE_INVALID_USER, Constants.MSG_INVALID_USER,
          "ClientId:" + clientId + " is not a valid client");
    }
    List<ProductPojo> products = ProductDtoHelper.convertCSVtoPojo(productDetails, clientId);
    addProducts(clientId, products);
  }

  private List<ProductDetailCSV> parseAndValidateCSV(MultipartFile csvFile) {
    if (!FilenameUtils.isExtension(csvFile.getOriginalFilename(), "csv")) {
      throw new ApiException(Constants.CODE_ERROR_PARSING_CSV_FILE, Constants.MSG_ERROR_PARSING_CSV_FILE,
          "Input file is not a valid CSV file");
    }
    List<ProductDetailCSV> productDetails = null;
    try {
      productDetails = CSVUtils.parseCSV(csvFile.getBytes(), ProductDetailCSV.class);
    }
    catch (IOException e) {
      throw new ApiException(Constants.CODE_ERROR_PARSING_CSV_FILE, Constants.MSG_ERROR_PARSING_CSV_FILE);
    }
    Set<String> clientToClientSkuId = productDetails.stream().map((ProductDetailCSV::getClientSkuId)).collect(Collectors.toSet());
    if (clientToClientSkuId.size() != productDetails.size()) {
      throw new ApiException(Constants.CODE_DUPLICATE_CLIENT_SKU_ID, Constants.MSG_DUPLICATE_CLIENT_SKU_ID);
    }
    return productDetails;
  }

  private void addProducts(Long clientId, List<ProductPojo> products) {
    for (ProductPojo productToBeAdded : products) {
      ProductPojo productPojo = productApi.getProductByClientSkuIdAndClientId(productToBeAdded.getClientSkuId(), clientId);
      if (Objects.nonNull(productPojo)) {
        productPojo.setName(productToBeAdded.getName());
        productPojo.setMrp(productToBeAdded.getMrp());
        productPojo.setBrandId(productToBeAdded.getBrandId());
        productPojo.setDescription(productToBeAdded.getDescription());
      } else {
        productPojo = productToBeAdded;
      }
      productApi.addProduct(productPojo);
    }
  }
}