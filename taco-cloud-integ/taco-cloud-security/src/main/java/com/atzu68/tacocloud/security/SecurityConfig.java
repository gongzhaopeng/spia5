package com.atzu68.tacocloud.security;

import com.atzu68.tacocloud.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private UserRepository userRepository;

    @Autowired
    public SecurityConfig(UserRepository userRepository) {

        super();

        this.userRepository = userRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                // needed for Angular/CORS
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .antMatchers(HttpMethod.POST,
                        "/api/ingredients").permitAll()
                .antMatchers("/design",
                        "/orders/**").access("hasRole('USER')")
                .antMatchers(HttpMethod.PATCH,
                        "/ingredients").permitAll()
                .antMatchers("/**").access("permitAll");

        http.formLogin()
                .loginPage("/login");

        http.httpBasic()
                .realmName("Taco Cloud");

        http.logout()
                .logoutSuccessUrl("/");

        // Make H2-Console non-secured; for debug purposes
        http.csrf()
                .ignoringAntMatchers(
                        "/h2-console/**",
                        "/ingredients/**",
                        "/design/**",
                        "/orders/**",
                        "/api/**");

        // Allow pages to be loaded in frames from the same origin; needed for H2-Console
        http.headers()
                .frameOptions().sameOrigin();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {

        auth.userDetailsService(userRepositoryUserDetailsService())
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    @Primary
    public UserDetailsService userRepositoryUserDetailsService() {

        return username ->
                Optional.ofNullable(userRepository.findByUsername(username))
                        .orElseThrow(() -> new UsernameNotFoundException(
                                String.format("User '%s' not found", username)
                        ));
    }
}
