package com.ikea.product.catalog.constant;


public final class AppConstants {

  public static final String METHOD_LOG_BEFORE_METHOD_CALL =
          "Before method {} . {} is called.";

  public static final String METHOD_LOG_AFTER_METHOD_CALL =
          "After method {} .{} is called.";

  public static final String METHOD_LOG_PARAMETER_USED =
          "Parameters used in: {} .{} are ::: {}";

  public static final String EXCEPTION_STACKTRACE = "Exception stacktrace :: {} ";

  public static final String LOG_API_LOGGER_REQUEST_RECEIVED =
          "REST Request received for {} . With body :\n{}";
  public static final String LOG_API_LOGGER_RESPONSE = "REST Response sent with status: ";
  public static final String LOG_API_LOGGER_RESPONSE_REQUEST = " for request : ";
  public static final String LOG_API_LOGGER_HEADERS = "\n Response headers -> [";
  public static final String LOG_API_LOGGER_BODY = "\n Response Body -> ";
  public static final String LOG_API_LOGGER_RESPONSE_HTTP_METHOD = "POST";
  public static final String LOG_API_LOGGER_RESPONSE_ENCODING =
          "Error While Encoding Response {}";
  public static final String APIS_LOGGING_ENABLED = "apis.logging.enabled";
  public static final String UNSUPPORTED_ENCODING = "Unsupported-Encoding";



  public static class APIs {
    private APIs() {}
    public static final String CORRELATION_ID_HEADER = "transactionId";
    public static final String REST_LIVELINESS = "/actuator/health/liveliness";
    public static final String REST_READINESS = "/actuator/health/readiness";
    public static final String HTTP_RESPONSE_CODE_HEADER = "http_response_code";
    public static final String HTTP_SOURCE_HEADER = "source";

    public static final String PRODUCT_CATALOG_200_1 = "PRODUCT_CATALOG.200.1";
    public static final String PRODUCT_CATALOG_200_2 = "PRODUCT_CATALOG.200.2";
    public static final String PRODUCT_CATALOG_200_3 = "PRODUCT_CATALOG.200.3";
    public static final String PRODUCT_CATALOG_200_4 = "PRODUCT_CATALOG.200.4";
    public static final String EXCEPTION = "PRODUCT_CATALOG.EXCEPTION";
  }

  public static class Formats {
    private Formats() {}
    public static final String EMPTY_STRING = "";
    public static final String COMMA_DELIMITER = ", ";
    public static final String EQUAL_SIGN = " = ";
    public static final String CLOSING_BRACKET = "]";
    public static final String SPACE = " ";
  }


  private AppConstants() {}
}
