package com.example.shortenurl.common;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class AppProperties {

    @Value("${short.url.domain}")
    private String shortUrlDomain;
}
