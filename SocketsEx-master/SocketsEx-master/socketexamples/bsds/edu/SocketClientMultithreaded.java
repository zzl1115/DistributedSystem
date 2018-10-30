package socketexamples.bsds.edu;

/**
 *
 * @author Ian Gortan
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketClientMultithreaded {
    
    static CyclicBarrier barrier; 
    
    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
        String hostName;
        final int MAX_THREADS = 100;
        int port;

        if (args.length == 2) {
            hostName = args[0];
            port = Integer.parseInt(args[1]);
        } else {
            hostName = null;
            port = 12031;  // default port in SocketServer
        }
        barrier = new CyclicBarrier(MAX_THREADS);

        // TO DO create and start client threads
        ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);
        for (int i = 0; i < MAX_THREADS; i++) {
            SocketClientThread thread = new SocketClientThread(hostName, port, barrier);
            executor.execute(thread);
        }
        barrier.await();
        executor.shutdown();
    }
}
