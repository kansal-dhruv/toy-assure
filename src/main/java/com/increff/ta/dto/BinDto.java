package com.increff.ta.dto;

import com.increff.ta.api.BinApi;
import com.increff.ta.api.InventoryApi;
import com.increff.ta.api.ProductApi;
import com.increff.ta.commons.exception.ApiException;
import com.increff.ta.commons.model.BinClientSkuCSV;
import com.increff.ta.constants.Constants;
import com.increff.ta.dto.helper.BinDtoHelper;
import com.increff.ta.pojo.BinPojo;
import com.increff.ta.pojo.BinSkuPojo;
import com.increff.ta.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
public class BinDto {

  private final BinApi binApi;

  private final ProductApi productApi;

  private final InventoryApi inventoryApi;

  @Autowired
  public BinDto(BinApi binApi, ProductApi productApi, InventoryApi inventoryApi) {
    this.binApi = binApi;
    this.productApi = productApi;
    this.inventoryApi = inventoryApi;
  }


  public List<Long> createBins(Integer count) {
    if (Objects.isNull(count) || count <= 0) {
      throw new ApiException(Constants.CODE_INVALID_BIN_COUNT, Constants.MSG_INVALID_BIN_COUNT);
    }
    return binApi.createBins(count);
  }

  @Transactional
  public void putProductsToBin(MultipartFile csvFile) {
    List<BinClientSkuCSV> binQuantityList = BinDtoHelper.parseAndValidateCSVFile(csvFile);
    for (BinClientSkuCSV binClientSkuCSV : binQuantityList) {
      ProductPojo productPojo = productApi.getProductByClientSkuID(binClientSkuCSV.getClientSkuId());
      if (Objects.isNull(productPojo)) {
        throw new ApiException(Constants.CODE_PRODUCT_NOT_FOUND, Constants.MSG_PRODUCT_NOT_FOUND,
            "ClientSkuId: " + binClientSkuCSV.getClientSkuId() + " not found");
      }
      BinPojo binPojo = binApi.getBinById(binClientSkuCSV.getBinId());
      if (Objects.isNull(binPojo)) {
        throw new ApiException(Constants.CODE_BIN_NOT_FOUND, Constants.MSG_BIN_NOT_FOUND,
            "BinPojo with ID: " + binClientSkuCSV.getBinId() + " not found");
      }
      BinSkuPojo binSkuPojo = createBinSku(binPojo, productPojo, binClientSkuCSV.getQuantity());
      binApi.putProductToBin(binSkuPojo);

      Long totalQuantity = binApi.getTotalQuantityByGlobalSkuID(productPojo);
      inventoryApi.updateAvailableQuantity(productPojo, totalQuantity);

    }
  }

  private BinSkuPojo createBinSku(BinPojo binPojo, ProductPojo productPojo, Long quantity) {
    BinSkuPojo binSkuPojo = binApi.getBinSkuByClientSkuIdAndBinId(binPojo, productPojo);
    if (Objects.isNull(binSkuPojo)) {
      binSkuPojo = new BinSkuPojo();
      binSkuPojo.setGlobalSkuId(productPojo.getGlobalSkuId());
      binSkuPojo.setBinId(binPojo.getBinId());
      binSkuPojo.setQuantity(quantity);
    } else {
      binSkuPojo.setQuantity(quantity);
    }
    return binSkuPojo;
  }
}