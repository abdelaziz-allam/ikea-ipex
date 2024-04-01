package com.ikea.product.catalog.api.helper;

import com.ikea.product.catalog.dto.APIResponse;
import com.ikea.product.catalog.dto.APIResponseBuilder;
import com.ikea.product.catalog.dto.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class APIHelperUtil<T> {


    public  ResponseEntity<APIResponse<T>> createUnifiedResponse(T body, HttpStatus status, String message, String code) {
        APIResponse<T> apiResponse = getApiResponseInstance(body, status,code);
        APIResponse<T> response = APIResponseBuilder
                .<T>builder()
                .status(apiResponse.status())
                .code(code)
                .result(body)
                .message(message)
                .build();
        return new ResponseEntity<>(response, status);
    }

    private APIResponse<T> getApiResponseInstance(T body, HttpStatus status,String code) {
        return APIResponseBuilder
                .<T>builder()
                .status(status.is2xxSuccessful() ? ResponseStatus.SUCCESS : ResponseStatus.ERROR)
                .code(code)
                .result(body)
                .build();
    }
}

