package com.lz.football_management.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class CaptchaConfig {

    @Bean
    public DefaultKaptcha captchaProducer() {
        DefaultKaptcha captchaProducer = new DefaultKaptcha();
        Properties properties = new Properties();
//         配置生成验证码的属性，例如验证码长度、字符集等
         properties.setProperty("kaptcha.textproducer.char.length", "4");
         properties.setProperty("kaptcha.textproducer.char.string", "123456789abcdefg");
        // ...
        Config config = new Config(properties);
        captchaProducer.setConfig(config);
        return captchaProducer;
    }

    // 验证验证码方法
    public static boolean validateCaptcha(String inputCaptcha, String storedCaptcha) {
        return inputCaptcha.equalsIgnoreCase(storedCaptcha);
    }
}
