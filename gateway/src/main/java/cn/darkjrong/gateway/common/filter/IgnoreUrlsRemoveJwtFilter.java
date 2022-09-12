package cn.darkjrong.gateway.common.filter;

import cn.darkjrong.gateway.common.config.WhiteListConfig;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

/**
 * 白名单路径访问时需要移除JWT请求头
 * @author Rong.Jia
 */
@Slf4j
@Component
public class IgnoreUrlsRemoveJwtFilter implements WebFilter {

    private static final String AUTH_PREIFX = "oauth";

    @Autowired
    private WhiteListConfig whiteListConfig;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        log.debug("-----IgnoreUrlsRemoveJwtFilter--------------");

        ServerHttpRequest request = exchange.getRequest();
        URI uri = request.getURI();
        PathMatcher pathMatcher = new AntPathMatcher();
        //白名单路径移除JWT请求头
        List<String> ignoreUrls = whiteListConfig.getUrls();
        for (String ignoreUrl : ignoreUrls) {
            if (!StrUtil.contains(uri.getPath(), AUTH_PREIFX) && pathMatcher.match(ignoreUrl, uri.getPath())) {
                request = exchange.getRequest().mutate().header("Authorization", "").build();
                exchange = exchange.mutate().request(request).build();
                return chain.filter(exchange);
            }
        }
        return chain.filter(exchange);
    }
}
