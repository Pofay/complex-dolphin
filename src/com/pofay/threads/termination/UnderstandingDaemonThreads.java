package com.pofay.threads.termination;

public class UnderstandingDaemonThreads {

    public static void main(String[] args) {
        final var t = new Thread(() -> {
            while (true) {
                System.out.println("Background Work...");
            }
        });

        t.setDaemon(true);
        t.start();
        System.out.println("Main finished.");
    }

}
