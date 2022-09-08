package com.increff.ta.dto;

import com.increff.ta.api.ApiException;
import com.increff.ta.api.ChannelApi;
import com.increff.ta.constants.Constants;
import com.increff.ta.model.ChannelForm;
import com.increff.ta.model.ChannelListingCSV;
import com.increff.ta.pojo.Channel;
import com.increff.ta.utils.CSVUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ChannelDto {

    @Autowired
    private ChannelApi channelApi;

    public void createChannel(ChannelForm channelForm){
        Channel channel = convertChannelFormToPojo(channelForm);
        channelApi.addChannel(channel);
    }

    public void addChannelListing(String channelName, String clientName, MultipartFile csvFile) {
        List<ChannelListingCSV> channelListingDetails = null;
        try {
            channelListingDetails = CSVUtils.parseCSV(csvFile.getBytes(), ChannelListingCSV.class);
        } catch (IOException e) {
            throw new ApiException(Constants.CODE_ERROR_PARSING_CSV_FILE, Constants.MSG_ERROR_PARSING_CSV_FILE);
        }
        for(ChannelListingCSV channelListingCSV : channelListingDetails) {
            channelApi.addChannelListing(channelName, clientName, channelListingCSV.getChannelSkuId(), channelListingCSV.getClientSkuId());
        }
    }

    private Channel convertChannelFormToPojo(ChannelForm channelForm){
        Channel channel = new Channel();
        channel.setName(channelForm.getName());
        channel.setInvoiceType(channelForm.getInvoiceType());
        return channel;
    }
}
