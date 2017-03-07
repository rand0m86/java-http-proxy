package com.github.rand0m86.filters.hop_by_hop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RequestWithoutHopByHopHeadersTest {

    private HttpServletRequest inbound;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        inbound = mock(HttpServletRequest.class);
        request = new RequestWithoutHopByHopHeaders(inbound);
    }

    @Nested
    @DisplayName("Tests for not modifying any non transitional headers on requests")
    class PositiveRequestTests {

        @Test
        void shouldPassThroughNotTransitionalHeadersWhenGetHeaderMethodCalled() throws Exception {
            when(inbound.getHeader("x-custom-header")).thenReturn("some value");

            String header = request.getHeader("x-custom-header");

            assertThat(header).isEqualTo("some value");
        }

        @Test
        void shouldPassThroughNotTransitionalHeadersWhenGetHeaderNamesMethodCalled() throws Exception {
            when(inbound.getHeaderNames()).thenReturn(toEnumeration("x-custom-header"));

            List<String> actual = toList(request.getHeaderNames());

            assertThat(actual).containsExactly("x-custom-header");
        }

        @Test
        void shouldPassThroughNotTransitionalHeadersWhenGetHeadersMethodCalled() throws Exception {
            when(inbound.getHeaders("x-custom-header")).thenReturn(toEnumeration("value1", "value2"));

            List<String> actual = toList(request.getHeaders("x-custom-header"));

            assertThat(actual).containsExactly("value1", "value2");
        }

        @Test
        void shouldPassThroughNotTransitionalHeadersWhenGetDateHeaderMethodCalled() throws Exception {
            when(inbound.getDateHeader("x-custom-header")).thenReturn(123L);

            assertThat(request.getDateHeader("x-custom-header")).isEqualTo(123L);
        }

        @Test
        void shouldPassThroughNotTransitionalHeadersWhenGetIntHeaderMethodCalled() throws Exception {
            when(inbound.getIntHeader("x-custom-header")).thenReturn(123);

            assertThat(request.getIntHeader("x-custom-header")).isEqualTo(123);
        }
    }

    @Nested
    @DisplayName("Tests for removing any transitional headers on requests")
    class NegativeRequestTests {

        @Test
        void shouldRemoveConnectionHeaderWhenGetHeaderMethodCalledCaseInsensitive() throws Exception {
            when(inbound.getHeader("ConnEction")).thenReturn("some value");

            assertThat(request.getHeader("ConnEction")).isNull();
        }

        @Test
        void shouldRemoveConnectionHeaderWhenGetHeaderNamesMethodCalledCaseInsensitive() throws Exception {
            when(inbound.getHeaderNames()).thenReturn(toEnumeration("connEction"));

            List<String> headers = toList(request.getHeaderNames());
            assertThat(headers).isEmpty();
        }

        @Test
        void shouldRemoveConnectionHeaderWhenGetHeadersMethodCalledCaseInsensitive() throws Exception {
            when(inbound.getHeaders("coNnection")).thenReturn(toEnumeration("value1", "value2"));

            List<String> headers = toList(request.getHeaders("coNnection"));
            assertThat(headers).isEmpty();
        }

        @Test
        void shouldRemoveConnectionHeaderWhenGetDateHeaderMethodCalledCaseInsensitive() throws Exception {
            when(inbound.getDateHeader("coNnection")).thenReturn(123L);

            long dateAsLong = request.getDateHeader("coNnection");
            assertThat(dateAsLong).isEqualTo(-1);
        }

        @Test
        void shouldRemoveConnectionHeaderWhenGetIntHeaderMethodCalledCaseInsensitive() throws Exception {
            when(inbound.getIntHeader("coNnection")).thenReturn(123);

            int asInteger = request.getIntHeader("coNnection");
            assertThat(asInteger).isEqualTo(-1);
        }
    }

    @Nested
    @DisplayName("Tests for removing only transitional headers")
    class MixedHeadersTests {

        @Test
        void shouldLeaveNonTransitionalHeadersAfterPassingThroughFilter() {
            when(inbound.getHeaderNames()).thenReturn(toEnumeration(
                    "Connection",
                    "Keep-Alive",
                    "Proxy-Authenticate",
                    "Proxy-Authorization",
                    "Te",
                    "Trailer",
                    "Transfer-Encoding",
                    "Upgrade",
                    "Host",
                    "X-Custom-Header"));

            List<String> headers = toList(request.getHeaderNames());
            assertThat(headers).containsExactlyInAnyOrder("Host", "X-Custom-Header");
        }

        @Test
        void shouldReturnNullIfOriginalResponseReturnsNull() {
            when(inbound.getHeaderNames()).thenReturn(null);

            assertThat(request.getHeaderNames()).isNull();
        }
    }

    @SafeVarargs
    private final <T> Enumeration<T> toEnumeration(T... args) {
        return Collections.enumeration(Arrays.asList(args));
    }

    private <T> List<T> toList(Enumeration<T> enumeration) {
        return Collections.list(enumeration);
    }

}