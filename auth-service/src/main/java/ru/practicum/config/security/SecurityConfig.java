package ru.practicum.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(c ->
                        c
                                .requestMatchers("test/admin").hasAuthority("ROLE_catsgram.admin")
                                .requestMatchers("/test/user").hasAnyAuthority("ROLE_catsgram.admin", "ROLE_catsgram.user")
                                .requestMatchers("/users/**").permitAll()
                                .requestMatchers("/users").permitAll())
                .oauth2ResourceServer(oath2 -> oath2.jwt(jwtConfigurer ->
                        jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                )
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        var converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakAuthenticationConverter());
        return converter;
    }
}
