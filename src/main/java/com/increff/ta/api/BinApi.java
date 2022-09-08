package com.increff.ta.api;

import com.increff.ta.constants.Constants;
import com.increff.ta.dao.BinDao;
import com.increff.ta.dao.BinSkuDao;
import com.increff.ta.dao.ProductDao;
import com.increff.ta.pojo.Bin;
import com.increff.ta.pojo.BinSku;
import com.increff.ta.pojo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class BinApi {

    @Autowired
    private BinDao binDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private BinSkuDao binSkuDao;

    @Autowired
    private InventoryService inventoryService;

    public List<Long> createBins(Integer count) throws ApiException {
        List<Long> binIds = new ArrayList<Long>();
        while (count-- != 0) {
            Bin newBin = binDao.create(new Bin());
            binIds.add(newBin.getBinId());
        }
        return binIds;
    }

    @Transactional
    public void putProdcutToBin(Long binId, String clientSkuId, Long quantity) {
        Product product = productDao.findByClientSkuId(clientSkuId);
        if (product == null) {
            throw new ApiException(Constants.CODE_PRODUCT_NOT_FOUND, Constants.MSG_PRODUCT_NOT_FOUND,
                    "ClientSkuId: " + clientSkuId + " not found");
        }
        Bin bin = binDao.findById(binId);
        if (bin == null) {
            throw new ApiException(Constants.CODE_BIN_NOT_FOUND, Constants.MSG_BIN_NOT_FOUND,
                    "BinId: " + binId + " not found");
        }
        BinSku binSku = binSkuDao.findByBinIdAndGlobalSkuId(binId, product.getGlobalSkuId());
        if (binSku == null) {
            binSku = new BinSku();
            binSku.setGlobalSkuId(product.getGlobalSkuId());
            binSku.setBinId(binId);
            binSku.setQuantity(quantity);
            binSkuDao.insertOrUpdate(binSku);
        } else {
            binSku.setQuantity(quantity);
        }
        Long totalAvailableQuantity = binSkuDao.findTotalCountByGlobalSkuId(product.getGlobalSkuId());
        inventoryService.updateAvailableQuantity(product, totalAvailableQuantity);
    }
}
