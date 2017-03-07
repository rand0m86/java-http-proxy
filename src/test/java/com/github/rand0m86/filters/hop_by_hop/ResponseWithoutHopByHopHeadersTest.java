package com.github.rand0m86.filters.hop_by_hop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ResponseWithoutHopByHopHeadersTest {

    private HttpServletResponse inbound;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        inbound = mock(HttpServletResponse.class);
        response = new ResponseWithoutHopByHopHeaders(inbound);
    }

    @Nested
    @DisplayName("Tests for not modifying any non transitional headers on responses")
    class PositiveResponseTests {

        @Test
        void shouldPassThroughNotTransitionalHeadersWhenGetHeaderMethodCalled() throws Exception {
            when(inbound.getHeader("x-custom-header")).thenReturn("some value");

            String header = response.getHeader("x-custom-header");

            assertThat(header).isEqualTo("some value");
        }

        @Test
        void shouldPassThroughNotTransitionalHeadersWhenGetHeaderNamesMethodCalled() throws Exception {
            when(inbound.getHeaderNames()).thenReturn(Collections.singletonList("x-custom-header"));

            assertThat(response.getHeaderNames()).containsExactly("x-custom-header");
        }

        @Test
        void shouldPassThroughNotTransitionalHeadersWhenGetHeadersMethodCalled() throws Exception {
            when(inbound.getHeaders("x-custom-header")).thenReturn(Arrays.asList("value1", "value2"));

            Collection<String> actual = response.getHeaders("x-custom-header");

            assertThat(actual).containsExactly("value1", "value2");
        }
    }

    @Nested
    @DisplayName("Tests for not removing any transitional headers on responses")
    class NegativeResponseTests {

        @Test
        void shouldRemoveTransitionalHeaderWhenGetHeaderMethodCalledCaseInsensitive() throws Exception {
            when(inbound.getHeader("ConnectioN")).thenReturn("some value");

            String header = response.getHeader("ConnectioN");

            assertThat(header).isNull();
        }

        @Test
        void shouldRemoveTransitionalHeaderWhenGetHeaderNamesMethodCalledCaseInsensitive() throws Exception {
            when(inbound.getHeaderNames()).thenReturn(Collections.singletonList("ConnectioN"));

            assertThat(response.getHeaderNames()).isEmpty();
        }

        @Test
        void shouldRemoveTransitionalHeaderWhenGetHeadersMethodCalledCaseInsensitive() throws Exception {
            when(inbound.getHeaders("ConnectioN")).thenReturn(Arrays.asList("value1", "value2"));

            Collection<String> actual = response.getHeaders("ConnectioN");

            assertThat(actual).isEmpty();
        }
    }

    @Nested
    @DisplayName("Tests for removing transitional headers and leaving all others")
    class MixedTests {

        @Test
        void shouldLeaveNonTransitionalHeadersAfterPassingThroughFilter() {
            when(inbound.getHeaderNames()).thenReturn(Arrays.asList(
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

            Collection<String> headers = response.getHeaderNames();
            assertThat(headers).containsExactlyInAnyOrder("Host", "X-Custom-Header");
        }
    }
}