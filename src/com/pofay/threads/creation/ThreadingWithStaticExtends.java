package com.pofay.threads.creation;

public class ThreadingWithStaticExtends {

    public static void main(String[] args) {
        final var thread = new WorkerThread();

        thread.start();
    }


    private static class WorkerThread extends Thread {

        @Override
        public void run() {
            System.out.println("Hello from " + Thread.currentThread().getName());
        }
    }

}
