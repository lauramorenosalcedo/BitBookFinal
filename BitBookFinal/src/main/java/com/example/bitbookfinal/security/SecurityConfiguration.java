package com.example.bitbookfinal.security;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Autowired
    private RepositoryUserDetailsService userService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authenticationProvider(authenticationProvider());

        http
                .authorizeHttpRequests(authorize -> authorize
                        // PUBLIC PAGES (Paginas a las que cualquier persona (sin iniciar sesion) puede acceder)
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/books").permitAll()
                        .requestMatchers("/books/{id}").permitAll()
                        .requestMatchers("/categories").permitAll()
                        .requestMatchers("/categories/{id}").permitAll()
                        .requestMatchers("/register").permitAll()
                        // PRIVATE PAGES
                        .requestMatchers("/newbook").hasAnyRole("ADMIN")
                        .requestMatchers("/newcategory").hasAnyRole("ADMIN")
                        .requestMatchers("/book/{id}/delete").hasRole("ADMIN")
                        .requestMatchers("/categories/{id}/delete").hasRole("ADMIN")
                        .requestMatchers("/categories/{id}/editform").hasRole("ADMIN")
                        .anyRequest().authenticated())

                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .failureUrl("/loginerror") //Si el usuario no existe te lleva a /loginerror
                        .defaultSuccessUrl("/") //Si el usuario exite te lleva a /
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        // Disable CSRF at the moment
        http.csrf(csrf -> csrf.disable());

        return http.build();
    }

}