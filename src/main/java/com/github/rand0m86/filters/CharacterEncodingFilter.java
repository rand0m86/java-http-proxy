package com.github.rand0m86.filters;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CharacterEncodingFilter extends AbstractHttpFilter {

    private static final String UTF_8 = StandardCharsets.UTF_8.displayName();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding(UTF_8);
        response.setCharacterEncoding(UTF_8);
    }
}
