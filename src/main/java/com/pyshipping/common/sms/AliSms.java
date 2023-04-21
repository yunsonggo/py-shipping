package com.pyshipping.common.sms;

import com.pyshipping.config.AliSmsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.annotation.PostConstruct;

/**
 * 阿里云发送短信验证码
 */
@Component
public class AliSms {
    @Autowired
    private AliSmsConfig aliSmsConfig;
    private static AliSmsConfig staticAliSmsConfig;
    @PostConstruct
    public void init(){
        staticAliSmsConfig = this.aliSmsConfig;
    }

    public static com.aliyun.dysmsapi20170525.Client createClient() throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config();
        config.setAccessKeyId(staticAliSmsConfig.getAccessId());
        config.setAccessKeySecret(staticAliSmsConfig.getAccessKey());
        config.endpoint="dysmsapi.aliyuncs.com";
        return new com.aliyun.dysmsapi20170525.Client(config);
    }

    public static com.aliyun.dysmsapi20170525.models.SendSmsResponse sendSmsCode(String phoneNumber,int smsCode) throws Exception {
        com.aliyun.dysmsapi20170525.Client client = AliSms.createClient();
        com.aliyun.dysmsapi20170525.models.SendSmsRequest sendRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest();
        sendRequest.setSignName(staticAliSmsConfig.getAliSignName());
        sendRequest.setTemplateCode(staticAliSmsConfig.getTemplateCode());
        sendRequest.setPhoneNumbers(phoneNumber);
        sendRequest.setTemplateParam("{\"code\":\""+smsCode+"\"}");
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        return client.sendSmsWithOptions(sendRequest, runtime);
    }
}
