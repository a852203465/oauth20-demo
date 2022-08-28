package cn.darkjrong.gateway.common.utils;

import cn.darkjrong.oauth.common.enums.ResponseEnum;
import cn.darkjrong.oauth.common.vo.ResponseVO;
import cn.hutool.core.util.CharsetUtil;
import com.alibaba.fastjson2.JSON;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

/**
 *  WEB 工具类
 * @author Rong.Jia
 * @date 2021/05/19 13:30
 */
public class WebUtils {

    public static Mono<Void> writeFailedToResponse(ServerHttpResponse response, ResponseEnum responseEnum){
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.getHeaders().set(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        response.getHeaders().set(HttpHeaders.CACHE_CONTROL, "no-cache");
        String body = JSON.toJSONString(ResponseVO.error(responseEnum));
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(CharsetUtil.CHARSET_UTF_8));
        return response.writeWith(Mono.just(buffer))
                .doOnError(error -> DataBufferUtils.release(buffer));
    }

    public static Mono<Void> writeFailedToResponse(ServerHttpResponse response, ResponseVO responseVO){
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.getHeaders().set(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        response.getHeaders().set(HttpHeaders.CACHE_CONTROL, "no-cache");
        String body = JSON.toJSONString(responseVO);
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(CharsetUtil.CHARSET_UTF_8));
        return response.writeWith(Mono.just(buffer))
                .doOnError(error -> DataBufferUtils.release(buffer));
    }

}
