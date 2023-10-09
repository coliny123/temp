package com.example.demo;

import com.example.demo.file.filter.CorsFilter;
import com.example.demo.file.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public CorsFilter corsFilter(){
        return new CorsFilter();
    }
}
