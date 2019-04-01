package com.atzu68.spia5.tacocloud.configuration;

import com.atzu68.spia5.tacocloud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/design/**",
                        "/orders/**")
                .access("hasRole('USER')")
                .antMatchers("/**")
                .access("permitAll");

        http.formLogin()
                .loginPage("/login");

        http.logout()
                .logoutSuccessUrl("/");

        // Make H2-Console non-secured; for debug purposes
        http.csrf()
                .ignoringAntMatchers("/h2-console/**");

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
