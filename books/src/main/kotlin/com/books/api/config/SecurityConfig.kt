package com.books.api.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Autowired
    lateinit var jwtAuthFilter: JwtAuthFilter

    @Autowired
    lateinit var userService: UserDetailsService

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain = http
        .csrf { it.disable() }
        .authorizeHttpRequests()
        .requestMatchers("/authenticate")
        .permitAll()
        .requestMatchers(HttpMethod.GET, "/books/**")
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
        .build()

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return NoOpPasswordEncoder.getInstance()
        //return BCryptPasswordEncoder()
    }

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager = config.authenticationManager

    @Bean
    fun authenticationProvider(): AuthenticationProvider = DaoAuthenticationProvider().apply {
        setUserDetailsService(userService)
        setPasswordEncoder(passwordEncoder())
    }


}