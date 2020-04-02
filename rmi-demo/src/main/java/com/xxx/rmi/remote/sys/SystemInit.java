package com.xxx.rmi.remote.sys;

import com.xxx.rmi.remote.server.RMIServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class SystemInit implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private RMIServer rmiServer;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            rmiServer.start();
        }
    }
}
