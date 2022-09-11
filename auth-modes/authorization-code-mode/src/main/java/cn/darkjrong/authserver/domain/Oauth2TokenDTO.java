package cn.darkjrong.authserver.domain;

import cn.darkjrong.oauth.common.enums.AuthMode;
import lombok.Data;

/**
 * oauth2牌dto
 *
 * @author Rong.Jia
 * @date 2022/09/11
 */
@Data
public class Oauth2TokenDTO {

    private String code;
    private String grant_type = AuthMode.AUTHORIZATION_CODE.getValue();
    private String redirect_uri;

}
