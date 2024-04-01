package com.ikea.product.catalog.log;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.StringJoiner;
import java.util.UUID;

import static com.ikea.product.catalog.constant.AppConstants.*;
import static com.ikea.product.catalog.constant.AppConstants.APIs.*;
import static com.ikea.product.catalog.constant.AppConstants.Formats.*;
import static org.apache.commons.lang3.BooleanUtils.TRUE;

@Component
@Order(1)
@Slf4j
@ConditionalOnProperty(value = APIS_LOGGING_ENABLED, havingValue = TRUE)
public class APILoggerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException {
        MDC.put(HTTP_SOURCE_HEADER, request.getHeader(HTTP_SOURCE_HEADER));

        doFilterWrapped(wrapRequest(request), wrapResponse(response), filterChain);
    }


    protected void doFilterWrapped(
            ContentCachingRequestWrapper request,
            ContentCachingResponseWrapper response,
            FilterChain filterChain)
            throws IOException {
        String requestURL = request.getMethod().concat(SPACE).concat(request.getRequestURL().toString());
        try {
            checkCorrelationId(request);
            filterChain.doFilter(request, response);

            if (!(REST_LIVELINESS.equalsIgnoreCase(request.getRequestURI()))
                    && !(REST_READINESS.equalsIgnoreCase(request.getRequestURI()))) {
                logRequest(request, requestURL);
            }
            MDC.put(HTTP_RESPONSE_CODE_HEADER, String.valueOf(response.getStatus()));
            response.addHeader(CORRELATION_ID_HEADER, MDC.get(CORRELATION_ID_HEADER));
        } catch (Exception e) {
            log.error(EXCEPTION_STACKTRACE, ExceptionUtils.getStackTrace(e));
        } finally {
            if (!(REST_LIVELINESS.equalsIgnoreCase(request.getRequestURI()))
                    && !(REST_READINESS.equalsIgnoreCase(request.getRequestURI()))) {
                logResponse(response, requestURL);
            }
            response.copyBodyToResponse();
            MDC.clear();
        }
    }

    private void checkCorrelationId(ContentCachingRequestWrapper request) {
        String correlationId = ((HttpServletRequest) request).getHeader(CORRELATION_ID_HEADER);

        if (!StringUtils.hasLength(correlationId)) {
            correlationId = UUID.randomUUID().toString();
        }

        MDC.put(CORRELATION_ID_HEADER, correlationId);
    }

    private void logRequest(ContentCachingRequestWrapper request, String requestURL) {
        if (LOG_API_LOGGER_RESPONSE_HTTP_METHOD.equalsIgnoreCase(request.getMethod())) {
            String body = request.getContentAsString();
            log.info(LOG_API_LOGGER_REQUEST_RECEIVED, requestURL, body);
        }
    }

    private void logResponse(ContentCachingResponseWrapper httpServletResponse, String requestName)
            throws IOException {
        final StringBuilder logMessage =
                new StringBuilder(LOG_API_LOGGER_RESPONSE)
                        .append(httpServletResponse.getStatus())
                        .append(LOG_API_LOGGER_RESPONSE_REQUEST).append(requestName);
        if (log.isDebugEnabled()) {
            Iterator<String> headerNames = httpServletResponse.getHeaderNames().iterator();
            StringJoiner headers = new StringJoiner(COMMA_DELIMITER);
            while (headerNames.hasNext()) {
                String headerName = headerNames.next();
                headers.add(headerName.concat(EQUAL_SIGN).concat(httpServletResponse.getHeader(headerName)));
            }

            logMessage.append(LOG_API_LOGGER_HEADERS).append(headers).append(CLOSING_BRACKET);
            logMessage
                    .append(LOG_API_LOGGER_BODY)
                    .append(performResponseAudit(httpServletResponse));
            log.debug(logMessage.toString());
        } else {
            log.info(logMessage.toString());
        }
    }


    private ContentCachingRequestWrapper wrapRequest(HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper) {
            return (ContentCachingRequestWrapper) request;
        } else {
            return new ContentCachingRequestWrapper(request);
        }
    }

    private ContentCachingResponseWrapper wrapResponse(HttpServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper) {
            return (ContentCachingResponseWrapper) response;
        } else {
            return new ContentCachingResponseWrapper(response);
        }
    }

    private String performResponseAudit(
            ContentCachingResponseWrapper responseWrapper) throws IOException {
        String payLoadFromByteArray = EMPTY_STRING;
        if (responseWrapper != null) {
            responseWrapper.getContentAsByteArray();
            if (responseWrapper.getContentAsByteArray().length > 0) {
                payLoadFromByteArray =
                        getPayLoadFromByteArray(
                                responseWrapper.getContentAsByteArray(), responseWrapper.getCharacterEncoding());
                responseWrapper.copyBodyToResponse();
            }
        }
        return payLoadFromByteArray;
    }

    private String getPayLoadFromByteArray(byte[] requestBuffer, String charEncoding) {
        String payLoad;
        try {
            payLoad = new String(requestBuffer, charEncoding);
        } catch (UnsupportedEncodingException ex) {
            log.error(LOG_API_LOGGER_RESPONSE_ENCODING, ExceptionUtils.getStackTrace(ex));
            payLoad = UNSUPPORTED_ENCODING;
        }
        return payLoad;
    }

}
