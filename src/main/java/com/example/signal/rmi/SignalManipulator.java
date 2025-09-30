package com.example.signal.rmi;
import java.rmi.Naming;
import java.util.Random;
import java.util.Scanner;

public class SignalManipulator {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java SignalManipulator <server_ip_address>");
            return;
        }
        String serverIp = args[0];

        try {
            String url = "rmi://localhost/SignalService";
            SignalControlInterface stub = (SignalControlInterface) Naming.lookup(url);

            Random rand = new Random();
            Scanner sc = new Scanner(System.in);

            System.out.println("Connected to Signal Controller.");
            System.out.println("Type 'vip 1' or 'vip 2' to override for VIP cars.");
            System.out.println("Press Enter for normal random switching.");
            System.out.println("--------------------------------------------------");

            while (true) {
                System.out.print("Enter command (vip <1|2> / Enter for random): ");
                String input = sc.nextLine().trim();

                String result;
                if (input.startsWith("vip")) {
                    int group = Integer.parseInt(input.split(" ")[1]);
                    result = stub.manualOverride(group);
                    System.out.println("VIP Override Request Sent: " + result);

                    // Log in manipulator DB
                    //DBHelper.saveManipulatorLog((group == 1 ? "1 & 2" : "3 & 4"), "VIP OVERRIDE", "operator");

                } else {
                    int signalGroup = rand.nextBoolean() ? 1 : 2;
                    String signalPair = (signalGroup == 1) ? "1 & 2" : "3 & 4";

                    System.out.println("Random Request: Asking " + signalPair + " to turn GREEN.");
                    result = stub.requestGreen(signalGroup);
                    System.out.println(" Server Response: " + result);

                    // Log in manipulator DB
                    //DBHelper.saveManipulatorLog(signalPair, "AUTO SWITCH REQUEST", "system");
                }

                System.out.println("📡 Current Signal Status: " + stub.getSignalStatus());
                System.out.println("--------------------------------------------------");

                Thread.sleep(5000);
            }

        } catch (Exception e) {
            System.err.println("❌ Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
