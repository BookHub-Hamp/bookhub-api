package com.hampcode.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JWTFilter extends GenericFilterBean {

    private final TokenProvider tokenProvider;

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        // Extraer el token JWT de la cabecera de autorización
        String bearerToken = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        // Verificar que el token no esté vacío y comience con "Bearer "
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);

            // Obtener la autenticación a partir del token
            Authentication authentication = tokenProvider.getAuthentication(token);

            // Establecer la autenticación en el contexto de seguridad
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // Continuar con el siguiente filtro en la cadena
        chain.doFilter(request, response);
    }
}
