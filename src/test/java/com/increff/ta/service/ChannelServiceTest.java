package com.increff.ta.service;

import com.increff.ta.constants.Constants;
import com.increff.ta.dao.ChannelDao;
import com.increff.ta.dao.ChannelListingDao;
import com.increff.ta.dao.ProductDao;
import com.increff.ta.dao.UserDao;
import com.increff.ta.pojo.ChannelListing;
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

@RunWith(MockitoJUnitRunner.class)
public class ChannelServiceTest extends AbstractUnitTest {

    @InjectMocks
    private ChannelService channelService;

    @Mock
    private ChannelDao channelDao;

    @Mock
    private UserDao userDao;

    @Mock
    private ProductDao productDao;

    @Mock
    private ChannelListingDao channelListingDao;

    @Test
    public void createChannel(){
        Mockito.when(channelDao.findByChannelName(Mockito.anyString())).thenReturn(null);
        Mockito.when(channelDao.insertOrUpdate(Mockito.any())).thenReturn(TestUtils.getChannel());
        channelService.addChannel(TestUtils.getChannel());
    }

    @Test
    public void createChannelSameName(){
        Mockito.when(channelDao.findByChannelName(Mockito.anyString())).thenReturn(TestUtils.getChannel());
        try {
            channelService.addChannel(TestUtils.getChannel());
        } catch (ApiException e){
            Assert.assertEquals(e.getCode(), Constants.CODE_INAVLID_CHANNEL);
        }
    }

    @Test
    public void addChannelListing(){
        Mockito.when(userDao.selectByName(Mockito.anyString())).thenReturn(TestUtils.getClientUser());
        Mockito.when(channelDao.findByChannelName(Mockito.anyString())).thenReturn(TestUtils.getChannel());
        Mockito.when(productDao.findByClientSkuId(Mockito.anyString())).thenReturn(TestUtils.getProduct());
        Mockito.when(channelListingDao.findByChannelSkuIdAndChannelIdAndGlobalSkuId(Mockito.anyLong(), Mockito.anyString(), Mockito.anyLong())).thenReturn(null);
        Mockito.when(channelListingDao.saveOrUpdate(Mockito.any())).thenReturn(new ChannelListing());
        MockMultipartFile csvFile = FileUtils.getFileFromResources("addChannelListing.csv");
        channelService.addChannelListing("FLIPKART", "clientName", csvFile);
    }

    @Test
    public void addChannelListingInvalidCSV(){
        Mockito.when(userDao.selectByName(Mockito.anyString())).thenReturn(TestUtils.getClientUser());
        Mockito.when(channelDao.findByChannelName(Mockito.anyString())).thenReturn(TestUtils.getChannel());
        Mockito.when(productDao.findByClientSkuId(Mockito.anyString())).thenReturn(TestUtils.getProduct());
        Mockito.when(channelListingDao.findByChannelSkuIdAndChannelIdAndGlobalSkuId(Mockito.anyLong(), Mockito.anyString(), Mockito.anyLong())).thenReturn(null);
        Mockito.when(channelListingDao.saveOrUpdate(Mockito.any())).thenReturn(new ChannelListing());
        MockMultipartFile csvFile = FileUtils.getFileFromResources("invalidCSV.html");
        try {
            channelService.addChannelListing("FLIPKART", "clientName", csvFile);
        } catch (ApiException e) {
            Assert.assertEquals(e.getCode(), Constants.CODE_ERROR_PARSING_CSV_FILE);
        }
    }

    @Test
    public void addChannelListingInvalidClientName(){
        Mockito.when(userDao.selectByName(Mockito.anyString())).thenReturn(TestUtils.getCustomerUser());
        Mockito.when(channelDao.findByChannelName(Mockito.anyString())).thenReturn(TestUtils.getChannel());
        Mockito.when(productDao.findByClientSkuId(Mockito.anyString())).thenReturn(TestUtils.getProduct());
        Mockito.when(channelListingDao.findByChannelSkuIdAndChannelIdAndGlobalSkuId(Mockito.anyLong(), Mockito.anyString(), Mockito.anyLong())).thenReturn(null);
        Mockito.when(channelListingDao.saveOrUpdate(Mockito.any())).thenReturn(new ChannelListing());
        MockMultipartFile csvFile = FileUtils.getFileFromResources("addChannelListing.csv");
        try {
            channelService.addChannelListing("FLIPKART", "clientName", csvFile);
        } catch (ApiException e){
            Assert.assertEquals(e.getCode(), Constants.CODE_INVALID_USER);
        }
    }

