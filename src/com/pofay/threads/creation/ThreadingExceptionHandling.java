package com.pofay.threads.creation;

public class ThreadingExceptionHandling {

    public static void main(String[] args) throws InterruptedException {
        final var thread = new Thread(() -> {
            throw new RuntimeException("Blam!");
        });

        thread.setName("Worker Thread");

        thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println("A critical error happened in thread " + t.getName() + " with the error being "
                        + e.getMessage());
            }
        });

        thread.start();
    }
}
