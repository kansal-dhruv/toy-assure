package com.increff.ta.service;

import com.increff.ta.constants.Constants;
import com.increff.ta.dao.ProductDao;
import com.increff.ta.dao.UserDao;
import com.increff.ta.utils.FileUtils;
import com.increff.ta.utils.TestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;

import static org.mockito.Matchers.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest extends AbstractUnitTest{

    @Mock
    ProductDao productDao;

    @Mock
    UserDao userDao;

    @InjectMocks
    ProductService productService;

    @Test
    public void addProducts(){
        Mockito.when(userDao.selectById(anyLong())).thenReturn(TestUtils.getClientUser());
        Mockito.when(productDao.findByClientSkuId(anyString())).thenReturn(null);
        Mockito.when(productDao.addProduct(any())).thenReturn(TestUtils.getProduct());
        MockMultipartFile csvFile = null;
        csvFile = FileUtils.getFileFromResources("addProducts.csv");
        productService.addProductsFromCSV(csvFile, 1L);
    }

    @Test
    public void addProductsInvalidCSV(){
        Mockito.when(userDao.selectById(anyLong())).thenReturn(TestUtils.getClientUser());
        Mockito.when(productDao.findByClientSkuId(anyString())).thenReturn(null);
        Mockito.when(productDao.addProduct(any())).thenReturn(TestUtils.getProduct());
        MockMultipartFile csvFile = null;
        csvFile = FileUtils.getFileFromResources("invalidCSV.html");
        try {
            productService.addProductsFromCSV(csvFile, 1L);
        } catch (ApiException e){
            Assert.assertEquals(e.getCode(), Constants.CODE_ERROR_PARSING_CSV_FILE);
        }
    }

    @Test
    public void addProductsInvalidUser(){
        Mockito.when(userDao.selectById(anyLong())).thenReturn(TestUtils.getCustomerUser());
        Mockito.when(productDao.findByClientSkuId(anyString())).thenReturn(null);
        Mockito.when(productDao.addProduct(any())).thenReturn(TestUtils.getProduct());
        MockMultipartFile csvFile = null;
        csvFile = FileUtils.getFileFromResources("addProducts.csv");
        try {
            productService.addProductsFromCSV(csvFile, 1L);
        } catch(ApiException e){
            Assert.assertEquals(e.getCode(), Constants.CODE_INVALID_USER);
            return;
        }
        Assert.fail();
    }

    @Test
    public void addProductsDuplicateProduct(){
        Mockito.when(userDao.selectById(anyLong())).thenReturn(TestUtils.getClientUser());
        Mockito.when(productDao.findByClientSkuId(anyString())).thenReturn(TestUtils.getProduct());
        Mockito.when(productDao.addProduct(any())).thenReturn(TestUtils.getProduct());
        MockMultipartFile csvFile = null;
        csvFile = FileUtils.getFileFromResources("addProducts-sameSkuId.csv");
        try {
            productService.addProductsFromCSV(csvFile, 1L);
        } catch(ApiException e){
            Assert.assertEquals(e.getCode(), Constants.CODE_DUPLICATE_CLIENT_SKU_ID);
            return;
        }
        Assert.fail();
    }
}
