package cn.darkjrong.oauth.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用户信息
 *
 * @author Rong.Jia
 * @date 2022/08/13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoVO {

    private Long id;

    private String username;

    private String password;

    private Integer status;

    private List<String> roles;

}
