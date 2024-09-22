package com.hampcode.security;

import com.hampcode.exception.RoleNotFoundException;
import com.hampcode.repository.UserRepository;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Component
public class TokenProvider {
    private final UserRepository userRepository;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.validity-in-seconds}")
    private long jwtValidityInSeconds;

    private Key key;
    private JwtParser jwtParser;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
        jwtParser = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build();
    }

    // Generar tokens JWT
    public String createAccessToken(Authentication authentication) {
        // Obtener el email del usuario autenticado desde el principal
        String email = authentication.getName();

        // Buscar el usuario en la base de datos usando el email
        com.hampcode.model.entity.User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el email: " + email));

        // Obtener el rol del usuario
        String role = authentication
                .getAuthorities()
                .stream()
                .findFirst()
                .orElseThrow(RoleNotFoundException::new)
                .getAuthority();

        // Crear el token JWT con solo el rol y el sujeto
        return Jwts
                .builder()
                .setSubject(authentication.getName())  // El sujeto será el email o el nombre de usuario
                .claim("role", role)  // Solo se incluye el rol como claim
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(new Date(System.currentTimeMillis() + jwtValidityInSeconds * 1000))
                .compact();
    }

    // Obtener autenticación
    public Authentication getAuthentication(String token) {
        Claims claims = jwtParser.parseClaimsJws(token).getBody();

        String role = claims.get("role").toString();

        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role));

        // El principal será el email del usuario que viene en el subject del JWT
        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    // Validar tokens JWT
    public boolean validateToken(String token) {
        try {
            jwtParser.parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
