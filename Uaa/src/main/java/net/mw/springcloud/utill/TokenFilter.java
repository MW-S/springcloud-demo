package net.mw.springcloud.utill;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class TokenFilter implements WebFilter {

    private ReactiveUserDetailsService service;
//    private ReactiveUserDetailsService service2;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String token = request.getHeaders().getFirst("token");
        if(StringUtils.hasText(token)){
            //通过token能不能拿到用户名
            String userName = null;
            try {
                userName = JwtUtils.getUserName(token);
            } catch (Exception e) {
                String msg = e.getMessage();
                byte[] value = new byte[0];
                try {
                    value = objectMapper.writeValueAsBytes(msg);
                } catch (JsonProcessingException jsonProcessingException) {
                    jsonProcessingException.printStackTrace();
                }
                DataBuffer wrap = response.bufferFactory().wrap(value);
                return response.writeWith(Mono.just(wrap));
            }
            Mono<UserDetails> m =  service.findByUsername(userName);
/*            if(m == null){
                m = service2.findByUsername(userName);
            }*/
            if(m != null){
                UserDetails user = m.block();
                if (user !=null && JwtUtils.validate(token)) {
                    //校验成功
                    //将用户的凭证存入到security的上下文中
                    UsernamePasswordAuthenticationToken authenticationToken= new UsernamePasswordAuthenticationToken(userName, user.getPassword(),user.getAuthorities());
//                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    return chain.filter(exchange)
                            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authenticationToken));

                }
            }
        }
        return chain.filter(exchange);
    }
}
