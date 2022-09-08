package com.increff.ta.dto;

import com.increff.ta.api.ApiException;
import com.increff.ta.api.ProductApi;
import com.increff.ta.constants.Constants;
import com.increff.ta.model.ProductDetailCSV;
import com.increff.ta.pojo.Product;
import com.increff.ta.utils.CSVUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductDto {

    @Autowired
    private ProductApi productApi;

    public void addProdcutsFromCSV(MultipartFile csvFile, Long clientId){
        List<ProductDetailCSV> productdetails = null;
        try {
            productdetails = CSVUtils.parseCSV(csvFile.getBytes(), ProductDetailCSV.class);
        } catch (IOException e) {
            throw new ApiException(Constants.CODE_ERROR_PARSING_CSV_FILE, Constants.MSG_ERROR_PARSING_CSV_FILE);
        }
        Set<String> clientToClientSkuId = productdetails.stream().map((productPojo -> productPojo.getClientSkuId())).collect(Collectors.toSet());
        if (clientToClientSkuId.size() != productdetails.size()) {
            throw new ApiException(Constants.CODE_DUPLICATE_CLIENT_SKU_ID, Constants.MSG_DUPLICATE_CLIENT_SKU_ID);
        }
        List<Product> products = convertCSVtoPojo(productdetails, clientId);
        productApi.addProducts(products);
    }

    private List<Product> convertCSVtoPojo(List<ProductDetailCSV> productDetailCSVList, Long clientId) {
        List<Product> productList = new ArrayList<>();
        for(ProductDetailCSV productDetailCSV : productDetailCSVList){
            Product product = new Product();
            product.setClientSkuId(productDetailCSV.getClientSkuId());
            product.setClient(clientId);
            product.setMrp(productDetailCSV.getMrp());
            product.setDescription(productDetailCSV.getDescription());
            product.setName(productDetailCSV.getName());
            product.setBrandId(productDetailCSV.getBrandId());
            productList.add(product);
        }
        return productList;
    }

}