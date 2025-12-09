package co.edu.unbosque.apigateway.filter;

import co.edu.unbosque.apigateway.dto.TokenValidationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;

@Component
public class JwtValidationFilterFactory extends AbstractGatewayFilterFactory<Object> {

    private final WebClient webClient;

    public JwtValidationFilterFactory(WebClient.Builder webClientBuilder, @Value("${auth.service.url}") String authServiceUrl) {
        this.webClient = webClientBuilder.baseUrl(authServiceUrl).build();
    }

    @Override
    public GatewayFilter apply(Object config) {
        return (((exchange, chain) -> {
            String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if(token == null || !token.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            return webClient.get()
                    .uri("/validate")
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .retrieve()
                    .bodyToMono(TokenValidationResponse.class)
                    .flatMap(validation -> {
                        if(!validation.valid()){
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            return  exchange.getResponse().setComplete();
                        }

                        ServerHttpRequest mutatedRequest = exchange.getRequest()
                                .mutate()
                                .header("X-Tenant-ID", validation.tenantId())
                                .header("X-User-ID", validation.userId())
                                .build();

                        ServerWebExchange mutatedExchange = exchange
                                .mutate()
                                .request(mutatedRequest)
                                .build();

                        return chain.filter(mutatedExchange);
                    })
                    .then(chain.filter(exchange));

        }));
    }
}
