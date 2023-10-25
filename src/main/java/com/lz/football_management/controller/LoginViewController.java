package com.lz.football_management.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class LoginViewController {

    @Autowired
    private DefaultKaptcha captchaProducer;

    @GetMapping("/")
    public String loginView(HttpSession session, Model model) {
        // 生成验证码
        String captcha = captchaProducer.createText();

        // 存储验证码到会话
        session.setAttribute("captcha", captcha);

        // 将验证码传递给登录页面
        model.addAttribute("captcha", captcha);

        return "login";
    }
}

