package arkadisahakyan.authenticationwithspring.configuration;

import arkadisahakyan.authenticationwithspring.authentication.CustomAccessDeniedHandler;
import arkadisahakyan.authenticationwithspring.authentication.CustomAuthenticationFailureHandler;
import arkadisahakyan.authenticationwithspring.securityfilter.CsrfTokenLogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CsrfFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filter(HttpSecurity http, SecurityContextRepository securityContextRepository, CustomAccessDeniedHandler accessDeniedHandler,
                                      CustomAuthenticationFailureHandler authenticationFailureHandler, RememberMeServices rememberMeServices) throws Exception {
        return http
                .addFilterAfter(new CsrfTokenLogger(), CsrfFilter.class)
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/user").authenticated()
                        .requestMatchers("/admin").authenticated()
                        .requestMatchers(HttpMethod.POST, "/upload").authenticated()
                        .anyRequest().permitAll()
                )
                .formLogin((form) -> form.loginPage("/login").failureHandler(authenticationFailureHandler).securityContextRepository(securityContextRepository))
                .logout((logout) -> logout.logoutUrl("/logout").logoutSuccessUrl("/").invalidateHttpSession(true).deleteCookies("JSESSIONID"))
                .rememberMe((remember) -> remember.rememberMeServices(rememberMeServices))
                .securityContext(securityContext -> securityContext.securityContextRepository(securityContextRepository))
                .exceptionHandling((exception) -> exception.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")).accessDeniedHandler(accessDeniedHandler))
                .sessionManagement(session -> session.sessionFixation().migrateSession())
                .build();
    }

    @Bean
    public RememberMeServices rememberMeServices(UserDetailsService userDetailsService) {
        TokenBasedRememberMeServices.RememberMeTokenAlgorithm encodingAlgorithm = TokenBasedRememberMeServices.RememberMeTokenAlgorithm.SHA256;
        TokenBasedRememberMeServices rememberMe = new TokenBasedRememberMeServices("secretKeyForRememberMeToken", userDetailsService, encodingAlgorithm);
        rememberMe.setMatchingAlgorithm(TokenBasedRememberMeServices.RememberMeTokenAlgorithm.MD5);
        return rememberMe;
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new DelegatingSecurityContextRepository(
                new RequestAttributeSecurityContextRepository(),
                new HttpSessionSecurityContextRepository()
        );
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
