package com.RedFish.RedFish.shared.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
public class SecurityConfiguration {
    @Bean
    PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        CookieCsrfTokenRepository csrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        csrfTokenRepository.setCookiePath("/");
        org.springframework.security.web.AuthenticationEntryPoint jsonUnauthorized = (request, response, authException) -> {
            response.setHeader("WWW-Authenticate", "");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write("{\"message\":\"Autenticación requerida\"}");
        };
        return http
            .csrf(csrf -> csrf.csrfTokenRepository(csrfTokenRepository)
                .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))
            .addFilterAfter((request, response, chain) -> {
                CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
                if (csrfToken != null) csrfTokenRepository.saveToken(csrfToken,
                        (HttpServletRequest) request, (HttpServletResponse) response);
                chain.doFilter(request, response);
            }, CsrfFilter.class)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/", "/index.html", "/favicon.ico", "/actuator/health",
                        "/openapi-v1.yaml", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                .requestMatchers("/api/v1/auth/me").hasAnyRole("ADMIN", "PRODUCTION", "OPERATIONS", "COMMERCIAL", "OPERATOR")
                .requestMatchers("/api/v1/users/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/v1/products/**")
                    .hasAnyRole("ADMIN", "PRODUCTION", "OPERATIONS", "COMMERCIAL", "OPERATOR")
                .requestMatchers(HttpMethod.GET, "/api/products/**")
                    .hasAnyRole("ADMIN", "PRODUCTION", "OPERATIONS", "COMMERCIAL", "OPERATOR")
                .requestMatchers("/api/v1/products/**")
                    .hasAnyRole("ADMIN", "PRODUCTION")
                .requestMatchers("/api/products/**")
                    .hasAnyRole("ADMIN", "PRODUCTION", "OPERATOR")
                .requestMatchers("/api/v1/ponds/**", "/api/v1/feeding-records/**")
                    .hasAnyRole("ADMIN", "PRODUCTION")
                .requestMatchers("/api/v1/inventory/**", "/api/v1/vehicles/**", "/api/v1/dispatches/**")
                    .hasAnyRole("ADMIN", "OPERATIONS")
                .requestMatchers(HttpMethod.GET, "/api/v1/orders/**")
                    .hasAnyRole("ADMIN", "COMMERCIAL", "OPERATIONS")
                .requestMatchers("/api/v1/customers/**", "/api/customers/**", "/api/v1/orders/**")
                    .hasAnyRole("ADMIN", "COMMERCIAL")
                .anyRequest().authenticated())
            // Apply the same JSON response to authentication and authorization failures.
            // An HTTP Basic challenge makes browsers display their native credential dialog.
            .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(jsonUnauthorized))
            .httpBasic(basic -> basic.authenticationEntryPoint(jsonUnauthorized))
            .build();
    }
}
