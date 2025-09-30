package com.example.signal.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class LoadBalancer extends UnicastRemoteObject implements LoadBalancerInterface {

    private static class ControllerWrapper {
        final SignalControlInterface controller;
        final AtomicInteger pendingCount = new AtomicInteger(0);

        ControllerWrapper(SignalControlInterface c) {
            this.controller = c;
        }
    }

    private final List<ControllerWrapper> controllers = new ArrayList<>();
    private final ExecutorService exec = Executors.newCachedThreadPool();
    private final int threshold;

    public LoadBalancer(int initialControllers, int threshold) throws RemoteException {
        super();
        this.threshold = threshold;

        for (int i = 0; i < initialControllers; i++) {
            controllers.add(new ControllerWrapper(new SignalController()));
        }

        System.out.println("✅ LoadBalancer started with " + initialControllers + " controllers.");
    }

    private synchronized ControllerWrapper getBestController() throws RemoteException {
        ControllerWrapper best = controllers.stream()
                .min(Comparator.comparingInt(w -> w.pendingCount.get()))
                .orElseThrow(() -> new RemoteException("No controllers available"));

        if (best.pendingCount.get() < threshold) {
            return best;
        }

        System.out.println("⚠ All busy, creating new controller clone...");
        ControllerWrapper wrapper = new ControllerWrapper(new SignalController());
        controllers.add(wrapper);
        return wrapper;
    }

    @Override
    public String requestGreen(int signalNumber) throws RemoteException {
        ControllerWrapper wrapper = getBestController();
        wrapper.pendingCount.incrementAndGet();
        try {
            Future<String> f = exec.submit(() -> wrapper.controller.requestGreen(signalNumber));
            return f.get();
        } catch (Exception e) {
            throw new RemoteException("Execution failed", e);
        } finally {
            wrapper.pendingCount.decrementAndGet();
        }
    }

    @Override
    public String manualOverride(int signalNumber) throws RemoteException {
        ControllerWrapper wrapper = getBestController();
        wrapper.pendingCount.incrementAndGet();
        try {
            Future<String> f = exec.submit(() -> wrapper.controller.manualOverride(signalNumber));
            return f.get();
        } catch (Exception e) {
            throw new RemoteException("Execution failed", e);
        } finally {
            wrapper.pendingCount.decrementAndGet();
        }
    }

    @Override
    public String getGlobalStatus() throws RemoteException {
        StringBuilder sb = new StringBuilder();
        int idx = 1;
        for (ControllerWrapper w : controllers) {
            sb.append("Controller-").append(idx++)
              .append(" pending=").append(w.pendingCount.get())
              .append(" status=").append(w.controller.getSignalStatus())
              .append("\n");
        }
        sb.append("📊 Queue Active=").append(RequestQueue.getActiveCount())
          .append(" | Buffer=").append(RequestQueue.getBufferSize());
        return sb.toString();
    }
}
