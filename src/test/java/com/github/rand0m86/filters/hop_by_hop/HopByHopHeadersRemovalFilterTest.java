package com.github.rand0m86.filters.hop_by_hop;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

class HopByHopHeadersRemovalFilterTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;
    private HopByHopHeadersRemovalFilter filter = new HopByHopHeadersRemovalFilter();

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);
    }

    @Test
    void expectCallToFilterChainWithWrappedRequestAndResponse() throws Exception {
        ArgumentCaptor<ServletRequest> requestCaptor = ArgumentCaptor.forClass(ServletRequest.class);
        ArgumentCaptor<ServletResponse> responseCaptor = ArgumentCaptor.forClass(ServletResponse.class);

        doNothing().when(filterChain).doFilter(requestCaptor.capture(), responseCaptor.capture());

        filter.doFilter(request, response, filterChain);

        assertThat(requestCaptor.getValue()).isInstanceOf(RequestWithoutHopByHopHeaders.class);
        assertThat(responseCaptor.getValue()).isInstanceOf(ResponseWithoutHopByHopHeaders.class);
    }

}