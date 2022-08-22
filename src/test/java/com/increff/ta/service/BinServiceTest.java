package com.increff.ta.service;

import com.increff.ta.constants.Constants;
import com.increff.ta.dao.BinDao;
import com.increff.ta.dao.BinSkuDao;
import com.increff.ta.dao.ProductDao;
import com.increff.ta.enums.UserType;
import com.increff.ta.model.UserForm;
import com.increff.ta.utils.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class BinServiceTest extends AbstractUnitTest{

    @Autowired
    BinService binService;

    @Autowired
    ProductServiceTest productServiceTest;

    @Autowired
    InventoryService inventoryService;

    @Autowired
    BinSkuDao binSkuDao;

    @Autowired
    BinDao binDao;

    @Autowired
    ProductDao productDao;

    @Autowired
    private UserService userService;

    private void init(){
        UserForm userForm = new UserForm();
        userForm.setType(UserType.CUSTOMER);
        userForm.setName("customer");
        this.customerId = userService.createUser(userForm).getId();
        userForm.setType(UserType.CLIENT);
        userForm.setName("client");
        this.clientId = userService.createUser(userForm).getId();
    }

    @Test
    public void createBinsWithInvalidBinCount(){
        try {
            binService.createBins(0);
        } catch (ApiException e){
            Assert.assertEquals(e.getCode(), Constants.CODE_INVALID_BIN_COUNT);
        }
    }

    @Test
    public void createBinsWithValidBinCount(){
        List<Long> binIds = binService.createBins(5);
        if(binIds.size() != 5){
            Assert.fail();
        }
    }

    @Test
    public void putProductsToBin(){
        createBinsWithValidBinCount();
        productServiceTest.addProductsAsClient();
        try {
            putProductsToBinWithFileName("addProductsToBin.csv");
        } catch (ApiException e){
            e.printStackTrace();
        }
    }

    @Test
    public void putProductsToBinWithInvalidCSV(){
        createBinsWithValidBinCount();
        productServiceTest.addProductsAsClient();
        try {
            putProductsToBinWithFileName("InvalidCSV.html");
        } catch (ApiException e){
            Assert.assertEquals(e.getCode(), Constants.CODE_ERROR_PARSING_CSV_FILE);
            return;
        } catch(Exception e){
            Assert.fail();
        }
        Assert.fail();
    }

    @Test
    public void putProductsToBinWithInvalidBinId(){
        createBinsWithValidBinCount();
        productServiceTest.addProductsAsClient();
        try {
            putProductsToBinWithFileName("addProductsToBin-invalidBinID.csv");
        } catch (ApiException e){
            Assert.assertEquals(e.getCode(), Constants.CODE_BIN_NOT_FOUND);
            return;
        } catch(Exception e){
            Assert.fail();
        }
        Assert.fail();
    }

    @Test
    public void putProductsToBinWithInvalidClientSkuId(){
        createBinsWithValidBinCount();
        productServiceTest.addProductsAsClient();
        try {
            putProductsToBinWithFileName("addProductsToBin-invalidProductsID.csv");
        } catch (ApiException e){
            Assert.assertEquals(e.getCode(), Constants.CODE_PRODUCT_NOT_FOUND);
            return;
        } catch(Exception e){
            Assert.fail();
        }
        Assert.fail();
    }

    @Test
    public void putProductsToBinWithInvalidBinSkuMapping(){
        createBinsWithValidBinCount();
        productServiceTest.addProductsAsClient();
        try {
            putProductsToBinWithFileName("addProductsToBin-invalidBinSkuMapping.csv");
        } catch (ApiException e){
            Assert.assertEquals(e.getCode(), Constants.CODE_BINID_CLIENTSKUID_SHOULD_BE_UNIQUE);
            return;
        } catch(Exception e){
            Assert.fail();
        }
        Assert.fail();
    }

//    @Test
//    public void putProductsToBinWithUpdateAlreadyExisting(){
//        createBinsWithValidBinCount();
//        productServiceTest.init();
//        productServiceTest.addProductsAsClient();
//        BinSku binSku = new BinSku();
//        binSku.setQuantity(10L);
//        binSku.setBin(binDao.findById(1L));
//        binSku.setProduct(productDao.findByClientSkuId("sku-1"));
//        binSkuDao.insertOrUpdate(binSku);
//        putProductsToBinWithFileName("addProductsToBin.csv");
//    }

    private void putProductsToBinWithFileName(String fileName){
        byte[] csvBytes = null;
        try {
            csvBytes = Files.readAllBytes(Paths.get(FileUtils.getFileFromResources(fileName).toURI()));
        } catch (IOException | URISyntaxException e) {
            Assert.fail();
        }
        try {
            binService.putProductsToBin(csvBytes);
        } catch(Exception e){
            throw e;
        }
    }
}
