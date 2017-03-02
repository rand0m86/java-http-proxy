package com.github.rand0m86.filters;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class HopByHopHeadersRemovalFilter extends AbstractHttpFilter {

    private static final List<String> HOP_BY_HOP_HEADERS = Arrays.asList(
            "connection", "keep-alive", "proxy-authenticate", "proxy-authorization",
            "te", "trailer", "transfer-encoding", "upgrade"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        RequestWithoutHopByHopHeaders wrappedRequest = new RequestWithoutHopByHopHeaders((HttpServletRequest) request);
        ResponseWithoutHopByHopHeaders wrappedResponse = new ResponseWithoutHopByHopHeaders((HttpServletResponse) response);
        chain.doFilter(wrappedRequest, wrappedResponse);
    }

    private static boolean isHopByHopHeader(String header) {
        if (null == header) {
            return false;
        }
        return HOP_BY_HOP_HEADERS.contains(header.toLowerCase());
    }

    private static class RequestWithoutHopByHopHeaders extends HttpServletRequestWrapper {

        RequestWithoutHopByHopHeaders(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String getHeader(String name) {
            if (isHopByHopHeader(name)) {
                return null;
            }
            return super.getHeader(name);
        }

        @Override
        public int getIntHeader(String name) {
            if (isHopByHopHeader(name)) {
                return -1;
            }
            return super.getIntHeader(name);
        }

        @Override
        public long getDateHeader(String name) {
            if (isHopByHopHeader(name)) {
                return -1;
            }
            return super.getDateHeader(name);
        }

        @Override
        public Enumeration<String> getHeaderNames() {
            Enumeration<String> headerNames = super.getHeaderNames();
            if (null == headerNames) {
                return null;
            }
            List<String> filteredHeaderNames = new ArrayList<>();
            while (headerNames.hasMoreElements()) {
                filteredHeaderNames.add(headerNames.nextElement().toLowerCase());
            }
            filteredHeaderNames.removeAll(HOP_BY_HOP_HEADERS);
            return Collections.enumeration(filteredHeaderNames);
        }

        @Override
        public Enumeration<String> getHeaders(String name) {
            if (isHopByHopHeader(name)) {
                return Collections.emptyEnumeration();
            }
            return super.getHeaders(name);
        }
    }

    private static class ResponseWithoutHopByHopHeaders extends HttpServletResponseWrapper {

        ResponseWithoutHopByHopHeaders(HttpServletResponse response) {
            super(response);
        }

        @Override
        public String getHeader(String name) {
            if (isHopByHopHeader(name)) {
                return null;
            }
            return super.getHeader(name);
        }

        @Override
        public Collection<String> getHeaderNames() {
            Collection<String> headerNames = new ArrayList<>(super.getHeaderNames());
            headerNames.removeAll(HOP_BY_HOP_HEADERS);
            return headerNames;
        }

        @Override
        public Collection<String> getHeaders(String name) {
            if (isHopByHopHeader(name)) {
                return Collections.emptyList();
            }
            return super.getHeaders(name);
        }
    }
}
