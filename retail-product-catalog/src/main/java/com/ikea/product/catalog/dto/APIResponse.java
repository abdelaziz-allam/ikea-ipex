package com.ikea.product.catalog.dto;

import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
public record APIResponse<T>(ResponseStatus status,
                             T result,
                             String code,
                             String message) {
}
