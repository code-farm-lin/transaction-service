package com.bank.customerservice.security;

import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfiguration {//extends WebSecurityConfigurerAdapter {
    private static final String[] AUTH_WHITELIST = {

            // -- swagger ui
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/webjars/**"};

//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers("v1/customers").authenticated()
//                .and()
//                .exceptionHandling()
//                .and()
//                .authorizeRequests()
//                .antMatchers(AUTH_WHITELIST).permitAll()
//                .and()
//                .requiresChannel()
//                .anyRequest().requiresSecure();
//
//
//    }

}
