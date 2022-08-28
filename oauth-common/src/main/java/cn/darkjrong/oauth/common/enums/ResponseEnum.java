package cn.darkjrong.oauth.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应枚举
 *
 * @author Rong.Jia
 * @date 2022/08/14
 */
@Getter
@AllArgsConstructor
public enum ResponseEnum {

    // 成功
    SUCCESS(0, "成功"),

    // 参数不正确
    PARAMETER_ERROR(1, "参数不正确"),

    // 失败
    ERROR(-1, "失败"),
    SYSTEM_ERROR(-1, "系统错误"),
    FILE_LIMIT_EXCEEDED(-1, "文件超出限制, 请选择较小文件"),
    INT404_NOT_FOUND(-1, "找不到请求接口"),
    INT400_BAD_REQUEST(-1, "请求参数或方式错误"),


    // 未找到
    NOT_FOUND(404, "请求接口不存在"),

    // 请求方式错误
    REQUEST_MODE_ERROR(405, "请求方式错误, 请检查"),

    //媒体类型不支持
    MEDIA_TYPE_NOT_SUPPORTED(415, "媒体类型不支持"),

    SERVICE_EXCEPTIONS(4000, "服务异常, 请联系管理员"),

    UNAUTHORIZED(401, "无权访问(未授权)"),
    USERNAME_PASSWORD_ERROR(401, "用户名或密码错误"),
    CREDENTIALS_EXPIRED(401, "该账户的登录凭证已过期，请重新登录"),
    ACCOUNT_DISABLED(401, "该账户已被禁用，请联系管理员"),
    ACCOUNT_LOCKED(401, "该账号已被锁定，请联系管理员"),
    ACCOUNT_EXPIRED(401, "该账号已过期，请联系管理员"),
    PERMISSION_DENIED(401, "没有访问权限，请联系管理员"),
    USER_DOES_NOT_EXIST(401, "用户不存在"),
    NOT_LOGGED_IN(401, "未登录，或者授权过期"),
    MISSING_TOKEN_AUTHENTICATION_FAILED(401, "缺失令牌,鉴权失败"),










    ;

    private final Integer code;
    private final String message;


}
