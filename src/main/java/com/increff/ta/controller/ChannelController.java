package com.increff.ta.controller;

import com.increff.ta.pojo.Channel;
import com.increff.ta.service.ChannelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

@Api
@RestController
public class ChannelController {

    @Autowired
    private ChannelService channelService;

    @Transactional
    @ApiOperation(value = "Api to create a channel")
    @RequestMapping(value = "/api/channel/addChannel", method = RequestMethod.POST)
    public String addChannel(@RequestBody Channel channel) {
        channelService.addChannel(channel);
        return "success";
    }

    @Transactional
    @ApiOperation(value = "Api to create channel listings")
    @RequestMapping(value = "/api/channel/listing", method = RequestMethod.POST)
    public String addChannelListing(@RequestParam("channelName") String channelName, @RequestParam("clientName") String clientName, @RequestParam("csvFile") MultipartFile csvFile) {
        channelService.addChannelListing(channelName, clientName, csvFile);
        return "success";
    }
}