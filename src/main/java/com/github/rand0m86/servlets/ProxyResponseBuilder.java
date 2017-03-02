package com.github.rand0m86.servlets;

import org.asynchttpclient.Response;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ProxyResponseBuilder {
    void prepareResponse(Response source, HttpServletResponse target) throws IOException, ServletException;
}
