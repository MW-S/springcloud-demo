package com.mw.springcloud.config;

import com.mw.springcloud.service.impl.LdapUserDetailsService;
import com.mw.springcloud.service.impl.MyUserDetailsService;
import com.mw.springcloud.utill.MyAccessDeniedHandler;
import com.mw.springcloud.utill.MyAuthenticationEntryPoint;
import com.mw.springcloud.utill.TokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SecurityConfiguration 配置类
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Autowired
    private MyAccessDeniedHandler MyAccessDeniedHandler;
    @Autowired
    private MyAuthenticationEntryPoint myAuthenticationEntryPoint;
    @Autowired
    MyUserDetailsService service;
    @Autowired
    LdapUserDetailsService ldapService;
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();//加盐加密   md5  e10xxxxx
        //如果不想加密就返回
//        return NoOpPasswordEncoder.getInstance();
    }

    //静态资源直接放行
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        //忽略这些静态资源（不拦截）  新版本 Spring Security 6.0 已经弃用 antMatchers()
        return (web) -> web.ignoring().requestMatchers("/index", "/oauth/redirect", "*.html", "/js/**", "/css/**", "/images/**", "*.jpg", "/*/login", "/loginError");
    }

    @Bean
    UserDetailsService userDetailsService(){
        InMemoryUserDetailsManager manager= new InMemoryUserDetailsManager();
        //考虑到一般数据库存的是密文
        UserDetails user1= User.withUsername("admin").password(passwordEncoder().encode("123")).roles("admin").build();
        UserDetails user2= User.withUsername("boss").password(passwordEncoder().encode("123")).roles("boss").build();
        manager.createUser(user1);
        manager.createUser(user2);
        return manager;
    }

    /***
     * 默认账号登录
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    @Order(0)
    SecurityFilterChain memoryFilterChain(HttpSecurity http) throws Exception {
        http.sessionManagement(n->n.disable())
//                .authorizeHttpRequests(authz->authz.requestMatchers("index").permitAll())
                .authorizeRequests().anyRequest().authenticated();

        // 设置异常的EntryPoint的处理
        http.exceptionHandling(exceptions -> exceptions
                // 未登录
                .authenticationEntryPoint(myAuthenticationEntryPoint)
                // 权限不足
                .accessDeniedHandler(MyAccessDeniedHandler));
        http.addFilterBefore(new TokenFilter(userDetailsService()), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /***
     * 数据库账号登录
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    @Order(1)
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.sessionManagement(n->n.disable())
//                .authorizeHttpRequests(authz->authz.requestMatchers("index").permitAll())
                .authorizeRequests().anyRequest().authenticated();
        http.exceptionHandling(n->n.accessDeniedHandler(MyAccessDeniedHandler)
                .authenticationEntryPoint(myAuthenticationEntryPoint));
        http.addFilterBefore(new TokenFilter(service), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /***
     * LDAP账号登录
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    @Order(2)
    SecurityFilterChain LdapFilterChain(HttpSecurity http) throws Exception {
        http.sessionManagement(n->n.disable())
//                .authorizeHttpRequests(authz->authz.requestMatchers("index").permitAll())
                .authorizeRequests().anyRequest().authenticated();
        http.exceptionHandling(n->n.accessDeniedHandler(MyAccessDeniedHandler)
                .authenticationEntryPoint(myAuthenticationEntryPoint));
        http.addFilterBefore(new TokenFilter(ldapService), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
