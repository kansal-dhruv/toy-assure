package com.increff.ta.service;

import com.increff.ta.dao.ProductDao;
import com.increff.ta.dao.UserDao;
import com.increff.ta.enums.UserType;
import com.increff.ta.model.ProductDetailCSV;
import com.increff.ta.pojo.Product;
import com.increff.ta.pojo.User;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserDao userDao;

    public String addProductsFromCSV(MultipartFile csvFile, Long clientId) throws ApiException {
        User user = userDao.selectById(clientId);
        if (user !=null && user.getType().equals(UserType.CLIENT)) {
            List<ProductDetailCSV> productdetails = null;
            try {
                productdetails = new CsvToBeanBuilder(new InputStreamReader(new ByteArrayInputStream(csvFile.getBytes()), "UTF8"))
                        .withType(ProductDetailCSV.class).withSkipLines(1).build().parse();
            } catch (IOException e) {
                throw new ApiException("Issue while processing CSV File");
            }
            Set<String> clientToClientSkuId = productdetails.stream().map((productPojo -> productPojo.getClientSkuId())).collect(Collectors.toSet());
            if (clientToClientSkuId.size() == productdetails.size()) {
                for (ProductDetailCSV productdetail : productdetails) {
                    Product product = productDao.findByClientSkuId(productdetail.getClientSkuId());
                    if(product!=null){
                        product = convertCSVtoPojo(product, user, product.getGlobalSkuId());
                    } else {
                        product = convertCSVtoPojo(product, user, null);
                    }
                    productDao.addProduct(product);
                }
                return "success";
            } else throw new ApiException("There Cannot be same clientSkuId for a given clientId");
        } else throw new ApiException("Given clientId is not a client");
    }

    private Product convertCSVtoPojo(ProductDetailCSV productDetail, User user, Long globalSkuId) {
        Product product = new Product();
        if(globalSkuId!=null){
            product.setGlobalSkuId(globalSkuId);
        }
        product.setClient(user);
        product.setName(productDetail.getName());
        product.setBrandId(productDetail.getBrandId());
        product.setDescription(productDetail.getDescription());
        product.setClientSkuId(productDetail.getClientSkuId());
        product.setMrp(productDetail.getMrp());
        return product;
    }

}
