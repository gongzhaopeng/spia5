package com.atzu68.tacocloud.security;

import com.atzu68.tacocloud.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter;
import org.springframework.security.web.server.util.matcher.AndServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private UserRepository userRepository;

    @Autowired
    public SecurityConfig(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity serverHttpSecurity) {

        serverHttpSecurity.authorizeExchange()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .pathMatchers(HttpMethod.POST,
                        "/api/ingredients").permitAll()
                .pathMatchers("/design",
                        "/orders/**").hasRole("USER")
                .pathMatchers(HttpMethod.PATCH,
                        "/ingredients").permitAll()
                .anyExchange().permitAll();

        serverHttpSecurity.formLogin()
                .loginPage("/login");

        serverHttpSecurity.httpBasic();

        serverHttpSecurity.logout()
                .logoutUrl("/logout");

        serverHttpSecurity.csrf()
                .requireCsrfProtectionMatcher(new AndServerWebExchangeMatcher(
                        new PathPatternParserServerWebExchangeMatcher(
                                "/**", HttpMethod.POST),
                        new NegatedServerWebExchangeMatcher(
                                new PathPatternParserServerWebExchangeMatcher
                                        ("/h2-console/**")),
                        new NegatedServerWebExchangeMatcher(
                                new PathPatternParserServerWebExchangeMatcher
                                        ("/ingredients/**")),
                        new NegatedServerWebExchangeMatcher(
                                new PathPatternParserServerWebExchangeMatcher
                                        ("/design/**")),
                        new NegatedServerWebExchangeMatcher(
                                new PathPatternParserServerWebExchangeMatcher
                                        ("/orders/**")),
                        new NegatedServerWebExchangeMatcher(
                                new PathPatternParserServerWebExchangeMatcher
                                        ("/api/**"))
                ));

        serverHttpSecurity.headers()
                .frameOptions().mode(
                XFrameOptionsServerHttpHeadersWriter.Mode.SAMEORIGIN);

        var reactiveAuthenticationManager =
                new UserDetailsRepositoryReactiveAuthenticationManager(
                        userRepositoryUserDetailsService());
        reactiveAuthenticationManager.setPasswordEncoder(passwordEncoder());
        serverHttpSecurity.authenticationManager(reactiveAuthenticationManager);

        return serverHttpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    @Primary
    public ReactiveUserDetailsService userRepositoryUserDetailsService() {

        return username -> userRepository.findByUsername(username)
                .map(user -> (UserDetails) user);
    }
}
