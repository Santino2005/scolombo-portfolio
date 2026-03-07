package com.baby_io.baby_io_app.configuration;

import com.baby_io.baby_io_app.component.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Autowired
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    String[] allowedOrigins = {
        "http://localhost:3000",
        "http://127.0.0.1:3000",
        "http://13.219.34.10:3000",
        "http://13.219.34.10",
        "http://52.202.248.7:8080",
        "http://52.202.248.7"
    };

    registry.addMapping("/**")
        .allowedOrigins(allowedOrigins)
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
        .allowedHeaders("*")
        .allowCredentials(false)
        .exposedHeaders("Authorization")
        .maxAge(3600);
  }

  @Bean
  public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilter() {
    FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(jwtAuthenticationFilter);
    registrationBean.addUrlPatterns("/api/*");
    registrationBean.setOrder(1);
    return registrationBean;
  }
}

