package com.example.pixels.config;

import com.example.pixels.entity.User;
import com.example.pixels.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    UserServiceImpl userService;

    private static final String[] WHITE_LIST_URLS = {
            "/register",
            "/verifyRegistration*",
            "/resendVerifyToken*",
            "/resetPassword*",
            "/savePassword",
            "/movies/**",
            "/review/**",
            "/comment/**",
            "/allCritic*"
    };

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .clearAuthentication(true)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user/**").hasRole("USER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers(WHITE_LIST_URLS).permitAll()
                        .anyRequest().authenticated())
                .formLogin(formLogin -> formLogin
//                        .loginPage("/login")  // Specify the login page
                        .defaultSuccessUrl("/movies/all", true)  // Redirect to home on login success
                        .permitAll())
//                .formLogin(Customizer.withDefaults());
//                .formLogin(formLogin -> formLogin
//                        .loginPage("/login")
//                        .loginProcessingUrl("/perform_login")
//                        .defaultSuccessUrl("/default", true)
//                        .failureUrl("/login?error=true")
//                        .successHandler(new CustomLoginSuccessHandler())  // Set the custom success handler
//                        .permitAll());
                .formLogin(AbstractAuthenticationFilterConfigurer::permitAll);

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return new OurUserInfoDetailsService();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

//    public void clearCurrentSecurityContext() {
//        SecurityContextHolder.clearContext();
//    }

//    public void reAuthenticateUser(OurUserInfoDetailsService user) {
//        List<SimpleGrantedAuthority> updatedAuthorities = user.
//
//                getUserRole().stream()
//                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.g()))
//                .collect(Collectors.toList());
//
//        UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(
//                user.getUsername(), user.getPassword(), updatedAuthorities);
//
//        SecurityContextHolder.getContext().setAuthentication(newAuth);
//    }

}
