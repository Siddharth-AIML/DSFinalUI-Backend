package com.example.signal.rmi;
import java.util.concurrent.*;

public class RequestQueue {
    private static final int LIMIT = 10;  // Max 2 requests at once
    private static final BlockingQueue<Runnable> bufferQueue = new LinkedBlockingQueue<>();
    private static final ExecutorService mainExecutor = Executors.newFixedThreadPool(LIMIT);
    private static final ExecutorService cloneExecutor = Executors.newCachedThreadPool();

    public static void submitRequest(Runnable task) {
        ThreadPoolExecutor exec = (ThreadPoolExecutor) mainExecutor;

        if (exec.getActiveCount() <= 10) {
            // Main SignalController has capacity
            mainExecutor.submit(task);
            System.out.println("✅ Request sent to Main Controller. Active = " 
                               + exec.getActiveCount() + ", Buffer = " + bufferQueue.size());
       
       
 } else {
            System.out.println("⚠ Main SignalController full → buffering & sending to clone. Buffer size before = " 
                               + bufferQueue.size());
            bufferQueue.add(task);

            // Clone picks up requests and waits until main has space
            cloneExecutor.submit(() -> {
                try {
                    Runnable bufferedTask = bufferQueue.take();
                    // Wait until main has a free slot
                    while (exec.getActiveCount() >= LIMIT) {
                        Thread.sleep(1000);
                    }
                    mainExecutor.submit(bufferedTask);
                    System.out.println("🔄 Clone forwarded request back to Main Controller. Active = "
                                       + exec.getActiveCount() + ", Buffer left = " + bufferQueue.size());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    // --- Extra methods to monitor ---
    public static int getActiveCount() {
        return ((ThreadPoolExecutor) mainExecutor).getActiveCount();
    }

    public static int getBufferSize() {
        return bufferQueue.size();
    }
}
