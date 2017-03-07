package com.github.rand0m86;

import com.github.rand0m86.filters.CharacterEncodingFilter;
import com.github.rand0m86.filters.hop_by_hop.HopByHopHeadersRemovalFilter;
import com.github.rand0m86.servlets.AsyncServlet;
import com.github.rand0m86.servlets.DefaultProxyRequestBuilder;
import com.github.rand0m86.servlets.DefaultProxyResponseBuilder;
import com.github.rand0m86.servlets.ProxyRequestBuilder;
import com.github.rand0m86.servlets.ProxyResponseBuilder;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

public class App {
    public static void main(String[] args) throws Exception {
        AsyncHttpClient asyncHttpClient = new DefaultAsyncHttpClient();
        ProxyRequestBuilder requestBuilder = new DefaultProxyRequestBuilder();
        ProxyResponseBuilder responseBuilder = new DefaultProxyResponseBuilder();

        Server server = new Server(8080);
        ServletContextHandler contextHandler = new ServletContextHandler(server, "/");
        ServletHolder servletHolder = new ServletHolder(new AsyncServlet(asyncHttpClient, requestBuilder, responseBuilder));
        servletHolder.setAsyncSupported(true);
        contextHandler.addServlet(servletHolder, "/");
        contextHandler.addFilter(CharacterEncodingFilter.class, "/*", EnumSet.allOf(DispatcherType.class));
        contextHandler.addFilter(HopByHopHeadersRemovalFilter.class, "/*", EnumSet.allOf(DispatcherType.class));

        server.start();
        server.join();
    }
}
