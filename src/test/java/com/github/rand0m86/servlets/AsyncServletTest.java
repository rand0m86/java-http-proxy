package com.github.rand0m86.servlets;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AsyncServletTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private Request outboundProxyRequest;
    private AsyncHttpClient asyncHttpClient;
    private BoundRequestBuilder boundRequestBuilder;
    private ProxyRequestBuilder requestBuilder;
    private ProxyResponseBuilder responseBuilder;
    private AsyncServlet asyncServlet;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        outboundProxyRequest = mock(Request.class);
        boundRequestBuilder = mock(BoundRequestBuilder.class);
        asyncHttpClient = mock(AsyncHttpClient.class);
        requestBuilder = mock(ProxyRequestBuilder.class);
        responseBuilder = mock(ProxyResponseBuilder.class);
        asyncServlet = new AsyncServlet(asyncHttpClient, requestBuilder, responseBuilder);
    }

    @Test
    void shouldStartAsyncRequestHandlingWithReceivedRequestAndResponse() throws Exception {
        when(requestBuilder.prepareRequest(request)).thenReturn(outboundProxyRequest);
        when(asyncHttpClient.prepareRequest(any(Request.class))).thenReturn(boundRequestBuilder);

        asyncServlet.service(request, response);

        verify(request).startAsync(request, response);
    }
}