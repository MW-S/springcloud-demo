package net.mw.springcloud;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("net.mw.springcloud.dao")
public class ProviderProductApplication {
    public static void  main(String[] args){
        SpringApplication.run( ProviderProductApplication.class, args);
    }

/*    @Bean
    public FilterRegistrationBean registration(JwtAuthenticationTokenFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.setEnabled(false);
        return registration;
    }*/
}
