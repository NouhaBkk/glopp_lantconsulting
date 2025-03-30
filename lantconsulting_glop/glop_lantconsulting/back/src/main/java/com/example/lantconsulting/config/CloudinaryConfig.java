package com.example.lantconsulting.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "lantConsulting",
            "api_key", "112181639471991",
            "api_secret", "xvhlv30-cFBxNQyAX0JpVlj-m1s"
        ));
    }
}
