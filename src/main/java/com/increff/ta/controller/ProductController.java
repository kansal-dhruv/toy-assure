package com.increff.ta.controller;

import com.increff.ta.controller.handler.ResponseHandler;
import com.increff.ta.service.ApiException;
import com.increff.ta.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;

@Api
@RestController
@Validated
public class ProductController {

    @Autowired
    private ProductService productService;

    @Transactional
    @ApiOperation(value = "Api to add products")
    @RequestMapping(value = "/api/product/addProducts", method = RequestMethod.POST)
    public ResponseEntity<Object> addProducts(@RequestParam("file") @NotNull(message = "CSV file cannot be null") MultipartFile csvfile,
                                              @RequestParam("clientId") @NotNull(message = "Client ID cannot be null") Long clientId) throws ApiException {
        productService.addProductsFromCSV(csvfile, clientId);
        return ResponseHandler.successResponse();
    }
}
