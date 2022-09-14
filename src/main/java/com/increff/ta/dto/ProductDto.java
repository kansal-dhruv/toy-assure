package com.increff.ta.dto;

import com.increff.ta.api.ApiException;
import com.increff.ta.api.ProductApi;
import com.increff.ta.api.UserApi;
import com.increff.ta.constants.Constants;
import com.increff.ta.enums.UserType;
import com.increff.ta.dto.helper.ProductDtoHelper;
import com.increff.ta.model.ProductDetailCSV;
import com.increff.ta.pojo.Product;
import com.increff.ta.pojo.User;
import com.increff.ta.utils.CSVUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductDto {

    @Autowired
    private ProductApi productApi;

    @Autowired
    private UserApi userApi;

    public void addProductsFromCSV(MultipartFile csvFile, Long clientId){
        List<ProductDetailCSV> productDetails = parseAndValidateCSV(csvFile);
        User client = userApi.getUserById(clientId);
        if(client != null && !client.getType().equals(UserType.CLIENT)){
            throw new ApiException(Constants.CODE_INVALID_USER, Constants.MSG_INVALID_USER,
                "ClientId:" + clientId + " is not a valid client");
        }
        List<Product> products = ProductDtoHelper.convertCSVtoPojo(productDetails, clientId);
        addProducts(clientId, products);
    }

    private List<ProductDetailCSV> parseAndValidateCSV(MultipartFile csvFile) {
        if(!FilenameUtils.isExtension(csvFile.getOriginalFilename(), "csv")){
            throw new ApiException(Constants.CODE_ERROR_PARSING_CSV_FILE, Constants.MSG_ERROR_PARSING_CSV_FILE,
                "Input file is not a valid CSV file");
        }
        List<ProductDetailCSV> productDetails = null;
        try {
            productDetails = CSVUtils.parseCSV(csvFile.getBytes(), ProductDetailCSV.class);
        } catch (IOException e) {
            throw new ApiException(Constants.CODE_ERROR_PARSING_CSV_FILE, Constants.MSG_ERROR_PARSING_CSV_FILE);
        }
        Set<String> clientToClientSkuId = productDetails.stream().map((ProductDetailCSV::getClientSkuId)).collect(Collectors.toSet());
        if (clientToClientSkuId.size() != productDetails.size()) {
            throw new ApiException(Constants.CODE_DUPLICATE_CLIENT_SKU_ID, Constants.MSG_DUPLICATE_CLIENT_SKU_ID);
        }
        return productDetails;
    }

    private void addProducts(Long clientId, List<Product> products) {
        for(Product productToBeAdded : products) {
            Product product = productApi.getProductByClientSkuIdAndClientId(productToBeAdded.getClientSkuId(), clientId);
            if (product != null) {
                product.setName(productToBeAdded.getName());
                product.setMrp(productToBeAdded.getMrp());
                product.setBrandId(productToBeAdded.getBrandId());
                product.setDescription(productToBeAdded.getDescription());
            } else {
                product = productToBeAdded;
            }
            productApi.addProduct(product);
        }
    }
}