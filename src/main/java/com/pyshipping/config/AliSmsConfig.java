package com.pyshipping.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class AliSmsConfig {
    @Value("${ali-sign-name}")
    private String aliSignName;
    @Value("${ali-testing-sign-name}")
    private String testingAliSignName;
    @Value("${ali-sms-template}")
    private String templateCode;
    @Value("${ali-sms-testing-template}")
    private String testingTemplateCode;
    @Value("${ali-sms-access-id}")
    private String accessId;
    @Value("${ali-sms-access-key}")
    private String accessKey;
    @Value("${ali-sms-region-id}")
    private String regionId;
}
