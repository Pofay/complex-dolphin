public class ThreadingExample {

    public static void main(String[] args) throws InterruptedException {
        final var thread = new Thread(() -> {
            // Code that will run in a new thread
            System.out.println("Currently we are in thread: " + Thread.currentThread().getName());
            System.out.println(Thread.currentThread().getName() + " thread priority: " + Thread.currentThread().getPriority());
        });

        thread.setName("Worker Thread");
        thread.setPriority(Thread.MAX_PRIORITY);

        thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println("A critical error happened in thread " + t.getName() + " with the error being " + e.getMessage());
            }
        });

        System.out.println("Currently we are in thread: " + Thread.currentThread().getName() + " before starting a new thread");
        thread.start();
        System.out.println("Currently we are in thread: " + Thread.currentThread().getName() + " after starting a new thread");

        Thread.sleep(10000);
    }

}
