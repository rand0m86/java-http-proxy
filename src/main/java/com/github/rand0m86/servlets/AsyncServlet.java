package com.github.rand0m86.servlets;

import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Request;
import org.asynchttpclient.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AsyncServlet extends HttpServlet {

    private final AsyncHttpClient asyncHttpClient;
    private final ProxyRequestBuilder requestBuilder;
    private final ProxyResponseBuilder responseBuilder;

    private static final Logger logger = LoggerFactory.getLogger(AsyncServlet.class);

    public AsyncServlet(AsyncHttpClient asyncHttpClient, ProxyRequestBuilder requestBuilder, ProxyResponseBuilder responseBuilder) {
        this.asyncHttpClient = asyncHttpClient;
        this.requestBuilder = requestBuilder;
        this.responseBuilder = responseBuilder;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AsyncContext asyncContext = req.startAsync(req, resp);
        Request clientRequest = requestBuilder.prepareRequest(req);
        asyncHttpClient.prepareRequest(clientRequest)
                .execute(new AsyncCompletionHandler<Void>() {

                    @Override
                    public Void onCompleted(Response response) throws Exception {
                        HttpServletResponse asyncContextResponse = (HttpServletResponse) asyncContext.getResponse();
                        responseBuilder.prepareResponse(response, asyncContextResponse);
                        asyncContext.complete();
                        return null;
                    }

                    @Override
                    public void onThrowable(Throwable t) {
                        logger.error(String.format("Error during %s request processing %s", req.getMethod(), req.getRequestURL()), t);
                        asyncContext.complete();
                    }
                });
    }

}
