package com.xxx.rmi.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteMath extends Remote {

    public int add(int a, int b) throws RemoteException;


    public int sub(int a, int b) throws RemoteException;
}
