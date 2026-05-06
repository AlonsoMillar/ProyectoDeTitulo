package com.example.InicioSesion.Config; // <--- REVISA QUE SEA TU PAQUETE REAL

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // ESTE ES EL ÚNICO MÉTODO filterChain QUE DEBE EXISTIR
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults()) // Busca el bean corsConfigurationSource de abajo
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/usuarios/**").permitAll()
                .requestMatchers("/error").permitAll() // Vital para evitar el 403 cuando algo falla
                .anyRequest().authenticated()
            );
        
        return http.build();
    }

@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    
    // Aquí listamos todos los posibles "disfraces" que usa tu app de Ionic
    configuration.setAllowedOrigins(Arrays.asList(
        "http://localhost",       // Cómo se ve la app en Android
        "http://localhost:8100",  // Cómo se ve la app en el PC (ionic serve)
        "https://localhost",      // Cómo se ve en iOS
        "capacitor://localhost"   // El protocolo nativo de Capacitor
    ));
    
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
    configuration.setAllowCredentials(true); // Necesario si envías cookies o auth headers

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
}
}