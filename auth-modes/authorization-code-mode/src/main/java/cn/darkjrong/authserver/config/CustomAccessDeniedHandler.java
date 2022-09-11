package cn.darkjrong.authserver.config;

import cn.darkjrong.oauth.common.vo.ResponseVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 权限不足异常
 *
 * @author Rong.Jia
 * @date 2022/08/28
 */
@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws IOException, ServletException {

        log.error("AccessDenied异常: [{}], [{}], [{}]", exception.getMessage(), exception.getLocalizedMessage(), exception.toString());

        ResponseVO<Object> error = ResponseVO.error(401, "认证过的用户访问无权限资源时的异常");

        response.setStatus(HttpStatus.OK.value());
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(error));

    }

}
