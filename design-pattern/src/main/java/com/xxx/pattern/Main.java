package com.xxx.pattern;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println(com.sun.deploy.net.URLEncoder.encode("http://21323123 skafjk+ewerw", StandardCharsets.UTF_8.name()));
            System.out.println(URLEncoder.encode("http://21323123 skafjk+ewerw", StandardCharsets.UTF_8.name()));
            System.out.println(URLDecoder.decode("http%3A%2F%2F21323123+skafjk%2Bewerw", StandardCharsets.UTF_8.name()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
