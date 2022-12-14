package cn.darkjrong.authserver.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.endpoint.WhitelabelApprovalEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;
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
//@Controller
//@RequestMapping("/oauth")
//@SessionAttributes("authorizationRequest")
public class AuthController {

    @Autowired
    private TokenEndpoint tokenEndpoint;

    @Autowired
    private AuthorizationEndpoint authorizationEndpoint;

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

    /**
     * 授权
     *
     * @param principal     主要
     * @param parameters    参数
     * @param model         模型
     * @param sessionStatus 会话状态
     * @return {@link ModelAndView}
     */
    @RequestMapping(value = "/authorize")
    public ModelAndView authorize(Map<String, Object> model, @RequestParam Map<String, String> parameters,
                                  SessionStatus sessionStatus, Principal principal) {
        log.info("authorize {}", parameters.toString());
        return authorizationEndpoint.authorize(model, parameters, sessionStatus, principal);
    }

//    /**
//     * 批准或拒绝
//     *
//     * @param approvalParameters 批准参数
//     * @param model              模型
//     * @param sessionStatus      会话状态
//     * @param principal          主要
//     * @return {@link View}
//     */
//    @RequestMapping(value = "/authorize", method = RequestMethod.POST, params = OAuth2Utils.USER_OAUTH_APPROVAL)
//    public View approveOrDeny(@RequestParam Map<String, String> approvalParameters, Map<String, ?> model,
//                              SessionStatus sessionStatus, Principal principal) {
//        log.info("approveOrDeny {}", approvalParameters.toString());
//        return authorizationEndpoint.approveOrDeny(approvalParameters, model, sessionStatus, principal);
//    }

}
