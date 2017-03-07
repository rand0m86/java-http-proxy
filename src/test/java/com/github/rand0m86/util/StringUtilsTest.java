package com.github.rand0m86.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StringUtilsTest {

    @Test
    void shouldReturnTrueForNullString() {
        assertThat(StringUtils.isEmpty(null)).isTrue();
    }

    @Test
    void shouldReturnTrueForEmptyString() {
        assertThat(StringUtils.isEmpty("")).isTrue();
    }

    @Test
    void shouldReturnFalseForNotEmptyString() {
        assertThat(StringUtils.isEmpty("text")).isFalse();
    }

}