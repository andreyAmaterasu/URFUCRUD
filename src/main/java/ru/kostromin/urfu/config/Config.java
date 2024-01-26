package ru.kostromin.urfu.config;

import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class Config {

  @Bean
  public SecurityFilterChain configure(HttpSecurity http) throws Exception {

    http.authorizeHttpRequests(auth ->
            auth.requestMatchers("/users", "/hello").hasAnyRole("USER", "READ_ONLY")
                .requestMatchers("/register").permitAll()
                .requestMatchers("/webjars/**").permitAll()
                .anyRequest().authenticated()
    ).formLogin(form -> form
            .loginPage("/login")
            .permitAll()
            .defaultSuccessUrl("/main", true)
    ).logout(LogoutConfigurer::permitAll);

    return http.build();
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public Validator defaultValidator() {
    return new LocalValidatorFactoryBean();
  }
}
