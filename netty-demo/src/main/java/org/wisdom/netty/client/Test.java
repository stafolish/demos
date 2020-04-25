package org.wisdom.netty.client;

public class Test {
    public static void main(String[] args) {
        byte[] bytes = BytesUtil.intToBytes2(65535);


        System.out.println(BytesUtil.byte2ToInt(new byte[]{7, -47}));
    }



}
