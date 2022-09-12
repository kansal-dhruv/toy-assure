package com.increff.ta.utils;

import com.opencsv.bean.BeanField;
import com.opencsv.bean.validators.StringValidator;
import com.opencsv.exceptions.CsvValidationException;

public class MustBePositiveNumber implements StringValidator {

  @Override
  public boolean isValid(String value) {
    double num = 0;
    boolean valid = true;
    if (value == null)
      valid = false;
    else {
      num = Double.parseDouble(value);
    }
    return valid && num > 0;
  }

  @Override
  public void validate(String value, BeanField field) throws CsvValidationException {
    boolean isValid = false;
    try {
      isValid = isValid(value);
    }
    catch (NumberFormatException e) {
      throw new CsvValidationException("Field '" + field.getField().getName() + "' must be a positive number");
    }
    if (!isValid) {
      throw new CsvValidationException("Field '" + field.getField().getName() + "' cannot be negative or zero");
    }
  }

  @Override
  public void setParameterString(String value) {
    return;
  }
}