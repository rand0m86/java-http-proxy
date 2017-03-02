package com.github.rand0m86.servlets;

import org.asynchttpclient.Request;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface ProxyRequestBuilder {
    Request prepareRequest(HttpServletRequest request) throws ServletException, IOException;
}
