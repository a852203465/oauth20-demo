package cn.darkjrong.gateway.common.authorization;

import cn.darkjrong.gateway.common.utils.WebUtils;
import cn.darkjrong.oauth.common.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 未授权
 *
 * @author Rong.Jia
 * @date 2022/08/14
 */
@Slf4j
@Component
public class CustomAccessDeniedHandler implements ServerAccessDeniedHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException exception) {

        log.error("AccessDenied异常: [{}], [{}], [{}]", exception.getMessage(), exception.getLocalizedMessage(), exception.toString());

        ResponseVO<Object> responseVO = ResponseVO.error(HttpStatus.FORBIDDEN.value(), "认证过的用户访问无权限资源时的异常");

        ServerHttpResponse response = exchange.getResponse();
        return WebUtils.writeFailedToResponse(response, responseVO);
    }
}
