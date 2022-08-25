package com.increff.ta.controller;

import com.increff.ta.controller.handler.ResponseHandler;
import com.increff.ta.pojo.Channel;
import com.increff.ta.service.ChannelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Api
@RestController
@Validated
public class ChannelController {

    @Autowired
    private ChannelService channelService;

    @Transactional
    @ApiOperation(value = "Api to create a channel")
    @RequestMapping(value = "/api/channel/addChannel", method = RequestMethod.POST)
    public ResponseEntity<Object> addChannel(@RequestBody @Valid Channel channel) {
        channelService.addChannel(channel);
        return ResponseHandler.successResponse();
    }

    @Transactional
    @ApiOperation(value = "Api to create channel listings")
    @RequestMapping(value = "/api/channel/listing", method = RequestMethod.POST)
    public ResponseEntity<Object> addChannelListing(@RequestParam("channelName") @NotBlank(message = "Channel name cannot be Null") String channelName,
                                                    @RequestParam("clientName") @NotBlank(message = "Client name cannot be Blank") String clientName,
                                                    @RequestParam("csvFile") @NotNull(message = "CSV file is mandatory") MultipartFile csvFile) {
        channelService.addChannelListing(channelName, clientName, csvFile);
        return ResponseHandler.successResponse();
    }
}
