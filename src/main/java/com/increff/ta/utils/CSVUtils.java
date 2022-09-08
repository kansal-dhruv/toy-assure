package com.increff.ta.utils;

import com.increff.ta.api.ApiException;
import com.increff.ta.constants.Constants;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;
import org.apache.commons.collections4.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class CSVUtils {
    public static <T> List<T> parseCSV(byte[] csvBytes, Class<? extends T> type) {
        CsvToBean<T> csvBean = null;
        List<T> parsedData = new ArrayList<>();
        try {
            csvBean = new CsvToBeanBuilder(new InputStreamReader(new ByteArrayInputStream(csvBytes), "UTF8"))
                    .withType(type).withThrowExceptions(false).withSkipLines(1).build();
            parsedData = csvBean.parse();
            List<CsvException> errors = csvBean.getCapturedExceptions();
            if(CollectionUtils.isNotEmpty(errors))
                handleErrors(errors);
        } catch (UnsupportedEncodingException e) {
            throw new ApiException(Constants.CODE_ERROR_PARSING_CSV_FILE, Constants.MSG_ERROR_PARSING_CSV_FILE);
        }
        return parsedData;
    }

    private static void handleErrors(List<CsvException> errors){
        List<String> errorMessages = new ArrayList<>();
        for(CsvException e : errors){
            String errorMessage = e.getMessage().replaceAll("\\.", "");
            errorMessages.add(errorMessage + " at line number : " + e.getLineNumber());
        }
        throw new ApiException(Constants.CODE_ERROR_PARSING_CSV_FILE, Constants.MSG_ERROR_PARSING_CSV_FILE, errorMessages);
    }
}