package com.github.rand0m86.servlets;

import org.asynchttpclient.Response;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class DefaultProxyResponseBuilder implements ProxyResponseBuilder {

    @Override
    public void prepareResponse(Response source, HttpServletResponse target) throws IOException, ServletException {
        target.reset();
        target.getWriter().write(source.getResponseBody(StandardCharsets.UTF_8));
        target.setStatus(source.getStatusCode());
        copyHeaders(source, target);
    }

    private void copyHeaders(Response clientResponse, HttpServletResponse servletResponse) {
        for (Map.Entry<String, String> header : clientResponse.getHeaders()) {
            servletResponse.addHeader(header.getKey(), header.getValue());
        }
    }
}
