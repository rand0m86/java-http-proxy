package com.github.rand0m86.filters.hop_by_hop;

import com.github.rand0m86.filters.AbstractHttpFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HopByHopHeadersRemovalFilter extends AbstractHttpFilter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        RequestWithoutHopByHopHeaders wrappedRequest = new RequestWithoutHopByHopHeaders((HttpServletRequest) request);
        ResponseWithoutHopByHopHeaders wrappedResponse = new ResponseWithoutHopByHopHeaders((HttpServletResponse) response);
        chain.doFilter(wrappedRequest, wrappedResponse);
    }

}
