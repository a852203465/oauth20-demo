package cn.darkjrong.gateway.common.handler;

import cn.darkjrong.gateway.common.provider.ResponseProvider;
import cn.hutool.core.lang.Validator;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

/**
 * 异常处理
 *
 * @author Rong.Jia
 * @date 2021/08/18 13:17:11
 */
@Slf4j
public class ErrorExceptionHandler extends DefaultErrorWebExceptionHandler {

    private final static String SERVER_ERROR_TXT = "服务器内部错误";
    private final static String ARGUMENTS_ERROR_TXT = "参数错误";
    private final static String SERVICE_NOT_FOUND_TXT = "找不到服务";
    private final static String BAD_REQUEST_TXT = "错误的请求";

    public ErrorExceptionHandler(ErrorAttributes errorAttributes, WebProperties.Resources resources,
                                 ErrorProperties errorProperties, ApplicationContext applicationContext) {
        super(errorAttributes, resources, errorProperties, applicationContext);
    }

    /**
     * 获取异常属性
     */
    @Override
    protected Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        int code = 500;
        Throwable error = super.getError(request);
        Map<String, Object> errorAttributes = super.getErrorAttributes(request, options);

        log.error("网关出现异常了,异常为:{} ,异常类型为:{},请求为:{}", error.getMessage(), error.getCause(), JSON.toJSONString(errorAttributes));

        if (error instanceof NotFoundException) {
            code = 404;
        }
        if (error instanceof ResponseStatusException) {
            code = ((ResponseStatusException) error).getStatus().value();
        }
        return ResponseProvider.response(code, this.buildMessage(code));
    }

    /**
     * 指定响应处理方法为JSON处理的方法
     *
     * @param errorAttributes 错误的属性
     * @return {@link RouterFunction}<{@link ServerResponse}>
     */
    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    @Override
    protected int getHttpStatus(Map<String, Object> errorAttributes) {
        return (int) errorAttributes.get("code");
    }

    /**
     * 构建异常信息
     *
     * @param request 请求
     * @param ex      异常
     * @return {@link String}
     */
    private String buildMessage(ServerRequest request, Throwable ex) {
        StringBuilder message = new StringBuilder("Failed to handle request [");
        message.append(request.methodName());
        message.append(" ");
        message.append(request.uri());
        message.append("]");
        if (ex != null) {
            message.append(": ");
            message.append(ex.getMessage());
        }
        return message.toString();
    }

    /**
     * 构建异常信息
     *
     * @param status 状态
     * @return {@link String}
     */
    private String buildMessage(Integer status) {
        String message = SERVER_ERROR_TXT;
        if (Validator.isNull(status)) {
            message = SERVER_ERROR_TXT;
        } else {
            switch (status) {
                case 500:
                    message = SERVER_ERROR_TXT;
                    break;
                case 404:
                    message = SERVICE_NOT_FOUND_TXT;
                    break;
                case 400:
                    message = ARGUMENTS_ERROR_TXT;
                    break;
                default:
                    break;
            }
        }
        return message;
    }


}
