package com.pofay.threads.joining;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

public class ExampleJoinThreads {

    public static void main(String[] args) throws InterruptedException {
        final var inputNumbers = Arrays.asList(1000000L, 23L, 0L);
        final var factorialThreads = new ArrayList<FactorialThread>();

        for (final var inputNumber : inputNumbers) {
            factorialThreads.add(new FactorialThread(inputNumber));
        }

        // To resolve the race condition in the previous example
        // After starting...
        for (final var factorialThread : factorialThreads) {
            factorialThread.start();
        }

        // We join the threads back to the main thread
        for (final var factorialThread: factorialThreads) {
            // factorialThread.join();

            // In case where computations do take a long time
            // (Especially for the element with 1000000L)
            // We can add a timeout for execution in each thread 
            // If it goes beyond this (2 seconds) then the thread 
            // terminates and its results back to the main thread.
            factorialThread.join(2000);
        }

        // So the main thread can now see the results of each thread
        // after they have finished executing.
        for (var i = 0; i < inputNumbers.size(); i++) {
            final var factorialThread = factorialThreads.get(i);
            if (factorialThread.isFinished()) {
                System.out.println("Factorial of " + inputNumbers.get(i) + " is " + factorialThread.getResult());
            } else {
                System.out.println("The calculation for " + inputNumbers.get(i) + " is still in progress");
            }
        }
        // Failsafe, the 1000000L's thread is a normal thread
        // so even if it rejoins its still alive somewhere
        // So after the join and all that we just exit.
        System.exit(0);
    }

    private static class FactorialThread extends Thread {
        private long inputNumber;
        private BigInteger result = BigInteger.ZERO;
        private boolean isFinished = false;

        public FactorialThread(long inputNumber) {
            this.inputNumber = inputNumber;
        }

        @Override
        public void run() {
            this.result = factorial(inputNumber);
            this.isFinished = true;
        }

        public BigInteger factorial(long n) {
            BigInteger tempResult = BigInteger.ONE;
            for (long i = n; i > 0; i--) {
                tempResult = tempResult.multiply(new BigInteger(Long.toString(i)));
            }
            return tempResult;
        }

        public boolean isFinished() {
            return isFinished;
        }

        public BigInteger getResult() {
            return result;
        }
    }

}
