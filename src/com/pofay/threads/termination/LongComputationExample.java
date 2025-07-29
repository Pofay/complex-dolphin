package com.pofay.threads.termination;

import java.math.BigInteger;

public class LongComputationExample {

    public static void main(String[] args) throws InterruptedException {
        final var longComputation = new LongComputationTask(new BigInteger("200000"), new BigInteger("1000000000"));
        final var thread = new Thread(longComputation);
        thread.setName("Long Computation Thread");

        thread.setDaemon(true);
        thread.start();
        Thread.sleep(100);
        thread.interrupt();
    }

    private static class LongComputationTask implements Runnable {

        private final BigInteger base;
        private final BigInteger power;

        public LongComputationTask(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }

        @Override
        public void run() {
            System.out.println(base + "^" + power + " = " + pow(base, power));
        }

        private BigInteger pow(BigInteger base, BigInteger power) {
            var result = BigInteger.ONE;
            for (var i = BigInteger.ZERO; i.compareTo(power) != 0; i = i.add(BigInteger.ONE)) {
                if(Thread.currentThread().isInterrupted()) {
                    System.out.println("Prematurely interrupted computation");
                    return BigInteger.ZERO;
                }
                result = result.multiply(base);
            }
            return result;
        }

    }

}
