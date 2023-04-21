package com.pyshipping.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class JwtConfig {
    @Value("${jwt-key}")
    private String jwtkey;
    @Value("${expire-day}")
    private Integer expireday;
}
