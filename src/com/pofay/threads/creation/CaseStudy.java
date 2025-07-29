package com.pofay.threads.creation;

import java.util.List;
import java.util.Random;

public class CaseStudy {

    public static int MAX_PASSWORD = 9999;

    public static void main(String[] args) {

        final var random = new Random();
        final var currentPassword = random.nextInt(MAX_PASSWORD);
        System.out.println("Current Password: " + currentPassword);
        final var vault = new Vault(currentPassword);

        final var threads = List.of(
                new AscendingHackerThread(vault),
                new DescendingHackerThread(vault),
                new PoliceThread());

        for (var t : threads) {
            t.start();
        }
    }

    private static class Vault {
        private int password;

        public Vault(int password) {
            this.password = password;
        }

        public boolean isCorrectPassword(int guess) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
            }

            return password == guess;
        }
    }

    private static abstract class HackerThread extends Thread {
        protected Vault vault;

        public HackerThread(Vault vault) {
            this.vault = vault;
            this.setName(this.getClass().getSimpleName());
            this.setPriority(MAX_PRIORITY);
        }

        @Override
        public void start() {
            System.out.println("Starting thread: " + this.getName());
            super.start();
        }
    }

    private static class AscendingHackerThread extends HackerThread {

        public AscendingHackerThread(Vault vault) {
            super(vault);
        }

        @Override
        public void run() {
            for (var guess = 0; guess < MAX_PASSWORD; guess++) {
                if (vault.isCorrectPassword(guess)) {
                    System.out.println(this.getName() + " guessed the password " + guess);
                    System.exit(0);
                }
            }
        }
    }

    private static class DescendingHackerThread extends HackerThread {
        public DescendingHackerThread(Vault vault) {
            super(vault);
        }

        @Override
        public void run() {
            for (var guess = MAX_PASSWORD; guess >= 0; guess--) {
                if (vault.isCorrectPassword(guess)) {
                    System.out.println(this.getName() + " guessed the password " + guess);
                    System.exit(0);
                }
            }
        }
    }

    private static class PoliceThread extends Thread {

        public PoliceThread() {
            this.setName(this.getClass().getSimpleName());
        }

        @Override
        public void run() {
            System.out.println("Starting thread: " + this.getName());
            System.out.println("Catching the hackers in: ");
            for (var i = 10; i > 0; i--) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                System.out.println(i);
            }

            System.out.println("Game over hackers!!");
            System.exit(0);
        }
    }

}
