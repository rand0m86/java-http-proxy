package com.github.rand0m86.servlets;

import com.github.rand0m86.util.EnumerationUtil;
import org.asynchttpclient.Request;
import org.asynchttpclient.RequestBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.stream.Collectors;

public class DefaultProxyRequestBuilder implements ProxyRequestBuilder {

    @Override
    public Request prepareRequest(HttpServletRequest servletRequest) throws ServletException, IOException {
        RequestBuilder requestBuilder = new RequestBuilder(servletRequest.getMethod(), false, true);
        populateUrl(servletRequest, requestBuilder);
        populateRequestBody(servletRequest, requestBuilder);
        populateHeaders(servletRequest, requestBuilder);
        return requestBuilder.build();
    }

    private void populateUrl(HttpServletRequest servletRequest, RequestBuilder requestBuilder) {
        String query = servletRequest.getQueryString();
        StringBuffer url = servletRequest.getRequestURL();
        if (null != query) {
            url.append("?").append(query);
        }
        requestBuilder.setUrl(url.toString());
    }

    private void populateHeaders(HttpServletRequest source, RequestBuilder target) {
        EnumerationUtil.enumerationToStream(source.getHeaderNames())
                .forEach(header -> target.addHeader(
                        header,
                        EnumerationUtil.enumerationToList(source.getHeaders(header)))
                );
    }

    private void populateRequestBody(HttpServletRequest source, RequestBuilder target) throws IOException {
        String body = source.getReader().lines().collect(Collectors.joining());
        target.setBody(body);
    }
}
