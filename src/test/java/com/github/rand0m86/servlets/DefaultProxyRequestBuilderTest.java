package com.github.rand0m86.servlets;

import io.netty.handler.codec.http.HttpHeaders;
import org.asynchttpclient.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultProxyRequestBuilderTest {

    private StringBuffer requestUrl = new StringBuffer("http://example.com");

    private HttpServletRequest request;
    private ProxyRequestBuilder requestBuilder;
    private BufferedReader reader;

    @BeforeEach
    void setUp() throws Exception {
        request = mock(HttpServletRequest.class);
        requestBuilder = new DefaultProxyRequestBuilder();
    }

    @Test
    void shouldPopulateSimpleUrl() throws Exception {
        reader = createEmptyReader();
        when(request.getReader()).thenReturn(reader);
        when(request.getRequestURL()).thenReturn(requestUrl);

        Request request = requestBuilder.prepareRequest(this.request);

        assertThat(request.getUrl()).isEqualTo("http://example.com");
    }

    @Test
    void shouldPopulateUrlWithQueryParams() throws Exception {
        reader = createEmptyReader();
        when(request.getReader()).thenReturn(reader);
        when(request.getRequestURL()).thenReturn(requestUrl);
        when(request.getQueryString()).thenReturn("jane=a&jack=b");

        Request request = requestBuilder.prepareRequest(this.request);

        assertThat(request.getUrl()).isEqualTo("http://example.com?jane=a&jack=b");
    }

    @Test
    void shouldPopulateHeadersWithOneValue() throws Exception {
        reader = createEmptyReader();
        when(request.getReader()).thenReturn(reader);
        when(request.getRequestURL()).thenReturn(requestUrl);
        when(request.getHeaderNames()).thenReturn(fromValues("Host"));
        when(request.getHeaders("Host")).thenReturn(fromValues("localhost"));

        Request request = requestBuilder.prepareRequest(this.request);

        HttpHeaders actualHeaders = request.getHeaders();
        assertThat(actualHeaders.getAll("Host")).containsExactlyInAnyOrder("localhost");
    }

    @Test
    void shouldPopulateHeadersWithMultipleValues() throws Exception {
        reader = createEmptyReader();
        when(request.getReader()).thenReturn(reader);
        when(request.getHeaderNames()).thenReturn(fromValues("x-multiple-header"));
        when(request.getRequestURL()).thenReturn(requestUrl);
        when(request.getHeaders("x-multiple-header")).thenReturn(fromValues("value1", "value2"));

        Request request = requestBuilder.prepareRequest(this.request);

        HttpHeaders actualHeaders = request.getHeaders();
        assertThat(actualHeaders.getAll("x-multiple-header")).containsExactlyInAnyOrder("value1", "value2");
    }

    @Test
    void shouldNotPopulateBodyIfItIsNotPresent() throws Exception {
        reader = createEmptyReader();
        when(request.getReader()).thenReturn(reader);
        when(request.getRequestURL()).thenReturn(requestUrl);

        Request request = requestBuilder.prepareRequest(this.request);

        assertThat(request.getStringData()).isEmpty();
    }

    @Test
    void shouldPopulateBodyIfPresent() throws Exception {
        reader = createReaderWithLines("line1", "line2");
        when(request.getReader()).thenReturn(reader);
        when(request.getRequestURL()).thenReturn(requestUrl);

        Request request = requestBuilder.prepareRequest(this.request);

        assertThat(request.getStringData()).isEqualTo("line1line2");
    }

    @SafeVarargs
    private final <T> Enumeration<T> fromValues(T... values) {
        return Collections.enumeration(Arrays.asList(values));
    }

    private BufferedReader createEmptyReader() {
        BufferedReader reader = mock(BufferedReader.class);
        when(reader.lines()).thenReturn(Stream.empty());
        return reader;
    }

    private BufferedReader createReaderWithLines(String... lines) {
        BufferedReader reader = mock(BufferedReader.class);
        when(reader.lines()).thenReturn(Arrays.stream(lines));
        return reader;
    }

}