    @Test
    public void addChannelListingInvalidClient(){
        Mockito.when(userDao.selectByName(Mockito.anyString())).thenReturn(null);
        Mockito.when(channelDao.findByChannelName(Mockito.anyString())).thenReturn(TestUtils.getChannel());
        Mockito.when(productDao.findByClientSkuId(Mockito.anyString())).thenReturn(TestUtils.getProduct());
        Mockito.when(channelListingDao.findByChannelSkuIdAndChannelIdAndGlobalSkuId(Mockito.anyLong(), Mockito.anyString(), Mockito.anyLong())).thenReturn(null);
        Mockito.when(channelListingDao.saveOrUpdate(Mockito.any())).thenReturn(new ChannelListing());
        MockMultipartFile csvFile = FileUtils.getFileFromResources("addChannelListing.csv");
        try {
            channelService.addChannelListing("FLIPKART", "clientName", csvFile);
        } catch (ApiException e){
            Assert.assertEquals(e.getCode(), Constants.CODE_INVALID_USER);
        }
    }

    @Test
    public void addChannelListingInvalidProduct(){
        Mockito.when(userDao.selectByName(Mockito.anyString())).thenReturn(TestUtils.getClientUser());
        Mockito.when(channelDao.findByChannelName(Mockito.anyString())).thenReturn(TestUtils.getChannel());
        Mockito.when(productDao.findByClientSkuId(Mockito.anyString())).thenReturn(null);
        Mockito.when(channelListingDao.findByChannelSkuIdAndChannelIdAndGlobalSkuId(Mockito.anyLong(), Mockito.anyString(), Mockito.anyLong())).thenReturn(null);
        Mockito.when(channelListingDao.saveOrUpdate(Mockito.any())).thenReturn(new ChannelListing());
        MockMultipartFile csvFile = FileUtils.getFileFromResources("addChannelListing.csv");
        try {
            channelService.addChannelListing("FLIPKART", "clientName", csvFile);
        }catch (ApiException e){
            Assert.assertEquals(e.getCode(), Constants.CODE_PRODUCT_NOT_FOUND);
        }

    }

    @Test
    public void addChannelListingDuplicateOrderID(){
        Mockito.when(userDao.selectByName(Mockito.anyString())).thenReturn(TestUtils.getClientUser());
        Mockito.when(channelDao.findByChannelName(Mockito.anyString())).thenReturn(TestUtils.getChannel());
        Mockito.when(productDao.findByClientSkuId(Mockito.anyString())).thenReturn(TestUtils.getProduct());
        Mockito.when(channelListingDao.findByChannelSkuIdAndChannelIdAndGlobalSkuId(Mockito.anyLong(), Mockito.anyString(), Mockito.anyLong())).thenReturn(new ChannelListing());
        Mockito.when(channelListingDao.saveOrUpdate(Mockito.any())).thenReturn(new ChannelListing());
        MockMultipartFile csvFile = FileUtils.getFileFromResources("addChannelListing.csv");
        try{
            channelService.addChannelListing("FLIPKART", "clientName", csvFile);
        } catch (ApiException e){
            Assert.assertEquals(e.getCode(), Constants.CODE_DUPLICATE_CHANNEL_ORDER_ID);
        }
    }

    @Test
    public void addChannelListingInvalidChannelName(){
        Mockito.when(userDao.selectByName(Mockito.anyString())).thenReturn(TestUtils.getClientUser());
        Mockito.when(channelDao.findByChannelName(Mockito.anyString())).thenReturn(null);
        Mockito.when(productDao.findByClientSkuId(Mockito.anyString())).thenReturn(TestUtils.getProduct());
        Mockito.when(channelListingDao.findByChannelSkuIdAndChannelIdAndGlobalSkuId(Mockito.anyLong(), Mockito.anyString(), Mockito.anyLong())).thenReturn(null);
        Mockito.when(channelListingDao.saveOrUpdate(Mockito.any())).thenReturn(new ChannelListing());
        MockMultipartFile csvFile = FileUtils.getFileFromResources("addChannelListing.csv");
        try {
            channelService.addChannelListing("FLIPKART", "clientName", csvFile);
        }catch (ApiException e){
            Assert.assertEquals(e.getCode(), Constants.CODE_INAVLID_CHANNEL);
        }
    }

}
