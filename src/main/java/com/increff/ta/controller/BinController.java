package com.increff.ta.controller;

import com.increff.ta.controller.handler.ResponseHandler;
import com.increff.ta.service.ApiException;
import com.increff.ta.service.BinService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@Api
@RestController
public class BinController {

    @Autowired
    private BinService binService;

    @ApiOperation(value = "Used to create new bins")
    @RequestMapping(value = "api/bin/create", method = RequestMethod.POST)
    public ResponseEntity<Object> createUser(@RequestParam(value = "count") Integer count) throws ApiException {
        List<Long> responseObj = binService.createBins(count);
        return ResponseHandler.generateResponse("Success", HttpStatus.OK, responseObj);
    }

    @Transactional
    @ApiOperation(value = "Add products in bins")
    @RequestMapping(value = "/api/bin/putProducts", method = RequestMethod.POST)
    public ResponseEntity<Object> putProducts(@RequestParam("file") MultipartFile csvfile) throws ApiException, IOException {
        binService.putProductsToBin(csvfile.getBytes());
        return ResponseHandler.successResponse();
    }
}