package com.sample.app;

import com.sample.core.Engine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public Engine person() {
        return new Engine();
    }
}
