package com.pofay.threads.termination;

public class BlockingTaskExample {

    public static void main(String[] args) {
        final var blockingThread = new Thread(new BlockingTask());
        blockingThread.setName("Blocking Thread");

        blockingThread.start();
        
        blockingThread.interrupt();
    }


    private static class BlockingTask implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(50000);
            } catch (InterruptedException e) {
                System.out.println("Exiting blocking thread.");
            }
        }
    }
}
