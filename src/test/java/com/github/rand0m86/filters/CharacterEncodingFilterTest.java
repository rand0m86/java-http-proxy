package com.github.rand0m86.filters;

import org.junit.jupiter.api.Test;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class CharacterEncodingFilterTest {

    private HttpServletRequest request = mock(HttpServletRequest.class);
    private HttpServletResponse response = mock(HttpServletResponse.class);
    private FilterChain filterChain = mock(FilterChain.class);

    private CharacterEncodingFilter filter = new CharacterEncodingFilter();

    @Test
    void expectToHaveEncodingSetToUtf8OnRequestAndResponseByDefault() throws Exception {
        filter.doFilter(request, response, filterChain);

        verify(request).setCharacterEncoding("UTF-8");
        verify(response).setCharacterEncoding("UTF-8");
        verify(filterChain).doFilter(request, response);
    }
}
