package com.example.librarymanagementapp.config;

import com.example.librarymanagementapp.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // ✅ Acces liber doar la login & register
                        .requestMatchers("/login", "/register", "/css/**", "/uploads/**").permitAll()

                        // ✅ Doar ADMIN poate accesa aceste zone
                        .requestMatchers("/admin/**", "/home", "/books").hasAuthority("ADMIN")

                        // ✅ Userii obișnuiți pot accesa doar zonele permise
                        .requestMatchers("/user_wait", "/profile/**").hasAuthority("USER")

                        // Orice altă cerere — se cere autentificare
                        .anyRequest().authenticated()
                )
                // ✅ Configurăm login-ul cu redirecționare dinamică
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler((request, response, authentication) -> {
                            var authorities = authentication.getAuthorities();
                            String redirectUrl = "/home"; // default admin
                            for (var authority : authorities) {
                                if (authority.getAuthority().equals("USER")) {
                                    redirectUrl = "/user_wait";
                                    break;
                                }
                            }
                            response.sendRedirect(redirectUrl);
                        })
                        .permitAll()
                )
                // ✅ Logout
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                // ✅ Pagina de eroare 403
                .exceptionHandling(ex -> ex.accessDeniedPage("/error_403"));

        return http.build();
    }

}
