package com.increff.ta.service;

import com.increff.ta.constants.Constants;
import com.increff.ta.dao.ProductDao;
import com.increff.ta.enums.UserType;
import com.increff.ta.model.UserForm;
import com.increff.ta.pojo.Product;
import com.increff.ta.utils.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class ProductServiceTest extends AbstractUnitTest {

    @Autowired
    ProductService productService;

    @Autowired
    ProductDao productDao;

    @Autowired
    private UserService userService;

    private void init() {
        UserForm userForm = new UserForm();
        userForm.setType(UserType.CUSTOMER);
        userForm.setName("customer");
        this.customerId = userService.createUser(userForm).getId();

        userForm.setType(UserType.CLIENT);
        userForm.setName("client");
        this.clientId = userService.createUser(userForm).getId();

    }

    @Test
    public void addProductsAsCustomer() {
        init();
        byte[] csvBytes = null;
        try {
            csvBytes = Files.readAllBytes(Paths.get(FileUtils.getFileFromResources("addProducts.csv").toURI()));
        } catch (IOException | URISyntaxException e) {
            Assert.fail();
        }
        MultipartFile csvFile = new MockMultipartFile("addProducts.csv", csvBytes);
        try {
            productService.addProductsFromCSV(csvFile, customerId);
        } catch (ApiException e) {
            Assert.assertEquals(e.getCode(), Constants.CODE_INVALID_USER);
            return;
        }
        Assert.fail();
    }

    @Test
    public void addProductsAsClient() {
        init();
        byte[] csvBytes = null;
        try {
            csvBytes = Files.readAllBytes(Paths.get(FileUtils.getFileFromResources("addProducts.csv").toURI()));
        } catch (IOException | URISyntaxException e) {
            Assert.fail();
        }
        MultipartFile csvFile = new MockMultipartFile("addProducts.csv", csvBytes);
        productService.addProductsFromCSV(csvFile, clientId);
    }

    @Test
    public void addProductsWithSameSkuId() {
        init();
        byte[] csvBytes = null;
        try {
            csvBytes = Files.readAllBytes(Paths.get(FileUtils.getFileFromResources("addProducts-sameSkuId.csv").toURI()));
        } catch (IOException | URISyntaxException e) {
            Assert.fail();
        }
        MultipartFile csvFile = new MockMultipartFile("addProducts-sameSkuId.csv", csvBytes);
        try {
            productService.addProductsFromCSV(csvFile, clientId);
        } catch (ApiException e) {
            Assert.assertEquals(e.getCode(), Constants.CODE_DUPLICATE_CLIENT_SKU_ID);
            return;
        }
        Assert.fail();
    }

    @Test(expected = ApiException.class)
    public void addProductWithoutMandatoryFields() {
        init();
        byte[] csvBytes = null;
        try {
            csvBytes = Files.readAllBytes(Paths.get(FileUtils.getFileFromResources("InvalidCSV.html").toURI()));
        } catch (IOException | URISyntaxException e) {
            Assert.fail();
        }
        MultipartFile csvFile = new MockMultipartFile("addProducts-withoutClientSkuID.csv", csvBytes);
        productService.addProductsFromCSV(csvFile, clientId);
    }

    @Test
    public void addProductsAsClientWithUpdate() {
        init();
        String sampleSkuId = "sku-2";
        Product sampleProduct = new Product();
        sampleProduct.setClient(userService.getUser(clientId));
        sampleProduct.setName("Name Before");
        sampleProduct.setClientSkuId(sampleSkuId);
        sampleProduct.setMrp(200D);
        sampleProduct.setBrandId("123");
        sampleProduct.setDescription("Description Before");
        sampleProduct = productService.addProduct(sampleProduct);
        productDao.detach(sampleProduct);

        byte[] csvBytes = null;
        try {
            csvBytes = Files.readAllBytes(Paths.get(FileUtils.getFileFromResources("addProducts.csv").toURI()));
        } catch (IOException | URISyntaxException e) {
            Assert.fail();
        }
        MultipartFile csvFile = new MockMultipartFile("addProducts.csv", csvBytes);
        productService.addProductsFromCSV(csvFile, clientId);
        Product updatedProduct = productService.getProduct(sampleSkuId);
        Assert.assertNotEquals(sampleProduct, updatedProduct);
    }
}
