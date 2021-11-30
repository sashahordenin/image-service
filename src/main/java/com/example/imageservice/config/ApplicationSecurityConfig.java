package com.example.imageservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    public void configure(AuthenticationManagerBuilder authentication) throws Exception {
        authentication.inMemoryAuthentication()
                .withUser("admin@i.ua")
                .password(passwordEncoder().encode("admin123"))
                .roles("USER")
                .and()
                .withUser("user@i.ua")
                .password(passwordEncoder().encode("user1234"))
                .roles("USER");
    }

    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/register", "/login").permitAll()
                .antMatchers(HttpMethod.GET, "/images", "/images/{id}",
                        "/images/search", "/accounts", "/accounts/{id}",
                        "/tags/{id}", "/tags").authenticated()
                .antMatchers(HttpMethod.POST, "/images").authenticated()
                .antMatchers(HttpMethod.PUT, "/images/{id}", "/accounts",
                        "tags/add/by-image/{id}").authenticated()
                .antMatchers(HttpMethod.PUT, "/tags/{id}").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/images/{id}",
                        "/tags/delete/by-image/{id}").authenticated()
                .antMatchers(HttpMethod.DELETE, "/accounts/{id}",
                        "/tags/{id}").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .httpBasic()
                .and()
                .csrf().disable()
                .headers().frameOptions().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
