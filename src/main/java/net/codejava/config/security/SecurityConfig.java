package net.codejava.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import lombok.RequiredArgsConstructor;
import net.codejava.constant.Endpoint;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;
    private static final String CATCH_ALL_WILDCARDS = "/**";
    public static final String[] PUBLIC_ENDPOINTS = {
        "/api/v1/test" + CATCH_ALL_WILDCARDS,
        "/api-docs" + CATCH_ALL_WILDCARDS,
        "/swagger-ui" + CATCH_ALL_WILDCARDS,
        "/v3/api-docs" + CATCH_ALL_WILDCARDS,
        Endpoint.V1.Auth.BASE + CATCH_ALL_WILDCARDS,
        Endpoint.V1.Car.GET_DETAIL,
        Endpoint.V1.Car.SEARCH_CAR
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        httpSecurity.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(request -> request.requestMatchers(
                        HttpMethod.GET, PUBLIC_ENDPOINTS)
                .permitAll()
                .requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS)
                .permitAll()
                .anyRequest()
                .authenticated());
        httpSecurity.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
