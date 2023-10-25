package com.lz.football_management.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.lz.football_management.config.CaptchaConfig;
import com.lz.football_management.dao.UserDao;
import com.lz.football_management.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
@RestController
public class LoginController {

    @Autowired
    private DefaultKaptcha captchaProducer;

    private final UserDao userDao;

    @Autowired
    public LoginController(UserDao userDao) {
        this.userDao = userDao;
    }

    @PostMapping("/login")
    public ModelAndView login(@RequestParam String username,
                                        @RequestParam String password,
                                        @RequestParam String captcha,
                                        HttpSession session) {
        // 验证验证码
        String storedCaptcha = (String) session.getAttribute("captcha");
        if (!CaptchaConfig.validateCaptcha(captcha, storedCaptcha)) {
            ModelAndView modelAndView = new ModelAndView("redirect:/");
            modelAndView.addObject("msg","验证码错误");
            return modelAndView;

        }

        // 进行登录逻辑
        User user = userDao.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            // 登录成功
            return new ModelAndView("redirect:/index");
        } else {
            // 登录失败
            ModelAndView modelAndView = new ModelAndView("redirect:/");
            modelAndView.addObject("msg","用户名或密码错误");
            return modelAndView;
        }
    }
    @RequestMapping("/index")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("index");
        // 在这里可以添加需要传递给"index"页面的数据
        return modelAndView;
    }
}
