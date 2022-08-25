package com.increff.ta.service;

import com.increff.ta.constants.Constants;
import com.increff.ta.dao.BinDao;
import com.increff.ta.dao.BinSkuDao;
import com.increff.ta.dao.ProductDao;
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

import java.io.IOException;

@RunWith(MockitoJUnitRunner.class)
public class BinServiceTest extends AbstractUnitTest{

    @InjectMocks
    private BinService binService;

    @Mock
    private ProductDao productDao;

    @Mock
    private BinDao binDao;

    @Mock
    private BinSkuDao binSkuDao;

    @Mock
    InventoryService inventoryService;


    @Test
    public void createBins(){
        Mockito.when(binDao.create(Mockito.any())).thenReturn(TestUtils.getBin());
        binService.createBins(5);
    }

    @Test
    public void createBinsInvalidCount(){
        try {
            binService.createBins(0);
        } catch (ApiException e){
            Assert.assertEquals(e.getCode(), Constants.CODE_INVALID_BIN_COUNT);
        }
    }

    @Test
    public void putProductToBin() throws IOException {
        Mockito.when(productDao.findByClientSkuId(Mockito.anyString()))
                .thenReturn(TestUtils.getProduct());
        Mockito.when(binDao.findById(Mockito.anyLong()))
                .thenReturn(TestUtils.getBin());
        Mockito.when(binSkuDao.findByBinIdAndGlobalSkuId(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(null);
        Mockito.doNothing().when(inventoryService).updateAvailableQuantity(Mockito.any(), Mockito.anyLong());
        MockMultipartFile csvfile = FileUtils.getFileFromResources("addProductsToBin.csv");
        binService.putProductsToBin(csvfile.getBytes());
    }

    @Test
    public void putProductToBinAlreadyExisting() throws IOException {
        Mockito.when(productDao.findByClientSkuId(Mockito.anyString()))
                .thenReturn(TestUtils.getProduct());
        Mockito.when(binDao.findById(Mockito.anyLong()))
                .thenReturn(TestUtils.getBin());
        Mockito.when(binSkuDao.findByBinIdAndGlobalSkuId(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(TestUtils.getBinSku());
        Mockito.doNothing().when(inventoryService).updateAvailableQuantity(Mockito.any(), Mockito.anyLong());
        MockMultipartFile csvfile = FileUtils.getFileFromResources("addProductsToBin.csv");
        binService.putProductsToBin(csvfile.getBytes());
    }

    @Test
    public void putProductToBinInvalidCSV() throws IOException {
        Mockito.when(productDao.findByClientSkuId(Mockito.anyString()))
                .thenReturn(TestUtils.getProduct());
        Mockito.when(binDao.findById(Mockito.anyLong()))
                .thenReturn(TestUtils.getBin());
        Mockito.when(binSkuDao.findByBinIdAndGlobalSkuId(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(null);
        Mockito.doNothing().when(inventoryService).updateAvailableQuantity(Mockito.any(), Mockito.anyLong());
        MockMultipartFile csvfile = FileUtils.getFileFromResources("invalidCSV.html");
        try {
            binService.putProductsToBin(csvfile.getBytes());
        } catch(ApiException e){
            Assert.assertEquals(e.getCode(), Constants.CODE_ERROR_PARSING_CSV_FILE);
        }
    }

    @Test
    public void putProductToBinInvalidProduct() throws IOException {
        Mockito.when(productDao.findByClientSkuId(Mockito.anyString()))
                .thenReturn(null);
        Mockito.when(binDao.findById(Mockito.anyLong()))
                .thenReturn(TestUtils.getBin());
        Mockito.when(binSkuDao.findByBinIdAndGlobalSkuId(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(null);
        Mockito.doNothing().when(inventoryService).updateAvailableQuantity(Mockito.any(), Mockito.anyLong());
        MockMultipartFile csvfile = FileUtils.getFileFromResources("addProductsToBin.csv");
        try {
            binService.putProductsToBin(csvfile.getBytes());
        } catch(ApiException e){
            Assert.assertEquals(e.getCode(), Constants.CODE_PRODUCT_NOT_FOUND);
        }
    }

    @Test
    public void putProductToBinInvalidBin() throws IOException {
        Mockito.when(productDao.findByClientSkuId(Mockito.anyString()))
                .thenReturn(TestUtils.getProduct());
        Mockito.when(binDao.findById(Mockito.anyLong()))
                .thenReturn(null);
        Mockito.when(binSkuDao.findByBinIdAndGlobalSkuId(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(null);
        Mockito.doNothing().when(inventoryService).updateAvailableQuantity(Mockito.any(), Mockito.anyLong());
        MockMultipartFile csvfile = FileUtils.getFileFromResources("addProductsToBin.csv");
        try {
            binService.putProductsToBin(csvfile.getBytes());
        } catch(ApiException e){
            Assert.assertEquals(e.getCode(), Constants.CODE_BIN_NOT_FOUND);
        }
    }

    @Test
    public void putProductToBinDuplicateSkuID() throws IOException {
        Mockito.when(productDao.findByClientSkuId(Mockito.anyString()))
                .thenReturn(TestUtils.getProduct());
        Mockito.when(binDao.findById(Mockito.anyLong()))
                .thenReturn(TestUtils.getBin());
        Mockito.when(binSkuDao.findByBinIdAndGlobalSkuId(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(null);
        Mockito.doNothing().when(inventoryService).updateAvailableQuantity(Mockito.any(), Mockito.anyLong());
        MockMultipartFile csvfile = FileUtils.getFileFromResources("addProductsToBin-invalidBinSkuMapping.csv");
        try {
            binService.putProductsToBin(csvfile.getBytes());
        } catch (ApiException e){
            Assert.assertEquals(e.getCode(), Constants.CODE_BINID_CLIENTSKUID_SHOULD_BE_UNIQUE);
        }
    }
}
