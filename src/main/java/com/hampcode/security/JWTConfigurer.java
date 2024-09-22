package com.hampcode.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// JWTConfigurer integra el filtro JWTFilter en la configuración de seguridad de Spring Security.
@RequiredArgsConstructor
public class JWTConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final TokenProvider tokenProvider;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // Crear una instancia del filtro JWTFilter
        JWTFilter jwtFilter = new JWTFilter(tokenProvider);

        // Agregar el filtro JWTFilter antes del filtro de autenticación de nombre de usuario y contraseña
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
