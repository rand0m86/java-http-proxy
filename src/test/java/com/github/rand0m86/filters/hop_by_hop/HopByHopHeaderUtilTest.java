package com.github.rand0m86.filters.hop_by_hop;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HopByHopHeaderUtilTest {

    @Test
    void shouldIndicateIfHeaderIsHopByHopOneLowerCase() {
        assertThat(HopByHopHeaderUtil.isHopByHopHeader("connection")).isTrue();
    }

    @Test
    void shouldIndicateIfHeaderIsHopByHopOneUpperCase() {
        assertThat(HopByHopHeaderUtil.isHopByHopHeader("CONNECTION")).isTrue();
    }

    @Test
    void shouldIndicateIfHeaderIsHopByHopOneIgnoreCase() {
        assertThat(HopByHopHeaderUtil.isHopByHopHeader("CoNnEcTiOn")).isTrue();
    }

    @Test
    void shouldNotMarkNotTransitionalHeadersAsHopByHopOnes() {
        assertThat(HopByHopHeaderUtil.isHopByHopHeader("X-Dummy-Header")).isFalse();
    }

    @Test
    void shouldReturnFalseOnNullInput() {
        assertThat(HopByHopHeaderUtil.isHopByHopHeader(null)).isFalse();
    }

    @Test
    void shouldReturnFalseOnEmptyInput() {
        assertThat(HopByHopHeaderUtil.isHopByHopHeader("")).isFalse();
    }

    @Test
    void shouldReturnAllKnownTransitionalHeaders() {
        assertThat(HopByHopHeaderUtil.HOP_BY_HOP_HEADERS).containsExactlyInAnyOrder(
                "connection",
                "keep-alive",
                "proxy-authenticate",
                "proxy-authorization",
                "te",
                "trailer",
                "transfer-encoding",
                "upgrade"
        );
    }
}
