package com.github.rand0m86.filters.hop_by_hop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.rand0m86.filters.hop_by_hop.HopByHopHeaderUtil.isHopByHopHeader;

class RequestWithoutHopByHopHeaders extends HttpServletRequestWrapper {

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
            List<String> filtered = Collections.list(headerNames).stream()
                    .filter(header -> !isHopByHopHeader(header))
                    .collect(Collectors.toList());
            return Collections.enumeration(filtered);
        }

        @Override
        public Enumeration<String> getHeaders(String name) {
            if (isHopByHopHeader(name)) {
                return Collections.emptyEnumeration();
            }
            return super.getHeaders(name);
        }
    }
