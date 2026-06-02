package com.mobygo.car_rental.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults()) // Aktiviert CORS
                .csrf(csrf -> csrf.disable()) // Deaktiviert CSRF für die API
                .authorizeHttpRequests(authz -> authz
                        // Öffentlich zugängliche Routen
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/api/cars").permitAll()
                        // Erlaube Anfragen zur Authentifizierung (Login/Register)
                        .requestMatchers("/api/auth/**").permitAll()

                        // Alles andere muss authentifiziert sein
                        .anyRequest().authenticated()
                )
                // H2 Console Frames erlauben
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))

                // Standard Basic Authentication nutzen (wird später durch JWT ersetzt)
                .httpBasic(withDefaults());

        return http.build();
    }

    // CORS Konfiguration: Wer darf auf das Backend zugreifen?
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // HIER IST DER NEUE INTELLIJ-PORT EINGEFÜGT:
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:63342",
                "http://localhost:5500",
                "http://127.0.0.1:5500",
                "http://localhost:3000"
        ));

        // Erlaube alle wichtigen HTTP-Methoden
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Erlaube alle Header
        configuration.setAllowedHeaders(Arrays.asList("*"));
        // Wichtig für Authentifizierung (später für Cookies/Tokens)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Wende CORS auf alle Routen an
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}