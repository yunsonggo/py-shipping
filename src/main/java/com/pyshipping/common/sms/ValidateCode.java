package com.pyshipping.common.sms;

import java.util.Random;

/**
 * 生成随机验证码
 */
public class ValidateCode {
    public static Integer generateValidateCode(int length) {
        Integer code = null;
        if (length == 4) {
            code = new Random().nextInt(9999);
            if (code < 1000) {
                code = code + 1000;
            }
        } else if(length == 6) {
            code = new Random().nextInt(9999);
            if (code < 100000) {
                code = code + 100000;
            }
        } else {
            throw new RuntimeException("只能生成四位或六位数字验证码");
        }
        return code;
    }

    public static String generateValidateStringCode(int length) {
        Random rdm = new Random();
        String hash1 = Integer.toHexString(rdm.nextInt());
        return hash1.substring(0,length);
    }
}
