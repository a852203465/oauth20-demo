package cn.darkjrong.authserver.config;

import cn.darkjrong.oauth.common.dto.SecurityUser;
import cn.hutool.core.map.MapUtil;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * JWT内容增强器
 *
 * @author Rong.Jia
 * @date 2022/08/14
 */
@Component
public class JwtTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        Map<String, Object> info = MapUtil.newHashMap();
        info.put("username", securityUser.getUsername());
        info.put("id", securityUser.getId());
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
        return accessToken;
    }
}
