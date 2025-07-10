package com.cww.veille_springboot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // ✅ Appliquer à toutes les routes
                        .allowedOrigins("http://localhost:3000", "http://vel.preventis.com.tn", "http://77.37.124.128:9090") // ✅ Origines autorisées
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // ✅ Méthodes HTTP autorisées
                        .allowedHeaders("*") // 🔥 Autoriser tous les headers
                        .allowCredentials(true) // ✅ Permettre les cookies/token d'auth
                        .maxAge(3600); // ✅ Durée de mise en cache des résultats preflight
            }
        };
    }
}
