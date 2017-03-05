package com.github.rand0m86.filters;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

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

    @Nested
    @DisplayName("Tests for not modifying any non transitional headers on requests")
    class PositiveRequestTests {

        @Test
        void shouldPassThroughNotTransitionalHeadersWhenGetHeaderMethodCalled() throws Exception {
            when(request.getHeader("x-custom-header")).thenReturn("some value");
            ArgumentCaptor<ServletRequest> captor = captureRequest(filterChain);

            filter.doFilter(request, response, filterChain);

            HttpServletRequest filteredRequest = getFilteredRequest(captor);
            assertThat(filteredRequest.getHeader("x-custom-header")).isEqualTo("some value");
        }

        @Test
        void shouldPassThroughNotTransitionalHeadersWhenGetHeaderNamesMethodCalled() throws Exception {
            when(request.getHeaderNames()).thenReturn(Collections.enumeration(Collections.singletonList("x-custom-header")));
            ArgumentCaptor<ServletRequest> captor = captureRequest(filterChain);

            filter.doFilter(request, response, filterChain);

            HttpServletRequest filteredRequest = getFilteredRequest(captor);
            ArrayList<String> filteredRequestHeaders = Collections.list(filteredRequest.getHeaderNames());
            assertThat(filteredRequestHeaders).containsExactly("x-custom-header");
        }

        @Test
        void shouldPassThroughNotTransitionalHeadersWhenGetHeadersMethodCalled() throws Exception {
            when(request.getHeaders("x-custom-header")).thenReturn(Collections.enumeration(Arrays.asList("value1", "value2")));
            ArgumentCaptor<ServletRequest> captor = captureRequest(filterChain);

            filter.doFilter(request, response, filterChain);

            HttpServletRequest filteredRequest = getFilteredRequest(captor);
            ArrayList<String> filteredHeaderValues = Collections.list(filteredRequest.getHeaders("x-custom-header"));
            assertThat(filteredHeaderValues).containsExactly("value1", "value2");
        }

        @Test
        void shouldPassThroughNotTransitionalHeadersWhenGetDateHeaderMethodCalled() throws Exception {
            when(request.getDateHeader("x-custom-header")).thenReturn(123L);
            ArgumentCaptor<ServletRequest> captor = captureRequest(filterChain);

            filter.doFilter(request, response, filterChain);

            HttpServletRequest filteredRequest = getFilteredRequest(captor);
            assertThat(filteredRequest.getDateHeader("x-custom-header")).isEqualTo(123L);
        }

        @Test
        void shouldPassThroughNotTransitionalHeadersWhenGetIntHeaderMethodCalled() throws Exception {
            when(request.getIntHeader("x-custom-header")).thenReturn(123);
            ArgumentCaptor<ServletRequest> captor = captureRequest(filterChain);

            filter.doFilter(request, response, filterChain);

            HttpServletRequest filteredRequest = getFilteredRequest(captor);
            assertThat(filteredRequest.getIntHeader("x-custom-header")).isEqualTo(123);
        }
    }

    @Nested
    @DisplayName("Tests for removing any transitional headers on requests")
    class NegativeRequestTests {

        @Nested
        @DisplayName("Set of tests for Connection header")
        class ConnectionHeaderTests {

            @Test
            void shouldRemoveTransitionalHeadersWhenGetHeaderMethodCalledCaseInsensitive() throws Exception {
                ArgumentCaptor<ServletRequest> captor = captureRequest(filterChain);

                filter.doFilter(request, response, filterChain);

                HttpServletRequest filteredRequest = getFilteredRequest(captor);
                assertThat(filteredRequest.getHeader("ConnEction")).isNull();
                verifyZeroInteractions(request);
            }

        }

    }

    private ArgumentCaptor<ServletRequest> captureRequest(FilterChain filterChain) throws Exception {
        ArgumentCaptor<ServletRequest> captor = ArgumentCaptor.forClass(ServletRequest.class);
        doNothing().when(filterChain).doFilter(captor.capture(), any(ServletResponse.class));
        return captor;
    }

    private HttpServletRequest getFilteredRequest(ArgumentCaptor<ServletRequest> captor) {
        ServletRequest servletRequest = captor.getValue();
        return (HttpServletRequest) servletRequest;
    }

}