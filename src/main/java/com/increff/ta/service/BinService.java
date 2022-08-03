package com.increff.ta.service;

import com.increff.ta.dao.BinDao;
import com.increff.ta.dao.BinSkuDao;
import com.increff.ta.dao.InventoryDao;
import com.increff.ta.dao.ProductDao;
import com.increff.ta.model.BinClientSkuCSV;
import com.increff.ta.model.ProductDetailCSV;
import com.increff.ta.pojo.Bin;
import com.increff.ta.pojo.BinSku;
import com.increff.ta.pojo.Inventory;
import com.increff.ta.pojo.Product;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BinService {

    @Autowired
    private BinDao binDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private BinSkuDao binSkuDao;

    @Autowired
    private InventoryService inventoryService;

    public List<Long> createBins(Integer count) throws ApiException {
        if (count == null || count == 0) {
            throw new ApiException("Bin count should be greater than 0");
        }
        List<Long> binIds = new ArrayList<Long>();
        while (count-- != 0) {
            Bin newBin = binDao.create(new Bin());
            binIds.add(newBin.getBinId());
        }
        return binIds;
    }

    public void putProductsToBin(byte[] csvBytes) throws ApiException {
        List<BinClientSkuCSV> binQuantityList = null;
        try {
            binQuantityList = new CsvToBeanBuilder(new InputStreamReader(new ByteArrayInputStream(csvBytes), "UTF8"))
                    .withType(BinClientSkuCSV.class).withSkipLines(1).build().parse();
        } catch (IOException e) {
            throw new ApiException("Error while parsing the CSV File");
        }

        Set<String> binIdClientSkuIdsMapping = binQuantityList.stream().map(binQuantity -> binQuantity.getBinId() + binQuantity.getClientSkuId()).collect(Collectors.toSet());

        if (binIdClientSkuIdsMapping.size() != binQuantityList.size()) {
            throw new ApiException("Every csv row should have a unique binId and clientSkuId");
        }

        for (BinClientSkuCSV binQuantity : binQuantityList) {
            Long deltaQuantity = new Long(0);
            Product product = productDao.findByClientSkuId(binQuantity.getClientSkuId());
            if (product == null) {
                throw new ApiException("Cannot find product with clientSkuId: " + binQuantity.getClientSkuId());
            }
            Bin bin = binDao.findById(binQuantity.getBinId());
            if (bin == null) {
                throw new ApiException("Cannot find Bin with binId: " + binQuantity.getBinId());
            }
            BinSku binSku = binSkuDao.findByBinIdAndGlobalSkuId(bin.getBinId(), product.getGlobalSkuId());

            if (binSku == null) {
                binSku = new BinSku();
                binSku.setProduct(product);
                binSku.setBin(bin);
            }
            binSku.setQuantity(binQuantity.getQuantity());
            binSkuDao.insertOrUpdate(binSku);
            Long totalAvailableQuantity = binSkuDao.findTotalCountByGlobalSkuId(product.getGlobalSkuId());
            inventoryService.updateAvailableQuantity(product, totalAvailableQuantity);
        }
    }
}
