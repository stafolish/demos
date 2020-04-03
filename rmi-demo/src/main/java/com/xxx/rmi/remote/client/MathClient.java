package com.xxx.rmi.remote.client;


import com.xxx.rmi.remote.IRemoteMath;
import org.springframework.stereotype.Component;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
@Component
public class MathClient {

    public static void main(String[] args) {
        try {
            // 如果RMI Registry就在本地机器上，URL就是:rmi://localhost:1099/hello
            // 否则，URL就是：rmi://RMIService_IP:1099/hello
            Registry registry = LocateRegistry.getRegistry("localhost");
            // 从Registry中检索远程对象的存根/代理
            IRemoteMath remoteMath = (IRemoteMath)registry.lookup("Compute");
            // 调用远程对象的方法
            double addResult = remoteMath.add(5, 3);
            System.out.println("5 + 3 = " + addResult);
            double subResult = remoteMath.sub(5, 3);
            System.out.println("5 - 3 = " + subResult);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
