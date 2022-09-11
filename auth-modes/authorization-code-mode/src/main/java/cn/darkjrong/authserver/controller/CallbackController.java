package cn.darkjrong.authserver.controller;

import cn.darkjrong.authserver.domain.AccessToken;
import cn.darkjrong.authserver.domain.Oauth2TokenDTO;
import cn.darkjrong.oauth.common.enums.AuthMode;
import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.ejlchina.okhttps.HttpResult;
import com.ejlchina.okhttps.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 回调控制器
 *
 * @author Rong.Jia
 * @date 2022/09/11
 */
@Slf4j
@RestController
public class CallbackController {

    private static final String OAUTH = "http://localhost:%s/oauth/token";
    private static final String REDIRECT_URI = "http://localhost:%s/login/directly-callback";

    @Value("${server.port:8080}")
    private Integer port;

    @GetMapping("/login/callback")
    public String callback(@RequestParam String code, String state, HttpSession session) {
        log.info("code {} ", code);
        return "成功";
    }

    @GetMapping("/login/directly-callback")
    public AccessToken directlyCallback(@RequestParam String code, String state, HttpSession session) {
        log.info("code is {}, state is {}", code, state);

        Oauth2TokenDTO oAuth2TokenDTO = new Oauth2TokenDTO();
        oAuth2TokenDTO.setCode(code);
        oAuth2TokenDTO.setRedirect_uri(String.format(REDIRECT_URI, port));
        AccessToken oAuth2AccessToken = generateToken(oAuth2TokenDTO);
        session.setAttribute("token", oAuth2AccessToken);
        return oAuth2AccessToken;
    }

    /**
     * 生成令牌
     *
     * @param oAuth2TokenDTO oauth2牌dto
     * @return {@link AccessToken}
     */
    private AccessToken generateToken(Oauth2TokenDTO oAuth2TokenDTO) {
        Map<String, Object> params = JSONObject.parseObject(JSON.toJSONString(oAuth2TokenDTO), new TypeReference<Map<String, Object>>(){});
        String authorization = "Basic "  + Base64.encode(AuthMode.CLIENT_ID.getValue() + ":" + AuthMode.CLIENT_SECRET.getValue());

        HttpResult.Body body = HttpUtils.sync(String.format(OAUTH, port))
                .addHeader("Authorization", authorization)
                .addHeader("Content-Type", MediaType.APPLICATION_FORM_URLENCODED.toString())
                .addBodyPara(params)
                .post().getBody();

        return JSONObject.parseObject(body.toString(), AccessToken.class);
    }



}
