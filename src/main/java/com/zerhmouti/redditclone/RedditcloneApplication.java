package com.zerhmouti.redditclone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;


@EnableAsync
@SpringBootApplication
public class RedditcloneApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedditcloneApplication.class, args);
	}


//	@Bean
//	public CorsFilter corsFilter() {
//		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		CorsConfiguration config = new CorsConfiguration();
//
//		// Allow credentials
//		config.setAllowCredentials(true);
//
//		// Define allowed origins
//		config.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:4200"));
//
//		// Define allowed headers
//		config.setAllowedHeaders(Arrays.asList(
//				"Origin", "Access-Control-Allow-Origin", "Content-Type", "Accept",
//				"Jwt-Token", "Authorization", "X-Requested-With", "Access-Control-Request-Method",
//				"Access-Control-Request-Headers"
//		));
//
//		// Define exposed headers
//		config.setExposedHeaders(Arrays.asList(
//				"Origin", "Content-Type", "Accept", "Jwt-Token", "Authorization",
//				"Access-Control-Allow-Origin", "Access-Control-Allow-Credentials", "Filename"
//		));
//
//		// Define allowed methods
//		config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
//
//		// Register CORS configuration for all paths
//		source.registerCorsConfiguration("/**", config);
//
//		// Return CorsFilter instance with the configured CorsConfiguration
//		return new CorsFilter(source);
//	}
@Bean
CorsConfigurationSource corsConfigurationSource() {
	CorsConfiguration configuration = new CorsConfiguration();
	configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200")); // Allowed origins
	configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE")); // Allowed methods
	configuration.setAllowedHeaders(Arrays.asList("*")); // Allowed headers (use specific headers for better security)
	configuration.setAllowCredentials(true); // Allow credentials (cookies, etc.)

	UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	source.registerCorsConfiguration("/**", configuration); // Apply to all endpoints
	return source;
}
}
