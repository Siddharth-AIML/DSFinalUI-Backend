package com.example.signal.rmi;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.Naming;

public class SignalServer {
    public static void main(String[] args) {
        try {
            // Start the registry on port 1099 in code
            LocateRegistry.createRegistry(1099);

            SignalController controller = new SignalController();
            Naming.rebind("rmi://localhost/SignalService", controller);

            System.out.println("Traffic Signal Server is running...");
        } catch (Exception e) {
            System.out.println("Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
