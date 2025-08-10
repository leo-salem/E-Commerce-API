package API.com.example.E_COMMERCY.security;

import API.com.example.E_COMMERCY.model.User;
import API.com.example.E_COMMERCY.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthTokenFilter authTokenFilter;
    private final AuthEntryPointJwt unauthorizedHandler;
    private final UserRepository userRepository;

    @Autowired
    public SecurityConfig(AuthTokenFilter authTokenFilter, AuthEntryPointJwt unauthorizedEntryPoint, UserRepository userRepository) {
        this.authTokenFilter = authTokenFilter;
        this.unauthorizedHandler = unauthorizedEntryPoint;
        this.userRepository = userRepository;
    }

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "select username, password, true as enabled from users where username=?");
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "select username, has_role as authority from users where username=?"
                );
        return jdbcUserDetailsManager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                .requestMatchers("/api/Authentication/signup", "/api/Authentication/signin", "/api/Authentication/refresh").permitAll()

                .requestMatchers(HttpMethod.POST, "/api/CategorySection/category/Add").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/CategorySection/category/update/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/CategorySection/category/update/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/CategorySection/category/delete/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/CategorySection/**").hasAnyRole("ADMIN", "USER")

                .requestMatchers(HttpMethod.POST, "/api/ProductSection/product/Add").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/ProductSection/product/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/ProductSection/product/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/ProductSection/product/delete/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/ProductSection/**").hasAnyRole("ADMIN", "USER")
                .anyRequest().authenticated()
        );
        http.authenticationProvider(authenticationProvider());
        http.exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler));
        http.httpBasic(withDefaults());

        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                User user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
                return org.springframework.security.core.userdetails.User.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .roles(user.getHasRole().name())
                        .build();
            }
        };
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
