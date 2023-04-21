package com.pyshipping.common.pwd;

import java.util.Random;

public  class RandomCode {
    public static String randomString(Integer size) {
        Random r = new Random();
        int i = r.nextInt((int) (size* Math.pow(10,(size-2)))) + (int) Math.pow(10,size-1);
        return Integer.toString(i);
    }
}
