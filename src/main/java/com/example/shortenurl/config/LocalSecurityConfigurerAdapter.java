package com.example.shortenurl.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@Profile({"uat-dev", "local"})
@Order(102)
public class LocalSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
    @Override
    public void configure(final WebSecurity web) throws Exception {
        // web.ignoring().antMatchers("/**");  // This will ignore security for all requests.
        web.ignoring().antMatchers("/v2/api-docs", "/configuration/**", "/swagger-resources/**",  "/swagger-ui.html", "/webjars/**", "/api-docs/**");
    }
}
