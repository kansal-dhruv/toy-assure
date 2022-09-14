package com.increff.ta.dto;

import com.increff.ta.api.ApiException;
import com.increff.ta.api.BinApi;
import com.increff.ta.api.InventoryService;
import com.increff.ta.api.ProductApi;
import com.increff.ta.constants.Constants;
import com.increff.ta.dto.helper.BinDtoHelper;
import com.increff.ta.model.BinClientSkuCSV;
import com.increff.ta.pojo.Bin;
import com.increff.ta.pojo.BinSku;
import com.increff.ta.pojo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class BinDto {

  @Autowired
  private BinApi binApi;

  @Autowired
  private ProductApi productApi;

  @Autowired
  private InventoryService inventoryService;


  public List<Long> createBins(Integer count) {
    if (count == null || count <= 0) {
      throw new ApiException(Constants.CODE_INVALID_BIN_COUNT, Constants.MSG_INVALID_BIN_COUNT);
    }
    return binApi.createBins(count);
  }

  @Transactional
  public void putProductsToBin(MultipartFile csvFile) {
    List<BinClientSkuCSV> binQuantityList = BinDtoHelper.parseAndValidateCSVFile(csvFile);
    for (BinClientSkuCSV binClientSkuCSV : binQuantityList) {
      Product product = productApi.getProductByClientSkuID(binClientSkuCSV.getClientSkuId());
      if(product == null){
        throw new ApiException(Constants.CODE_PRODUCT_NOT_FOUND, Constants.MSG_PRODUCT_NOT_FOUND,
            "ClientSkuId: " + binClientSkuCSV.getClientSkuId() + " not found");
      }
      Bin bin = binApi.getBinById(binClientSkuCSV.getBinId());
      if (bin == null) {
        throw new ApiException(Constants.CODE_BIN_NOT_FOUND, Constants.MSG_BIN_NOT_FOUND,
            "Bin with ID: " + binClientSkuCSV.getBinId() + " not found");
      }
      BinSku binSku = createBinSku(bin, product, binClientSkuCSV.getQuantity());
      binApi.putProductToBin(binSku);

      Long totalQuantity = binApi.getTotalQuantityByGlobalSkuID(product);
      inventoryService.updateAvailableQuantity(product, totalQuantity);

    }
  }
  private BinSku createBinSku(Bin bin, Product product, Long quantity){
    BinSku binSku = binApi.getBinSkuByClientSkuIdAndBinId(bin, product);
    if (binSku == null) {
      binSku = new BinSku();
      binSku.setGlobalSkuId(product.getGlobalSkuId());
      binSku.setBinId(bin.getBinId());
      binSku.setQuantity(quantity);
    } else {
      binSku.setQuantity(quantity);
    }
    return binSku;
  }
}