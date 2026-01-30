package com.store.technology.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dcsrll36k",  // thay bằng cloud_name của bạn
                "api_key", "189694735618361",        // thay bằng api_key của bạn
                "api_secret", "ivm9yUvdK2NB2m0vZl8UX9YYYt4",  // thay bằng api_secret của bạn
                "secure", true
        ));
    }
}
