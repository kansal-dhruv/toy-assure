package com.increff.ta.controller;

import com.increff.ta.commons.model.ChannelForm;
import com.increff.ta.dto.ChannelDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Api
@RestController
@Validated
@RequestMapping("/api/channel")
public class ChannelController {

  @Autowired
  private final ChannelDto channelDto;

  public ChannelController(ChannelDto channelDto) {
    this.channelDto = channelDto;
  }

  @ApiOperation(value = "Api to create a channel")
  @RequestMapping(value = "/", method = RequestMethod.POST)
  public void addChannel(@RequestBody @Valid ChannelForm channelForm) {
    channelDto.createChannel(channelForm);
  }

  @ApiOperation(value = "Api to create channel listings")
  @RequestMapping(value = "/listing", method = RequestMethod.POST)
  public void addChannelListing(@RequestParam("channelName") @NotBlank(message = "ChannelPojo name cannot be Null") String channelName,
                                                  @RequestParam("clientName") @NotBlank(message = "Client name cannot be Blank") String clientName,
                                                  @RequestPart("csvFile") @NotNull(message = "CSV file is mandatory") MultipartFile csvFile) {
    channelDto.addChannelListing(channelName, clientName, csvFile);
  }
}