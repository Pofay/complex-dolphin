package com.pofay.threads.exercises.filedownload;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorsFileDownload {

    // Main Download logic is from https://www.baeldung.com/java-download-file
    private static String FILE_URL = "https://milkov.tech/assets/psd.pdf";
    private static String FILE_NAME = "./resources/A Philosophy of Software Design.pdf";

    public static void main(String[] args)
            throws URISyntaxException, IOException, InterruptedException, ExecutionException {

        final var url = new URI(FILE_URL).toURL();
        final var httpConnection = (HttpURLConnection) url.openConnection();
        httpConnection.setRequestMethod("HEAD");
        final var fileSize = httpConnection.getContentLengthLong();

        System.out.println("File size is: " + fileSize);

        final var outputFile = new RandomAccessFile(FILE_NAME, "rw");
        outputFile.setLength(fileSize);
        outputFile.close();

        int threadCount = 4;
        final var executor = Executors.newFixedThreadPool(threadCount);
        final var futures = new ArrayList<Future<?>>();
        int chunkSize = (int) fileSize / threadCount;

        long startTime = System.currentTimeMillis();

        for (var i = 0; i < threadCount; i++) {
            long start = i * chunkSize;
            long end = (i == threadCount - 1) ? fileSize - 1 : (i + 1) * chunkSize - 1;
            final var downloadTask = new DownloadTask(FILE_URL, FILE_NAME, start, end);
            futures.add(executor.submit(downloadTask));
        }

        for (var future : futures) {
            future.get();
        }

        executor.shutdown();
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println(duration);
    }

    public static class DownloadTask implements Runnable {

        private String fileURL;
        private String outputPath;
        private long startByte;
        private long endingByte;

        public DownloadTask(String fileURL, String outputPath, long startingByte, long endingByte) {
            this.fileURL = fileURL;
            this.outputPath = outputPath;
            this.startByte = startingByte;
            this.endingByte = endingByte;
        }

        @Override
        public void run() {
            try {
                final var url = new URI(fileURL).toURL();
                final var httpConnection = (HttpURLConnection) url.openConnection();
                final var chunkRange = String.format("bytes=%d-%d", startByte, endingByte);
                final var expectedBytes = endingByte - startByte + 1;
                httpConnection.setRequestProperty("Range", chunkRange);

                long totalBytesRead = 0;

                try (final var urlInputStream = new BufferedInputStream(httpConnection.getInputStream());
                        final var raf = new RandomAccessFile(outputPath, "rw")) {
                    raf.seek(startByte);

                    byte dataBuffer[] = new byte[8192];
                    int bytesRead;
                    while ((bytesRead = urlInputStream.read(dataBuffer)) != -1) {
                        raf.write(dataBuffer, 0, bytesRead);
                        totalBytesRead += bytesRead;
                        if (totalBytesRead >= expectedBytes) {
                            break;
                        }
                    }
                }
                httpConnection.disconnect();
            } catch (IOException e) {
                // handle exception
            } catch (URISyntaxException e) {

            }
        }
    }
}
