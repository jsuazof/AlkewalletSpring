package com.alkewallet6.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig{

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests((request) -> request
                        .requestMatchers("/","/register", "/registerUser", "/css/**", "/js/**").permitAll() // Permitir el acceso a la página de inicio y recursos estáticos sin autenticación
                        .requestMatchers("/home", "/deposit", "/withdraw", "/transfer", "/transactions").authenticated() // Requerir autenticación para estas rutas
                        .anyRequest().permitAll())
                .formLogin((form) -> form
                        .loginPage("/").permitAll() // Usar la página de inicio (index.html) para el login
                        .loginProcessingUrl("/login") // URL a la que el formulario de inicio de sesión envía datos
                        .defaultSuccessUrl("/access", true) // Redirigir a /access y después /home después del login exitoso
                        .failureUrl("/?error=true") // Redirigir a la página de inicio con un parámetro de error si el login falla
                        .usernameParameter("usernameLogin") // Nombre del parámetro de usuario en el formulario
                        .passwordParameter("passwordLogin")) // Nombre del parámetro de contraseña en el formulario
                .logout((logout) -> logout
                        .logoutUrl("/logout") // URL para cerrar sesión
                        .logoutSuccessUrl("/").permitAll()); // Redirigir a la página de inicio después de cerrar sesión

        return httpSecurity.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
