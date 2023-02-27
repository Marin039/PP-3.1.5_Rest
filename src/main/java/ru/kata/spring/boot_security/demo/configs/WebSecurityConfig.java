package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ru.kata.spring.boot_security.demo.service.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final SuccessUserHandler successUserHandler;
    private final UserDetailsService userDetailsService;

    @Autowired
    public WebSecurityConfig(SuccessUserHandler successUserHandler, @Lazy UserDetailsService userDetailsService) {
        this.successUserHandler = successUserHandler;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
            http.formLogin().loginPage("/login")
                    .successHandler(successUserHandler)
                    .loginProcessingUrl("/login")
                    .usernameParameter("j_login")
                    .passwordParameter("j_password")
                    .permitAll();

            http.logout()
                    .permitAll()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/login?logout")
                    .and().cors().and().csrf()
                    .disable();
            http
                    .authorizeRequests()
                    .antMatchers("/login").anonymous()
                    .antMatchers("/admin/**")
                    .access("hasAnyAuthority('ROLE_ADMIN')")
                    .antMatchers("/user/**")
                    .access("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
                    .anyRequest().authenticated();
        }
//        http
//                .csrf()
//                .disable()
//                .authorizeRequests()
//                .antMatchers("/login").not().fullyAuthenticated()
//                .antMatchers("/admin/**").hasRole("ADMIN")
//                .antMatchers("/user/**").hasAnyRole("ADMIN", "USER")
//                .antMatchers("/").permitAll()
//                .antMatchers("/registration").permitAll()
//                .antMatchers("/login").permitAll()
//                //остальные страницы требуют аутентификации
//                .anyRequest().authenticated()
//                .and()
//                //настройка для входа в систему
//                .formLogin().successHandler(successUserHandler)
//
//                .permitAll()
//                .and()
//                .logout()
//                .permitAll()
//                .logoutSuccessUrl("/login")
//                .and().csrf().disable();
//    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    // аутентификация inMemory
//    @Bean
//    @Override
//    public UserDetailsService userDetailsService() {
//        UserDetails user =
//                User.withDefaultPasswordEncoder()
//                        .username("user")
//                        .password("user")
//                        .roles("USER")
//                        .build();
//
//        return new InMemoryUserDetailsManager(user);
//    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }
}