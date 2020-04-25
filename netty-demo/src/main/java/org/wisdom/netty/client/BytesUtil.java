package org.wisdom.netty.client;

public class BytesUtil {
    private BytesUtil(){}

    public static byte[] intToBytes2(int n) {
        byte[] b = new byte[2];
        int temp = n >>> 8;
        byte high;
        if (temp > 127) {
            high = (byte) (n - 256);
        } else {
            high = (byte) temp;
        }
        temp = n & 0xff;
        byte low;
        if (temp > 127) {
            low = (byte) (n - 256);
        } else {
            low = (byte) temp;
        }
        b[0] = high;
        b[1] = low;
        return b;

    }

    public static int byte2ToInt(byte[] b) {
        int rn = 0;
        for (int i = 0; i < 2; i++) {
            int n = new Integer(b[i]).intValue();
            if (n < 0) {
                n += 256;
            }
            rn = (rn * 256) + n;

        }
        return rn;
    }
}
