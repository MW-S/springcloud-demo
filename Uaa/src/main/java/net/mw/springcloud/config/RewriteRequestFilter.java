package net.mw.springcloud.config;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * @author 泽济天下
 * 处理formdata提交文件信息controller获取不到参数的问题
 */
@Component
@Slf4j
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class RewriteRequestFilter implements GlobalFilter, Ordered {
    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        String method = serverHttpRequest.getMethod().toString();
        if ("POST".equals(method)) {
            if(serverHttpRequest.getURI().toString().contains("del")){
                MultiValueMap<String, String> queryParams = serverHttpRequest.getQueryParams();
                //TODO 得到Get请求的请求参数后，做你想做的事
                log.info("------------------------------- 带Query的Post 请求参数 ------------------------------------");
                log.info(queryParams.toString());
                return chain.filter(exchange);
            }
            return DataBufferUtils.join(exchange.getRequest().getBody())
                    .flatMap(dataBuffer -> {
                        byte[] bytes = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(bytes);
                        String bodyStr = new String(bytes, StandardCharsets.UTF_8);
                        log.info("------------------------------- POST请求参数 ------------------------------------");
                        //TODO 拿到POST日志后的操作
                        DataBufferUtils.release(dataBuffer);
                        MediaType contentType = exchange.getRequest().getHeaders().getContentType();
                        log.info("contentType:" + contentType);
                        if(!contentType.toString().contains("file"))
                            log.info("Body: " + bodyStr);
                        Flux<DataBuffer> cachedFlux = Flux.defer(() -> {
                            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
                            DataBufferUtils.retain(buffer);
                            return Mono.just(buffer);
                        });
                        ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
                                @Override
                            public Flux<DataBuffer> getBody() {
                                return cachedFlux;
                            }
                        };
                        return chain.filter(exchange.mutate().request(mutatedRequest).build());
                    });
        } else if ("GET".equals(method)) {
            MultiValueMap<String, String> queryParams = serverHttpRequest.getQueryParams();
            //TODO 得到Get请求的请求参数后，做你想做的事
            log.info("------------------------------- GET请求参数 ------------------------------------");
            log.info("Params: " + queryParams.toString());
            return chain.filter(exchange);
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}

