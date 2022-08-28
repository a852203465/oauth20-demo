package cn.darkjrong.gateway.common.authorization;

import cn.darkjrong.gateway.common.utils.WebUtils;
import cn.darkjrong.oauth.common.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * token无效或者已过期自定义响应
 *
 * @author Rong.Jia
 * @date 2022/08/28
 */
@Slf4j
@Component
public class RestAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException exception) {

        log.error("Authentication异常: [{}], [{}], [{}]", exchange.getRequest().getURI(), exception.getMessage(), exception);

        //自定义返回格式内容
        ResponseVO<Object> responseVO = ResponseVO.error(401, exception.getMessage());

        ServerHttpResponse response = exchange.getResponse();
        return WebUtils.writeFailedToResponse(response, responseVO);
    }
}
