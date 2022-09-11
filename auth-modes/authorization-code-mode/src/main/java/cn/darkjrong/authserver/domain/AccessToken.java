package cn.darkjrong.authserver.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 访问令牌
 *
 * @author Rong.Jia
 * @date 2022/09/11
 */
@NoArgsConstructor
@Data
public class AccessToken {

    @JSONField(name = "access_token")
    private String accessToken;
    @JSONField(name = "token_type")
    private String tokenType;
    @JSONField(name = "refresh_token")
    private String refreshToken;
    @JSONField(name = "expires_in")
    private Integer expiresIn;
    @JSONField(name = "scope")
    private String scope;
    @JSONField(name = "username")
    private UsernameDTO username;

    @NoArgsConstructor
    @Data
    public static class UsernameDTO {
        @JSONField(name = "id")
        private Integer id;
        @JSONField(name = "username")
        private String username;
        @JSONField(name = "password")
        private String password;
        @JSONField(name = "enabled")
        private Boolean enabled;
        @JSONField(name = "authorities")
        private List<AuthoritiesDTO> authorities;
        @JSONField(name = "accountNonExpired")
        private Boolean accountNonExpired;
        @JSONField(name = "accountNonLocked")
        private Boolean accountNonLocked;
        @JSONField(name = "credentialsNonExpired")
        private Boolean credentialsNonExpired;

        @NoArgsConstructor
        @Data
        public static class AuthoritiesDTO {
            @JSONField(name = "authority")
            private String authority;
        }
    }
}
