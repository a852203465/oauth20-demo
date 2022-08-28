package cn.darkjrong.resource.controller;

import cn.darkjrong.oauth.common.constants.AuthConstant;
import cn.darkjrong.oauth.common.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * api控制器
 *
 * @author Rong.Jia
 * @date 2022/08/27
 */
@Slf4j
@RestController
@RequestMapping("")
public class ApiController {

    @GetMapping(value = "hello", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO<String> hello() {

        log.info("hello {}", System.currentTimeMillis());

        return ResponseVO.success("hello world");
    }

    @GetMapping(value = "currentUser", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO<String> currentUser() {
        log.info("currentUser {}", System.currentTimeMillis());
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return ResponseVO.success(request.getHeader(AuthConstant.ACCOUNT));
    }















}
