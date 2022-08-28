package cn.darkjrong.gateway.common.filter;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@Data
public class Payload implements Serializable {

    private Long id;
    private String jti;
    @JSONField(name = "user_name")
    private String username;
    private List<String> authorities;

}
