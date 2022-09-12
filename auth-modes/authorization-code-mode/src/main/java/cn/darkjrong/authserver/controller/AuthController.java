package cn.darkjrong.authserver.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.endpoint.WhitelabelApprovalEndpoint;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

/**
 * 身份验证控制器
 *
 * @author Rong.Jia
 * @date 2022/09/12
 */
@Slf4j
@Controller
@RequestMapping("/oauth")
@SessionAttributes("authorizationRequest")
public class AuthController {

    @Autowired
//    令牌处理器
    private TokenEndpoint tokenEndpoint;

    @Autowired
//    授权处理器
    private AuthorizationEndpoint authEndpoint;

    @Autowired
    private WhitelabelApprovalEndpoint whitelabelApprovalEndpoint;

    /**
     * Oauth2获取token
     *
     * @param principal  主要
     * @param parameters 参数
     * @return {@link OAuth2AccessToken}
     * @throws HttpRequestMethodNotSupportedException http请求方法不支持异常
     */
    @RequestMapping(value = "/token", method = RequestMethod.POST)
    @ResponseBody
    public OAuth2AccessToken postAccessToken(Principal principal, @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        log.info("postAccessToken {}", parameters.toString());
        return tokenEndpoint.postAccessToken(principal,parameters).getBody();
    }

    /**
     * 授权端点
     *
     * @param model   模型
     * @param request 请求
     * @return {@link ModelAndView}
     * @throws Exception 异常
     */
    @RequestMapping("/confirm_access")
    public ModelAndView getAccessConfirmation(Map<String, Object> model, HttpServletRequest request) throws Exception{
        log.info("getAccessConfirmation {}", model.toString());
        return whitelabelApprovalEndpoint.getAccessConfirmation(model,request);
    }




}
