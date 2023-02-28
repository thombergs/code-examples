package com.reflectoring.security.config;

import com.reflectoring.security.CustomHeaderValidatorFilter;
import com.reflectoring.security.exception.UserAuthenticationErrorHandler;
import com.reflectoring.security.exception.UserForbiddenErrorHandler;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties(BasicAuthProperties.class)
public class SecurityConfiguration {

    private final BasicAuthProperties props;

    public SecurityConfiguration(BasicAuthProperties props) {
        this.props = props;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain bookFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .antMatcher("/library/**")
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/library/**").hasRole("USER").anyRequest().authenticated()
                .and()
                .httpBasic()
                .and()
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(userAuthenticationErrorHandler())
                        .accessDeniedHandler(new UserForbiddenErrorHandler()));

        http.addFilterBefore(customHeaderValidatorFilter(), BasicAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CustomHeaderValidatorFilter customHeaderValidatorFilter() {
        return new CustomHeaderValidatorFilter();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/library/info");
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(props.getUserDetails());
    }

    /*@Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {

        var builder = http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(new InMemoryUserDetailsManager(props.getUserDetails()));
        return builder.and().build();
    }*/

    @Bean
    public AuthenticationEntryPoint userAuthenticationErrorHandler() {
        UserAuthenticationErrorHandler userAuthenticationErrorHandler =
                new UserAuthenticationErrorHandler();
        userAuthenticationErrorHandler.setRealmName("Basic Authentication");
        return userAuthenticationErrorHandler;
    }

    /*@Bean
    public UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter(HttpSecurity http) throws Exception {

        //AuthenticationEntryPoint authenticationEntryPoint = new UserAuthenticationErrorHandler();
        return new UsernamePasswordAuthenticationFilter(authenticationManager(http));
    }*/

    public static final String[] ENDPOINTS_WHITELIST = {
            "/css/**",
            "/login",
            "/home"
    };
    public static final String LOGIN_URL = "/login";
    public static final String LOGIN_FAIL_URL = LOGIN_URL + "?error";
    public static final String DEFAULT_SUCCESS_URL = "/home";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    @Bean
    @Order(1)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Requests
        http.authorizeRequests(request -> request.antMatchers(ENDPOINTS_WHITELIST).hasRole("ADMIN")
                        .anyRequest().authenticated())
                // CSRF
                .csrf().disable()
                .antMatcher("/login")
                //.formLogin(Customizer.withDefaults())
                .formLogin(form -> form
                        .loginPage(LOGIN_URL)
                        .loginProcessingUrl(LOGIN_URL)
                        .failureUrl(LOGIN_FAIL_URL)
                        .usernameParameter(USERNAME)
                        .passwordParameter(PASSWORD)
                        .defaultSuccessUrl(DEFAULT_SUCCESS_URL))
                //.logout(Customizer.withDefaults())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl(LOGIN_URL + "?logout"))
                //.sessionManagement(Customizer.withDefaults())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                        .invalidSessionUrl("/invalidSession")
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true));

        return http.build();
    }

        /*private AuthenticationFilter authenticationFilter(HttpSecurity http) {
            AuthenticationFilter filter = new AuthenticationFilter(
                    resolver(http), authenticationConverter());
            filter.setSuccessHandler((request, response, auth) -> {});
            return filter;
        }

        public AuthenticationConverter authenticationConverter() {
            return new BasicAuthenticationConverter();
        }

        public AuthenticationManagerResolver<HttpServletRequest> resolver(HttpSecurity http) {
            return request -> {
                if (request.getPathInfo().contains("login")) {
                    try {
                        return customAuthenticationManager(http);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return null;
            };
        }

        public AuthenticationManager customAuthenticationManager(HttpSecurity http)
                throws Exception {
            return http.getSharedObject(AuthenticationManagerBuilder.class)
                    .userDetailsService(userDetailsService())
                    .passwordEncoder(passwordEncoder())
                    .and()
                    .build();
        }

        public InMemoryUserDetailsManager userDetailsService() {
            UserDetails admin = User.withUsername("user")
                    .password(passwordEncoder().encode("userpass"))
                    .roles("USER")
                    .build();
            return new InMemoryUserDetailsManager(admin);
        }

        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }*/


}

