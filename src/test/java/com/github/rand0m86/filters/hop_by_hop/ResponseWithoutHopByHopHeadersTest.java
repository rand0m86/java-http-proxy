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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
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

        @Test
        void shouldPassThroughNotSetTransitionalHeaderCaseInsensitive() throws Exception {
            response.setHeader("x-CusTom-header", "dummy");

            verify(inbound).setHeader("x-CusTom-header", "dummy");
        }

        @Test
        void shouldPassThroughNotAddTransitionalHeaderCaseInsensitive() throws Exception {
            response.addHeader("x-CusTom-header", "dummy");

            verify(inbound).addHeader("x-CusTom-header", "dummy");
        }

        @Test
        void shouldPassThroughNotAddTransitionalIntHeaderCaseInsensitive() throws Exception {
            response.addIntHeader("x-CusTom-header", 123);

            verify(inbound).addIntHeader("x-CusTom-header", 123);
        }

        @Test
        void shouldPassThroughNotSetTransitionalIntHeaderCaseInsensitive() throws Exception {
            response.setIntHeader("x-CusTom-header", 123);

            verify(inbound).setIntHeader("x-CusTom-header", 123);
        }

        @Test
        void shouldPassThroughNotAddTransitionalDateHeaderCaseInsensitive() throws Exception {
            response.addDateHeader("x-CusTom-header", 123L);

            verify(inbound).addDateHeader("x-CusTom-header", 123L);
        }

        @Test
        void shouldPassThroughNotSetTransitionalDateHeaderCaseInsensitive() throws Exception {
            response.setDateHeader("x-CusTom-header", 123L);

            verify(inbound).setDateHeader("x-CusTom-header", 123L);
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

        @Test
        void shouldNotSetTransitionalHeaderCaseInsensitive() throws Exception {
            response.setHeader("proXy-aUthorization", "dummy");

            verifyZeroInteractions(inbound);
        }

        @Test
        void shouldNotAddTransitionalHeaderCaseInsensitive() throws Exception {
            response.addHeader("proXy-auThorization", "dummy");

            verifyZeroInteractions(inbound);
        }

        @Test
        void shouldNotAddTransitionalIntHeaderCaseInsensitive() throws Exception {
            response.addIntHeader("proXy-auThorization", 123);

            verifyZeroInteractions(inbound);
        }

        @Test
        void shouldNotSetTransitionalIntHeaderCaseInsensitive() throws Exception {
            response.setIntHeader("proXy-auThorization", 123);

            verifyZeroInteractions(inbound);
        }

        @Test
        void shouldNotAddTransitionalDateHeaderCaseInsensitive() throws Exception {
            response.addDateHeader("proXy-auThorization", 123L);

            verifyZeroInteractions(inbound);
        }

        @Test
        void shouldNotSetTransitionalDateHeaderCaseInsensitive() throws Exception {
            response.setDateHeader("proXy-auThorization", 123L);

            verifyZeroInteractions(inbound);
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