package com.increff.ta.controller;

import com.increff.ta.dto.ProductDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Api
@RestController
@Validated
@RequestMapping("/api/product")
public class ProductController {

  private final ProductDto productDto;

  @Autowired
  public ProductController(ProductDto productDto) {
    this.productDto = productDto;
  }

  @ApiOperation(value = "Api to add products")
  @RequestMapping(value = "/{clientId}", method = RequestMethod.POST)
  public void addProducts(@RequestBody MultipartFile csvfile,
                                            @PathVariable("clientId") Long clientId) {
    productDto.addProductsFromCSV(csvfile, clientId);
  }
}