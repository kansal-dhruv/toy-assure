package com.increff.ta.controller;

import com.increff.ta.commons.exception.ApiException;
import com.increff.ta.dto.BinDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.List;

@Api
@RestController
@Validated
@RequestMapping("/api/bin")
public class BinController {

  private final BinDto binDto;

  @Autowired
  public BinController(BinDto binDto) {
    this.binDto = binDto;
  }

  @ApiOperation(value = "Used to create new bins")
  @RequestMapping(value = "/create", method = RequestMethod.POST)
  public List<Long> createUser(@RequestParam(value = "count") Integer count) throws ApiException {
    return binDto.createBins(count);
  }

  @ApiOperation(value = "Add products to bins")
  @RequestMapping(value = "/product", method = RequestMethod.POST)
  public void putProducts(@RequestBody @NotNull(message = "CSV file cannot be null") MultipartFile csvfile) throws ApiException {
    binDto.putProductsToBin(csvfile);
  }
}