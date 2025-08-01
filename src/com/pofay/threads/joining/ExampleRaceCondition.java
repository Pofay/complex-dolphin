package com.pofay.threads.joining;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

public class ExampleRaceCondition {

    public static void main(String[] args) {
        final var inputNumbers = Arrays.asList(0L, 3435L, 35435L, 2324L, 4656L, 23L, 5556L);
        final var factorialThreads = new ArrayList<FactorialThread>();

        for (final var inputNumber : inputNumbers) {
            factorialThreads.add(new FactorialThread(inputNumber));
        }

        // A Race happens here because the results of these threads
        // are competing with the main thread
        for (final var factorialThread : factorialThreads) {
            factorialThread.start();
        }

        // This is where the threads are competing against the main thread
        // The main thread can either see the factorial threads as still in progress 
        // even though its finished or see it finished.
        for (var i = 0; i < inputNumbers.size(); i++) {
            final var factorialThread = factorialThreads.get(i);
            if (factorialThread.isFinished()) {
                System.out.println("Factorial of " + inputNumbers.get(i) + " is " + factorialThread.getResult());
            } else {
                System.out.println("The calculation for " + inputNumbers.get(i) + " is still in progress");
            }
        }
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
            var tempResult = BigInteger.ONE;
            for (var i = n; i > 0; i--) {
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
