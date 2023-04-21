package com.pyshipping.controller;

import com.pyshipping.common.codes.Codes;
import com.pyshipping.common.msg.CustomException;
import com.pyshipping.common.msg.Msg;
import com.pyshipping.common.sms.AliSms;
import com.pyshipping.common.sms.ValidateCode;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/sms")
public class AliSmsController {

    // 发送验证码
    @GetMapping("{phone}")
    public Msg<String> sendSms(@PathVariable("phone") String phoneNumber) {
        // TODO:: redis查询phoneNumber 冷却时间
       int code = ValidateCode.generateValidateCode(6);
       try {
           com.aliyun.dysmsapi20170525.models.SendSmsResponse response = AliSms.sendSmsCode(phoneNumber,code);
           if (response.getBody().getCode().equals("OK")) {
               // TODO:: 存入redis

               return Msg.success(Codes.OK,"发送成功,请查收","ok");
           } else {
               return Msg.error(Codes.Internal,"发送失败",response.getBody().getMessage());
           }

       } catch (Exception e) {
           throw new CustomException("发送失败:"+e.getMessage());
       }
    }
}
