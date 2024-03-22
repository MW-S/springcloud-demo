package net.mw.springcloud.config;


import net.mw.springcloud.service.impl.MyUserDetailsService;
import net.mw.springcloud.utill.MyAuthenticationEntryPoint;
import net.mw.springcloud.utill.TokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * SecurityConfiguration 配置类
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {
    @Autowired
    private net.mw.springcloud.utill.MyAccessDeniedHandler MyAccessDeniedHandler;
    @Autowired
    private MyAuthenticationEntryPoint myAuthenticationEntryPoint;
    @Autowired
    MyUserDetailsService service;
/*    @Autowired
    LdapUserDetailsService ldapUserDetailsService;*/
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();//加盐加密   md5  e10xxxxx
    }

    /***
     * 默认账号登录
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    @Order(0)
    SecurityWebFilterChain filterChain(ServerHttpSecurity http) throws Exception {
        http
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/index", "/oauth/redirect", "*.html", "/js/**", "/css/**", "/images/**", "*.jpg", "/*/login", "/loginError")
                        .permitAll()
                        .pathMatchers("/product/getList").hasAuthority("read")
                        .pathMatchers("/product/save").hasAuthority("create")
                        .pathMatchers("/product/update").hasAuthority("update")
                        .pathMatchers("/product/delById").hasAuthority("delete")
                        .anyExchange().authenticated()
                )
                        .httpBasic(h->h.disable());

        // 设置异常的EntryPoint的处理
        http.exceptionHandling(exceptions -> exceptions
                // 未登录
                .authenticationEntryPoint(myAuthenticationEntryPoint)
                // 权限不足
                .accessDeniedHandler(MyAccessDeniedHandler));
        http.addFilterAt(new TokenFilter(service), SecurityWebFiltersOrder.HTTP_BASIC);
        http.csrf(csrf -> csrf.disable());
        http.cors(cors -> cors.disable());
        return http.build();
    }
}
