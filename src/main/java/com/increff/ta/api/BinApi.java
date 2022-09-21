package com.increff.ta.api;

import com.increff.ta.commons.exception.ApiException;
import com.increff.ta.dao.BinDao;
import com.increff.ta.dao.BinSkuDao;
import com.increff.ta.pojo.BinPojo;
import com.increff.ta.pojo.BinSkuPojo;
import com.increff.ta.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class BinApi {

  private final BinDao binDao;

  private final BinSkuDao binSkuDao;

  @Autowired
  public BinApi(BinDao binDao, BinSkuDao binSkuDao) {
    this.binDao = binDao;
    this.binSkuDao = binSkuDao;
  }

  public BinPojo getBinById(Long binId) {
    return binDao.findById(binId);
  }

  public List<Long> createBins(Integer count) throws ApiException {
    List<Long> binIds = new ArrayList<>();
    while (count-- != 0) {
      BinPojo newBin = binDao.create(new BinPojo());
      binIds.add(newBin.getBinId());
    }
    return binIds;
  }

  public void putProductToBin(BinSkuPojo binSkuPojo) {
    binSkuDao.insertOrUpdate(binSkuPojo);
  }

  public BinSkuPojo getBinSkuByClientSkuIdAndBinId(BinPojo binPojo, ProductPojo productPojo) {
    return binSkuDao.findByBinIdAndGlobalSkuId(binPojo.getBinId(), productPojo.getGlobalSkuId());
  }

  public Long getTotalQuantityByGlobalSkuID(ProductPojo productPojo) {
    return binSkuDao.findTotalCountByGlobalSkuId(productPojo.getGlobalSkuId());
  }

  public List<BinSkuPojo> getBinsWithProduct(Long globalSkuId) {
    return binSkuDao.findByglobalSkuId(globalSkuId);
  }
}