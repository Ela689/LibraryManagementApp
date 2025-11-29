//WebConfig.java
package com.example.librarymanagementapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // Pentru uploads (pozele sau ce mai ai acolo)
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");

        // ✅ Pentru PDF-uri din static/books/
        registry.addResourceHandler("/books/**")
                .addResourceLocations("classpath:/static/books/");
    }
}
