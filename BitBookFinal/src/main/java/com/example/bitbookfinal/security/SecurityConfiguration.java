package com.example.bitbookfinal.security;



import com.example.bitbookfinal.security.jwt.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import com.example.bitbookfinal.security.jwt.UnauthorizedHandlerJwt;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfiguration {
    @Autowired
    private RepositoryUserDetailsService userService;
    @Autowired
    private JwtRequestFilter jwtRequestFilter;
    @Autowired
    private UnauthorizedHandlerJwt unauthorizedHandlerJwt;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {

        http.authenticationProvider(authenticationProvider());

        http
                .securityMatcher("/api/**")
                .exceptionHandling(handling -> handling.authenticationEntryPoint(unauthorizedHandlerJwt));

        http
                .authorizeHttpRequests(authorize -> authorize
                        // PRIVATE ENDPOINTS
                        .requestMatchers(HttpMethod.POST,"/api/books/newbook").hasRole("ADMIN")
                    //   .requestMatchers(HttpMethod.GET,"/api/books/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE,"/api/books/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/books/{id}/image").hasRole("USER")
                        .requestMatchers(HttpMethod.POST,"/api/books/books/{id}/addreview").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE,"/api/books/book/{id}/review/{reviewid}").hasRole("USER")
                        .requestMatchers(HttpMethod.GET,"/api/categories/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE,"/api/categories/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/categories/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/categories/newcategory").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/users/").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/users/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/users/myperfil").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT,"/api/users/myperfil/email").hasRole("USER")
                        .requestMatchers(HttpMethod.POST,"/api/books/{id}/pdf").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/books/{id}/pdf").hasRole("USER")
                        .requestMatchers(HttpMethod.GET,"/api/users/myperfil/deleteAccount").hasRole("USER")

                        // PUBLIC ENDPOINTS
                        .requestMatchers(HttpMethod.GET,"/api/books/").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/categories/").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/register").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/auth/logout").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/users/register").permitAll()
                        .anyRequest().permitAll()
                );

        // Disable Form login Authentication
        http.formLogin(formLogin -> formLogin.disable());

        // Disable CSRF protection (it is difficult to implement in REST APIs)
        http.csrf(csrf -> csrf.disable());

        // Disable Basic Authentication
        http.httpBasic(httpBasic -> httpBasic.disable());

        // Stateless session
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Add JWT Token filter
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {

        http.authenticationProvider(authenticationProvider());

        http
                .authorizeHttpRequests(authorize -> authorize
                        // PUBLIC PAGES (Paginas a las que cualquier persona (sin iniciar sesion) puede acceder)
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/books").permitAll()
                        .requestMatchers("/categories").permitAll()
                        .requestMatchers("/register").permitAll()
                        .requestMatchers("/login").permitAll()
                       // .requestMatchers("/**.css").permitAll()
                       // .requestMatchers("/img/**").permitAll()
                        // PRIVATE PAGES
                        .requestMatchers("/newbook").hasAnyRole("ADMIN")
                        .requestMatchers("/books/{bookId}/addreview").hasAnyRole("USER")
                        .requestMatchers("/newcategory").hasAnyRole("ADMIN")
                        .requestMatchers("/book/{id}/delete").hasRole("ADMIN")
                        .requestMatchers("/categories/{id}/delete").hasRole("ADMIN")
                        .requestMatchers("/categories/{id}/editform").hasRole("ADMIN")
                        .requestMatchers("/users").hasRole("ADMIN")
                        .requestMatchers("/users/{id}/delete").hasRole("ADMIN")
                        .requestMatchers("/categories/{id}").hasAnyRole("USER")
                     //   .requestMatchers("/books/{id}").hasAnyRole("USER")
                        .anyRequest().permitAll())

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

        return http.build();
    }

}