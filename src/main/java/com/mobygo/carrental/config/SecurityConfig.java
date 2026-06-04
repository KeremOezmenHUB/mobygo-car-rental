package com.mobygo.carrental.config;

import com.mobygo.carrental.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    @Lazy
    private UserService userService;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authenticationProvider(authenticationProvider())
            .csrf(csrf -> csrf.disable())
            .cors(cors -> {})
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Static frontend files
                .requestMatchers("/", "/*.html", "/css/**", "/js/**", "/images/**").permitAll()
                // Public API endpoints
                .requestMatchers("/api/", "/api/rates").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/cars/**", "/api/locations/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/rentals").permitAll()
                // Swagger UI & OpenAPI docs
                .requestMatchers(
                    "/swagger-ui/**", "/swagger-ui.html",
                    "/v3/api-docs/**", "/v3/api-docs"
                ).permitAll()
                // H2 console (dev only)
                .requestMatchers("/h2-console/**").permitAll()
                // Everything else requires Basic Auth
                .anyRequest().authenticated()
            )
            .httpBasic(basic -> {})
            .headers(headers -> headers.frameOptions(frame -> frame.disable()));
        return http.build();
    }
}
