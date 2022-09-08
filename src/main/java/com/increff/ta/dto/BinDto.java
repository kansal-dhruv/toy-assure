package com.increff.ta.dto;

import com.increff.ta.api.ApiException;
import com.increff.ta.api.BinApi;
import com.increff.ta.constants.Constants;
import com.increff.ta.model.BinClientSkuCSV;
import com.increff.ta.utils.CSVUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BinDto {

    @Autowired
    private BinApi binApi;

    public List<Long> createBins(Integer count){
        if (count == null || count <= 0) {
            throw new ApiException(Constants.CODE_INVALID_BIN_COUNT, Constants.MSG_INVALID_BIN_COUNT);
        }
        return binApi.createBins(count);
    }

    public void putProductsToBin(byte[] csvBytes) {
        List<BinClientSkuCSV> binQuantityList = CSVUtils.parseCSV(csvBytes, BinClientSkuCSV.class);
        Set<String> binIdClientSkuIdsMapping = binQuantityList.stream().map(binQuantity -> binQuantity.getBinId() + binQuantity.getClientSkuId()).collect(Collectors.toSet());
        if (binIdClientSkuIdsMapping.size() != binQuantityList.size()) {
            throw new ApiException(Constants.CODE_BINID_CLIENTSKUID_SHOULD_BE_UNIQUE, Constants.MSG_BINID_CLIENTSKUID_SHOULD_BE_UNIQUE);
        }
        for (BinClientSkuCSV binClientSkuCSV : binQuantityList) {
            binApi.putProdcutToBin(binClientSkuCSV.getBinId(), binClientSkuCSV.getClientSkuId(), binClientSkuCSV.getQuantity());
        }
    }
}