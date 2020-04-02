package com.xxx.rmi.remote.impl;

import com.xxx.rmi.remote.IRemoteMath;
import org.springframework.stereotype.Service;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class RomoteMathImpl extends UnicastRemoteObject implements IRemoteMath {


    private AtomicLong numberOfComputations;


    protected RomoteMathImpl() throws RemoteException {
        numberOfComputations = new AtomicLong();
    }

    @Override
    public int add(int a, int b) throws RemoteException {
        numberOfComputations.incrementAndGet();
        System.out.println("Number of computations performed so far = "
                + numberOfComputations);
        return (a + b);
    }

    @Override
    public int sub(int a, int b) throws RemoteException {
        numberOfComputations.incrementAndGet();
        System.out.println("Number of computations performed so far = "
                + numberOfComputations);
        return (a - b);
    }
}
