package cn.darkjrong.authserver.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * 回调控制器
 *
 * @author Rong.Jia
 * @date 2022/09/11
 */
@Slf4j
@RestController
public class CallbackController {

    @GetMapping("/login/callback")
    public String callback(@RequestParam String code, String state, HttpSession session) {
        log.info("code {} ", code);
        return "成功";
    }




}
