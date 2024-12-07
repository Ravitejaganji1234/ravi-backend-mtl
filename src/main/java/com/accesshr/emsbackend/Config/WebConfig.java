package com.accesshr.emsbackend.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/api/**") // Adjust the mapping as needed
//                .allowedOrigins("http://localhost:3000") // Your frontend URL
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowed methods
//                .allowedHeaders("*") // Allow all headers
//                .allowCredentials(true); // Allow credentials (cookies, authorization headers, etc.)
//    }
//}

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Allow requests from your frontend's domain (e.g., localhost during development)
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")  // Change to your frontend URL in production
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("Authorization", "Content-Type")
                .allowCredentials(true);  // Allow cookies and headers for Authorization
    }
}
