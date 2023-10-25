package com.lz.football_management.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;

@Controller
public class CaptchaController {

    @Autowired
    private DefaultKaptcha captchaProducer;

    @GetMapping("/captcha")
    public void captchaImage(HttpServletResponse response, HttpSession session) throws Exception {
        // 生成验证码文本
        String captchaText = captchaProducer.createText();

        // 将验证码存储到会话中
        session.setAttribute("captcha", captchaText);

        // 生成验证码图片并写入响应
        response.setContentType("image/jpeg");
        BufferedImage captchaImage = captchaProducer.createImage(captchaText);
        ServletOutputStream outputStream = response.getOutputStream();
        ImageIO.write(captchaImage, "jpeg", outputStream);
        outputStream.flush();
        outputStream.close();
    }
}
