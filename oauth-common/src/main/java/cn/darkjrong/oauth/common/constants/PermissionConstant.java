package cn.darkjrong.oauth.common.constants;

import cn.darkjrong.oauth.common.vo.UserInfoVO;
import cn.hutool.core.collection.CollUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 许可常数
 *
 * @author Rong.Jia
 * @date 2022/08/27
 */
public class PermissionConstant {

    public static final Map<String, List<String>> RESOURCE_ROLES = new TreeMap<String, List<String>>() {{
        put("/hello", CollUtil.toList("ADMIN"));
        put("/currentUser", CollUtil.toList("ADMIN", "TEST"));
    }};

    public static final Map<String, UserInfoVO> USERS = new HashMap<String, UserInfoVO>() {{
        put("admin", new UserInfoVO(1L,"admin", "$2a$10$tJuvROdbLbUjjHminP8Y9uqk1JzrxxKiNP4P1yNiNxbNclThJeKG2" ,1, CollUtil.toList("ADMIN")));
        put("jay", new UserInfoVO(2L,"jay", "$2a$10$tJuvROdbLbUjjHminP8Y9uqk1JzrxxKiNP4P1yNiNxbNclThJeKG2" ,1, CollUtil.toList("TEST")));
    }};


}
