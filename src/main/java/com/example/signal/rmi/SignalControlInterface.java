package com.example.signal.rmi;
import java.rmi.Remote;
import java.rmi.RemoteException;
import com.example.signal.utils.DBHelper;
public interface SignalControlInterface extends Remote {
    String requestGreen(int signalNumber) throws RemoteException;
    String getSignalStatus() throws RemoteException;

    // ✅ NEW: Manual override for VIP car priority
    String manualOverride(int signalNumber) throws RemoteException;
}
