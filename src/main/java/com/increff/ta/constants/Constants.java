package com.increff.ta.constants;

public class Constants {
    public static Integer CODE_USERNAME_ALREADY_IN_USE = -2;
    public static Integer CODE_ERROR_PARSING_CSV_FILE = -3;
    public static Integer CODE_DUPLICATE_CLIENT_SKU_ID = -4;
    public static Integer CODE_INVALID_USER = -5;
    public static Integer CODE_INVALID_BIN_COUNT = -6;
    public static Integer CODE_BINID_CLIENTSKUID_SHOULD_BE_UNIQUE = -7;
    public static Integer CODE_PRODUCT_NOT_FOUND = -8;
    public static Integer CODE_BIN_NOT_FOUND = -9;
    public static Integer CODE_INVALID_ORDER_ID = -10;
    public static Integer CODE_ITEM_NOT_IN_INVENTORY = -11;
    public static Integer CODE_INTERNAL_CHANNEL_NOT_FOUND = -12;
    public static Integer CODE_DUPLICATE_CHANNEL_ORDER_ID = -13;
    public static Integer CODE_INAVLID_CHANNEL = -14;


    public static String MSG_USERNAME_ALREADY_EXISTS = "Input client name already exists";
    public static String MSG_ERROR_PARSING_CSV_FILE = "Error while parsing the CSV file";
    public static String MSG_DUPLICATE_CLIENT_SKU_ID = "There Cannot be same clientSkuId for a given clientId";
    public static String MSG_INVALID_USER = "Invalid User";
    public static String MSG_INVALID_BIN_COUNT = "Bin count should be greater than 0";
    public static String MSG_BINID_CLIENTSKUID_SHOULD_BE_UNIQUE = "Every csv row should have a unique binId and clientSkuId";
    public static String MSG_PRODUCT_NOT_FOUND = "Cannot find product with the given clientSkuId";
    public static String MSG_BIN_NOT_FOUND = "Cannot find Bin with the given binId";
    public static String MSG_INVALID_ORDER_ID = "Input order ID is not a valid input";
    public static String MSG_ITEM_NOT_IN_INVENTORY = "Input item is not present in inventory";
    public static String MSG_INTERNAL_CHANNEL_NOT_FOUND = "Internal channel is not present";
    public static String MSG_DUPLICATE_CHANNEL_ORDER_ID = "Channel order ID must be unique";
    public static String MSG_INVALID_CHANNEL = "Invalid channel name";
}
