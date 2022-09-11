package cn.darkjrong.authserver.service.impl;

import cn.darkjrong.oauth.common.constants.PermissionConstant;
import cn.darkjrong.oauth.common.dto.SecurityUser;
import cn.darkjrong.oauth.common.enums.ResponseEnum;
import cn.darkjrong.oauth.common.vo.UserInfoVO;
import cn.hutool.core.util.ObjectUtil;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 用户管理业务类
 *
 * @author Rong.Jia
 * @date 2022/08/13
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfoVO userInfoVO = PermissionConstant.USERS.get(username);
        if (ObjectUtil.isEmpty(userInfoVO)) {
            throw new UsernameNotFoundException(ResponseEnum.USERNAME_PASSWORD_ERROR.getMessage());
        }
        SecurityUser securityUser = new SecurityUser(userInfoVO);
        if (!securityUser.isEnabled()) {
            throw new DisabledException(ResponseEnum.ACCOUNT_DISABLED.getMessage());
        } else if (!securityUser.isAccountNonLocked()) {
            throw new LockedException(ResponseEnum.ACCOUNT_LOCKED.getMessage());
        } else if (!securityUser.isAccountNonExpired()) {
            throw new AccountExpiredException(ResponseEnum.ACCOUNT_EXPIRED.getMessage());
        } else if (!securityUser.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException(ResponseEnum.CREDENTIALS_EXPIRED.getMessage());
        }
        return securityUser;
    }

}
