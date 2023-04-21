package com.pyshipping;

import com.pyshipping.common.pwd.Pwd;
import com.pyshipping.common.pwd.RandomCode;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PyShippingApplicationTests {

    @Test
    void contextLoads() {

    }
    @Test
    public void testRandomCode() {
        System.out.println(RandomCode.randomString(4));
    }

}
