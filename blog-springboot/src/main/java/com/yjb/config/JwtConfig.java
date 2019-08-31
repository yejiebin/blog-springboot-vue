package com.yjb.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "jwt")
@Data
@Component
public class JwtConfig {
    private long time;     // 5天(以秒s计)过期时间
    private String secret;// JWT密码
    private String prefix ;         // Token前缀
    private String header; // 存放Token的Header Key
}
