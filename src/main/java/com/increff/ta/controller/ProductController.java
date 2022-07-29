package com.increff.ta.controller;

import com.increff.ta.model.ProductDetailCSV;
import com.increff.ta.service.ApiException;
import com.increff.ta.service.ProductService;
import com.opencsv.bean.CsvToBeanBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.stream.StreamSupport;

@Api
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @Transactional
    @ApiOperation(value = "Api to add products")
    @RequestMapping(value = "/api/product/addProducts", method = RequestMethod.POST)
    public String addProducts(@RequestParam("file") MultipartFile csvfile, @RequestParam("clientId") Long clientId) throws ApiException {
        productService.addProductsFromCSV(csvfile, clientId);
        return "success";
    }
}
