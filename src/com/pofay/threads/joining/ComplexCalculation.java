package com.pofay.threads.joining;

import java.math.BigInteger;

public class ComplexCalculation {
    public BigInteger calculateResult(BigInteger base1, BigInteger power1, BigInteger base2, BigInteger power2) throws InterruptedException {
        BigInteger result;
        PowerCalculatingThread powerOne = new PowerCalculatingThread(base1, power1);
        PowerCalculatingThread powerTwo = new PowerCalculatingThread(base2, power2);

        powerOne.start();
        powerTwo.start();
        
        powerOne.join();
        powerTwo.join();
        
        BigInteger p1 = powerOne.getResult();
        BigInteger p2 = powerTwo.getResult();
        
        result = p1.add(p2);
        
        return result;
    }

    private static class PowerCalculatingThread extends Thread {
        private BigInteger result = BigInteger.ONE;
        private BigInteger base;
        private BigInteger power;
    
        public PowerCalculatingThread(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }
    
        @Override
        public void run() {
           /*
           Implement the calculation of result = base ^ power
           */
           BigInteger i = BigInteger.ONE;
           result = base;
           for(; power.compareTo(i) == 1; i = i.add(BigInteger.ONE)) {
               result = result.multiply(base);
           }
        }
    
        public BigInteger getResult() { return result; }
    }
}