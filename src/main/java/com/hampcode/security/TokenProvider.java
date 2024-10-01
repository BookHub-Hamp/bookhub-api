package com.hampcode.security;

import com.hampcode.exception.RoleNotFoundException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Component
public class TokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.validity-in-seconds}")
    private long jwtValidityInSeconds;

    private Key key;
    private JwtParser jwtParser;

    @PostConstruct
    public void init() {
        // Generar la clave para firmar el JWT a partir del secreto configurado
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));

        // Inicializar el parser JWT con la clave generada para firmar y validar tokens
        jwtParser = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build();
    }

    // TODO: Método para crear el token JWT con los detalles del usuario autenticado
    public String createAccessToken(Authentication authentication) {
        // TODO: Obtener el email o nombre del usuario autenticado
        String email = authentication.getName();

        // TODO: Obtener el rol del usuario desde el objeto de autenticación
        String role = authentication
                .getAuthorities()
                .stream()
                .findFirst()
                .orElseThrow(RoleNotFoundException::new)
                .getAuthority();

        // TODO: Construir y firmar el token JWT que incluye el rol y el email
        return Jwts
                .builder()
                .setSubject(email)  // El sujeto del token es el email o nombre de usuario
                .claim("role", role)  // El rol se incluye como claim en el token
                .signWith(key, SignatureAlgorithm.HS512)  // Firmar el token con el algoritmo HS512 y la clave
                .setExpiration(new Date(System.currentTimeMillis() + jwtValidityInSeconds * 1000))  // Configurar la fecha de expiración del token
                .compact();
    }

    // TODO: Método para obtener la autenticación a partir del token JWT
    public Authentication getAuthentication(String token) {
        // TODO: Extraer los claims (datos) del token JWT
        Claims claims = jwtParser.parseClaimsJws(token).getBody();

        // TODO: Obtener el rol del token
        String role = claims.get("role").toString();

        // TODO: Crear la lista de autoridades (roles) para el usuario
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role));

        // TODO: El principal del contexto de seguridad será el email (subject) extraído del token
        User principal = new User(claims.getSubject(), "", authorities);

        // TODO: Crear el objeto de autenticación con los detalles del usuario
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    // TODO: Método para validar el token JWT (si está correctamente firmado y no ha expirado)
    public boolean validateToken(String token) {
        try {
            // TODO: Parsear el token JWT para verificar su validez
            jwtParser.parseClaimsJws(token);
            return true;  // El token es válido
        } catch (JwtException e) {
            return false;  // El token no es válido
        }
    }
}
