package com.mobygo.carrental.config;

import com.mobygo.carrental.security.JwtAuthenticationFilter;
import com.mobygo.carrental.security.JwtService;
import com.mobygo.carrental.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    @Lazy
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
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
                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/cars/**", "/api/locations/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/rentals").permitAll()
                // Swagger UI & OpenAPI docs
                .requestMatchers(
                    "/swagger-ui/**", "/swagger-ui.html",
                    "/v3/api-docs/**", "/v3/api-docs"
                ).permitAll()
                // H2 console (dev only)
                .requestMatchers("/h2-console/**").permitAll()
                // Any signed-in user can read their own profile (must precede the admin /api/users rules)
                .requestMatchers(HttpMethod.GET, "/api/users/me").authenticated()
                // Admin-only: managing the fleet, locations, users and viewing all rentals
                .requestMatchers(HttpMethod.POST,   "/api/cars/**", "/api/locations/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,    "/api/cars/**", "/api/locations/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/cars/**", "/api/locations/**", "/api/users/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET,  "/api/users").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/users").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET,  "/api/rentals").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET,  "/api/rentals/user/**").authenticated()
                // Everything else requires Basic Auth
                .anyRequest().authenticated()
            )
            .httpBasic(basic -> {})
            .addFilterBefore(new JwtAuthenticationFilter(jwtService), UsernamePasswordAuthenticationFilter.class)
            .headers(headers -> headers.frameOptions(frame -> frame.disable()));
        return http.build();
    }
}
