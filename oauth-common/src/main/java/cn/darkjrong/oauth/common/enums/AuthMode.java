package cn.darkjrong.oauth.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 身份验证模式
 *
 * @author Rong.Jia
 * @date 2022/08/27
 */
@Getter
@AllArgsConstructor
public enum AuthMode {

    // clientId
    CLIENT_ID("client-app"),
    CLIENT_SECRET("123456"),

    SCOPES("all"),

    PASSWORD("password"),

    TOKEN_HEAD("Bearer "),

    REFRESH_TOKEN("refresh_token"),

    CLIENT_CREDENTIALS("client_credentials"),

    AUTHORIZATION_CODE("authorization_code"),

    IMPLICIT("implicit"),


    ;




    private final String value;


}
