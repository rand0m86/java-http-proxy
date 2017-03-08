package com.github.rand0m86.filters.hop_by_hop;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import static com.github.rand0m86.filters.hop_by_hop.HopByHopHeaderUtil.isHopByHopHeader;

class ResponseWithoutHopByHopHeaders extends HttpServletResponseWrapper {

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
        return super.getHeaderNames().stream()
                .filter(header -> !isHopByHopHeader(header))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<String> getHeaders(String name) {
        if (isHopByHopHeader(name)) {
            return Collections.emptyList();
        }
        return super.getHeaders(name);
    }

    @Override
    public void addHeader(String name, String value) {
        if (!isHopByHopHeader(name)) {
            super.addHeader(name, value);
        }
    }

    @Override
    public void setHeader(String name, String value) {
        if (!isHopByHopHeader(name)) {
            super.setHeader(name, value);
        }
    }

    @Override
    public void addDateHeader(String name, long date) {
        if (!isHopByHopHeader(name)) {
            super.addDateHeader(name, date);
        }
    }

    @Override
    public void setDateHeader(String name, long date) {
        if (!isHopByHopHeader(name)) {
            super.setDateHeader(name, date);
        }
    }

    @Override
    public void addIntHeader(String name, int value) {
        if (!isHopByHopHeader(name)) {
            super.addIntHeader(name, value);
        }
    }

    @Override
    public void setIntHeader(String name, int value) {
        if (!isHopByHopHeader(name)) {
            super.setIntHeader(name, value);
        }
    }
}
