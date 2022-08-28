package cn.darkjrong.gateway.common.authorization;

import cn.darkjrong.gateway.common.config.WhiteListConfig;
import cn.darkjrong.oauth.common.constants.AuthConstant;
import cn.darkjrong.oauth.common.constants.PermissionConstant;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 鉴权管理器
 * @author Rong.Jia
 * @date 2021/17:21
 */
@Slf4j
@Component
public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    @Autowired
    private WhiteListConfig whiteListConfig;

    @Autowired
    private GatewayProperties gatewayProperties;

    private static final PathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {

        log.info("============== AuthorizationManager =========================");

        ServerHttpRequest request = authorizationContext.getExchange().getRequest();
        String restPath = request.getURI().getPath();

        log.info("AuthorizationManager :restPath {}", restPath);

        for (RouteDefinition route : gatewayProperties.getRoutes()) {
            for (PredicateDefinition predicate : route.getPredicates()) {
                List<String> urls = predicate.getArgs().values().stream().map(a -> StrUtil.removeSuffix(a, "/**")).collect(Collectors.toList());
                String url = urls.stream().filter(a -> StrUtil.contains(request.getURI().getPath(), a)).findAny().orElse(StrUtil.EMPTY);
                restPath = StrUtil.replace(restPath, url, StrUtil.EMPTY);
            }
        }

        // 对应跨域的预检请求直接放行
        if (request.getMethod() == HttpMethod.OPTIONS) {
            return Mono.just(new AuthorizationDecision(Boolean.TRUE));
        }

        // 无需鉴权直接放行
        for (String url : whiteListConfig.getUrls()) {
            if (pathMatcher.match(url, restPath)) {
                log.debug("请求无需鉴权，请求路径：{}", restPath);
                return Mono.just(new AuthorizationDecision(Boolean.TRUE));
            }
        }

        Map<String, List<String>> roleMap = PermissionConstant.RESOURCE_ROLES;

        String finalRestPath = restPath;
        List<String> roleList = roleMap.keySet().stream()
                .filter(a -> pathMatcher.match(a, finalRestPath))
                .collect(Collectors.toList());

        List<String> authorities = CollectionUtil.newArrayList();
        for (String role : roleList) {
            authorities.addAll(roleMap.get(role)
                    .stream().map(i -> i = AuthConstant.AUTHORITY_PREFIX + i)
                    .collect(Collectors.toList()));
        }

        //认证通过且角色匹配的用户可访问当前路径
        return mono.filter(Authentication::isAuthenticated)
                .flatMapIterable(Authentication::getAuthorities)
                .map(GrantedAuthority::getAuthority)
                .any(authorities::contains)
                .map(AuthorizationDecision::new)
                .defaultIfEmpty(new AuthorizationDecision(false));
    }

}
