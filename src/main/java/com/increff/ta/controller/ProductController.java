package com.increff.ta.controller;

import com.increff.ta.controller.handler.ResponseHandler;
import com.increff.ta.dto.ProductDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Api
@RestController
@Validated
@RequestMapping("/api/product")
public class ProductController {

  @Autowired
  private ProductDto productDto;

  @ApiOperation(value = "Api to add products")
  @RequestMapping(value = "/{clientId}", method = RequestMethod.POST)
  public ResponseEntity<Object> addProducts(@RequestBody MultipartFile csvfile,
                                            @PathVariable("clientId") Long clientId) {
    productDto.addProductsFromCSV(csvfile, clientId);
    return ResponseHandler.successResponse();
  }
}