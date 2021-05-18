/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package br.senac.tads.dsw.exemplospringsecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author fernando.tsuda
 */
@Configuration
@EnableGlobalMethodSecurity(
        prePostEnabled = true, 
        securedEnabled = true, 
        jsr250Enabled = true)
public class GlobalSecurityConfig extends GlobalMethodSecurityConfiguration {

    public static PasswordEncoder plainPasswordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence cs) {
                return cs.toString();
            }

            @Override
            public boolean matches(CharSequence cs, String hashSenha) {
                return hashSenha != null && hashSenha.equals(cs.toString());
            }
        };
    }

    public static PasswordEncoder bcryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return bcryptPasswordEncoder();
    }

    @Configuration
    @EnableWebSecurity
    public static class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // @formatter:off
            http.headers()
                    .cacheControl().disable() // Allow cache configuration
                    .frameOptions().sameOrigin() // Avoid x-frame-options problem opening H2 console
                .and().csrf().disable()
                .authorizeRequests()
                    .antMatchers("/css/**", "/img/**", "/js/**", "/font/**", "/", "/index.html", "/h2/**" ).permitAll()
//                    .antMatchers("/protegido/peao/**").hasRole("PEAO") // OU .hasAuthority("ROLE_PEAO")
//                    .antMatchers("/protegido/fodon/**").hasRole("FODON")
//                    .antMatchers("/protegido/god/**").hasRole("GOD")
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                        .loginPage("/login")
                        .usernameParameter("username")
                        .passwordParameter("senha")
                        .defaultSuccessUrl("/home").permitAll()
                .and()
                    .logout()
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                .and()
                    .exceptionHandling()
                        .accessDeniedPage("/erro/403");
            // @formatter:on
        }
    }



}
