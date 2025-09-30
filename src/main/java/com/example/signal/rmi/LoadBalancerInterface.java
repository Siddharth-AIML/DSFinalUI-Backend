package com.example.signal.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface LoadBalancerInterface extends Remote {
    String requestGreen(int signalNumber) throws RemoteException;
    String manualOverride(int signalNumber) throws RemoteException;
    String getGlobalStatus() throws RemoteException;
}

