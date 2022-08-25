package com.increff.ta.service;

import com.increff.ta.constants.Constants;
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

    public void addProductsFromCSV(MultipartFile csvFile, Long clientId) throws ApiException {
        User user = userDao.selectById(clientId);
        if (user != null && user.getType().equals(UserType.CLIENT)) {
            List<ProductDetailCSV> productdetails = null;
            try {
                productdetails = new CsvToBeanBuilder(new InputStreamReader(new ByteArrayInputStream(csvFile.getBytes()), "UTF8"))
                        .withType(ProductDetailCSV.class).withSkipLines(1).build().parse();
            } catch (Exception e) {
                throw new ApiException(Constants.CODE_ERROR_PARSING_CSV_FILE, Constants.MSG_ERROR_PARSING_CSV_FILE);
            }
            Set<String> clientToClientSkuId = productdetails.stream().map((productPojo -> productPojo.getClientSkuId())).collect(Collectors.toSet());
            if (clientToClientSkuId.size() == productdetails.size()) {
                for (ProductDetailCSV productdetail : productdetails) {
                    Product product = productDao.findByClientSkuId(productdetail.getClientSkuId());
                    if (product != null) {
                        product = convertCSVtoPojo(product, productdetail, user, product.getGlobalSkuId());
                    } else {
                        product = convertCSVtoPojo(product, productdetail, user, null);
                        productDao.addProduct(product);
                    }
                }
            } else
                throw new ApiException(Constants.CODE_DUPLICATE_CLIENT_SKU_ID, Constants.MSG_DUPLICATE_CLIENT_SKU_ID);
        } else throw new ApiException(Constants.CODE_INVALID_USER, Constants.MSG_INVALID_USER);
    }

//    public Product addProduct(Product product) {
//        return productDao.addProduct(product);
//    }
//
//    public Product getProduct(String clientSkuId) {
//        return productDao.findByClientSkuId(clientSkuId);
//    }

    private Product convertCSVtoPojo(Product product, ProductDetailCSV productDetail, User user, Long globalSkuId) {
        if (product == null) {
            product = new Product();
        }
        if (globalSkuId != null) {
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
