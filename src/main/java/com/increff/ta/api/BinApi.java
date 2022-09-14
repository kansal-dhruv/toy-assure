package com.increff.ta.api;

import com.increff.ta.dao.BinDao;
import com.increff.ta.dao.BinSkuDao;
import com.increff.ta.pojo.Bin;
import com.increff.ta.pojo.BinSku;
import com.increff.ta.pojo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BinApi {

  @Autowired
  private BinDao binDao;

  @Autowired
  private BinSkuDao binSkuDao;

  public Bin getBinById(Long binId) {
    return binDao.findById(binId);
  }

  public List<Long> createBins(Integer count) throws ApiException {
    List<Long> binIds = new ArrayList<Long>();
    while (count-- != 0) {
      Bin newBin = binDao.create(new Bin());
      binIds.add(newBin.getBinId());
    }
    return binIds;
  }

  public void putProductToBin(BinSku binSku) {
    binSkuDao.insertOrUpdate(binSku);
  }

  public BinSku getBinSkuByClientSkuIdAndBinId(Bin bin, Product product) {
    return binSkuDao.findByBinIdAndGlobalSkuId(bin.getBinId(), product.getGlobalSkuId());
  }

  public Long getTotalQuantityByGlobalSkuID(Product product) {
    return binSkuDao.findTotalCountByGlobalSkuId(product.getGlobalSkuId());
  }

  public List<BinSku> getBinsWithProduct(Long globalSkuId){
    return binSkuDao.findByglobalSkuId(globalSkuId);
  }
}