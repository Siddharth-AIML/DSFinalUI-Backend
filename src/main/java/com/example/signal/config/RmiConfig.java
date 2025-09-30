package com.example.signal.config;

import com.example.signal.rmi.LoadBalancer;
import com.example.signal.rmi.SignalControlInterface;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

@Configuration
public class RmiConfig {

    @Bean
    public LoadBalancer rmiService() throws Exception {
        System.setProperty("java.rmi.server.hostname", "localhost");

        try {
            // Try to connect to existing registry
            Registry registry = LocateRegistry.getRegistry(1099);
            registry.list();
            System.out.println("RMI registry already running on port 1099");
        } catch (RemoteException e) {
            // If not running, create new one
            LocateRegistry.createRegistry(1099);
            System.out.println("✅ Created new RMI registry on port 1099");
        }

        // Start LoadBalancer with 1 SignalController, threshold = 5
        LoadBalancer lb = new LoadBalancer(1, 5);

        // Bind LoadBalancer instead of SignalController
        Naming.rebind("rmi://localhost:1099/SignalService", lb);
        System.out.println("✅ LoadBalancer registered as RMI service at rmi://localhost:1099/SignalService");

        // Return as Spring bean for @Autowired usage
        return lb;
    }
}

/*package com.example.signal.config;

import com.example.signal.rmi.SignalController;
import com.example.signal.rmi.SignalControlInterface;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

@Configuration
public class RmiConfig {

    @Bean
    public SignalControlInterface rmiService() throws Exception {
        System.setProperty("java.rmi.server.hostname", "localhost");

        try {
            Registry registry = LocateRegistry.getRegistry(1099);
            registry.list();
            System.out.println("RMI registry already running on 1099");
        } catch (RemoteException e) {
            LocateRegistry.createRegistry(1099);
            System.out.println("Created RMI registry on 1099");
        }

        SignalController controller = new SignalController();
        Naming.rebind("rmi://localhost/SignalService", controller);
        System.out.println("🚦 RMI Service bound at rmi://localhost/SignalService");

        return controller;
    }
}*/
