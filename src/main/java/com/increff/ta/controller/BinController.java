package com.increff.ta.controller;

import com.increff.ta.model.UserForm;
import com.increff.ta.service.ApiException;
import com.increff.ta.service.BinService;
import com.increff.ta.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
    public List<Long> createUser(@RequestParam(value = "count") Integer count) throws ApiException {
        return binService.createBins(count);
    }

    @Transactional
    @ApiOperation(value = "Add products in bins")
    @RequestMapping(value = "/api/bin/putProducts", method = RequestMethod.POST)
    public String putProducts(@RequestParam("file") MultipartFile csvfile) throws ApiException, IOException {
        binService.putProductsToBin(csvfile.getBytes());
        return "success";
    }
}