package com.pagepal.capstone.configurations.appconfig;

import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Component
public class RequestHeaderInterceptor implements WebGraphQlInterceptor {

    private static final String AUTHORIZATION = "authorization";

    @Override
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
        String value = request.getHeaders().getFirst(AUTHORIZATION);
        request.configureExecutionInput((executionInput, builder) ->
                builder.graphQLContext(Collections.singletonMap(AUTHORIZATION, value)).build());
        return chain.next(request);
    }
}
