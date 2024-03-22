package net.mw.springcloud.utill;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class MyAccessDeniedHandler implements ServerAccessDeniedHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException e) {
        ServerHttpResponse response = exchange.getResponse();
        String msg = e.getMessage();
        byte[] value = new byte[0];
        try {
//            value = objectMapper.writeValueAsBytes(msg);
            value = objectMapper.writeValueAsBytes("权限不足");
        } catch (JsonProcessingException jsonProcessingException) {
            jsonProcessingException.printStackTrace();
        }
        DataBuffer wrap = response.bufferFactory().wrap(value);
        return response.writeWith(Mono.just(wrap));
    }
}
