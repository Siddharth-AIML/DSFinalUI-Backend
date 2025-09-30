package com.example.signal.rmi;
import com.example.signal.utils.DBHelper;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.springframework.beans.factory.annotation.Autowired;

public class SignalController extends UnicastRemoteObject implements SignalControlInterface {
   /* @Autowired
   // private DBHelper dbHelper; */
    private int greenSignalGroup; // 1 for 1&2 | 2 for 3&4
    public  String state;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public SignalController() throws RemoteException {
        super();
        greenSignalGroup = 1; // Initially 1&2 is green
        state = "GREEN";
        System.out.println("SignalController started. 1 & 2 are GREEN.");

    //   dbHelper.saveControllerLog("1 & 2", "INITIAL GREEN");
        

    }

    @Override
    public String requestGreen(int signalNumber) throws RemoteException {
        return handleRequest(signalNumber, false, "system");
        
    }

    @Override
    public String manualOverride(int signalNumber) throws RemoteException {
        return handleRequest(signalNumber, true, "VIP");
    }

    private String handleRequest(int signalNumber, boolean vip, String requestedBy) throws RemoteException {
        CompletableFuture<String> response = new CompletableFuture<>();

        // Wrap request into a Runnable and push to RequestQueue
        RequestQueue.submitRequest(() -> {
            try {
                String result = changeSignal(signalNumber, vip, requestedBy);
                response.complete(result);
            } catch (Exception e) {
                response.completeExceptionally(e);
            }
        });

        try {
            return response.get(); // Block until result ready (transparency for manipulator)
        } catch (Exception e) {
            throw new RemoteException("Execution failed", e);
        }
    }

    private String changeSignal(int signalNumber, boolean vip, String requestedBy) throws RemoteException {
        lock.writeLock().lock();
        try {
            if (signalNumber == greenSignalGroup) {
                return "Signal " + (signalNumber == 1 ? "1 & 2" : "3 & 4") + " is already GREEN.";
            }

            String currentGroup = greenSignalGroup == 1 ? "1 & 2" : "3 & 4";
            String nextGroup = signalNumber == 1 ? "1 & 2" : "3 & 4";

            if (vip) {
                System.out.println("🚨 VIP override activated!");
            } else {
                System.out.println("Changing signal...");
            }

            System.out.println(currentGroup + " is now YELLOW");
            state = "YELLOW";
            try { Thread.sleep(5000); } catch (InterruptedException e) {}

            greenSignalGroup = signalNumber;
            state = "GREEN";
            System.out.println(currentGroup + " is now RED");
            System.out.println(nextGroup + " is now GREEN");

            // --- Log to databases ---
        //     dbHelper.saveControllerLog(nextGroup, (vip ? "VIP OVERRIDE" : "AUTO SWITCH"));
        //     dbHelper.saveManipulatorLog(nextGroup, (vip ? "VIP OVERRIDE" : "AUTO SWITCH"), requestedBy);
        //     dbHelper.savePedestrianLog(true, nextGroup);

            return (vip ? "VIP Override: " : "") + nextGroup + " is GREEN now.";
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public String getSignalStatus() throws RemoteException {
      lock.readLock().lock();
    try {
        if (state.equals("YELLOW")) {
            return greenSignalGroup == 1
                ? "Signal 1 & 2: YELLOW | Signal 3 & 4: RED"
                : "Signal 1 & 2: RED   | Signal 3 & 4: YELLOW";
            }

        // Normal green/red
        return greenSignalGroup == 1
            ? "Signal 1 & 2: GREEN | Signal 3 & 4: RED"
            : "Signal 1 & 2: RED   | Signal 3 & 4: GREEN";
    } finally {
        lock.readLock().unlock();
    }

}
}