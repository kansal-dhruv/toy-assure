package com.increff.ta.controller;

import com.increff.ta.api.ApiException;
import com.increff.ta.controller.handler.ResponseHandler;
import com.increff.ta.dto.BinDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

@Api
@RestController
@Validated
public class BinController {

    @Autowired
    private BinDto binDto;

    @ApiOperation(value = "Used to create new bins")
    @RequestMapping(value = "api/bin/create", method = RequestMethod.POST)
    public ResponseEntity<Object> createUser(@RequestParam(value = "count") Integer count) throws ApiException {
        List<Long> responseObj = binDto.createBins(count);
        return ResponseHandler.generateResponse("Success", HttpStatus.OK, responseObj);
    }

    @ApiOperation(value = "Add products in bins")
    @RequestMapping(value = "/api/bin/putProducts", method = RequestMethod.POST)
    public ResponseEntity<Object> putProducts(@RequestBody @NotNull(message = "CSV file cannot be null") MultipartFile csvfile) throws ApiException, IOException {
        binDto.putProductsToBin(csvfile);
        return ResponseHandler.successResponse();
    }
}