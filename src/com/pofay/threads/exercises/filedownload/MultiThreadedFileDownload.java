package com.pofay.threads.exercises.filedownload;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class MultiThreadedFileDownload {

    // Main Download logic is from https://www.baeldung.com/java-download-file
    private static String FILE_URL = "https://cdn.britannica.com/21/75121-050-8CF5E1DB/Bats-structures-organs-sound-frequencies-signals-contexts.jpg";
    private static String FILE_NAME = "./resources/Bat.jpg";

    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {

        final var url = new URI(FILE_URL).toURL();
        final var httpConnection = (HttpURLConnection) url.openConnection();
        httpConnection.setRequestMethod("HEAD");
        final var fileSize = httpConnection.getContentLengthLong();

        System.out.println("File size is: " + fileSize);

        final var outputFile = new RandomAccessFile(FILE_NAME, "rw");
        outputFile.setLength(fileSize);
        outputFile.close();

        int threadCount = 4;
        int chunkSize = (int) fileSize / threadCount;
        final var threads = new ArrayList<DownloadThread>();

        
        for (var i = 0; i < threadCount; i++) {
            long start = i * chunkSize;
            long end = (i == threadCount - 1) ? fileSize - 1 : (i + 1) * chunkSize - 1;
            threads.add(new DownloadThread(FILE_URL, FILE_NAME, start, end));
        }

        long startTime = System.currentTimeMillis();

        for (var thread : threads) {
            thread.start();
        }

        for (var thread : threads) {
            thread.join();
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println(duration);
    }

    public static class DownloadThread extends Thread {

        private String fileURL;
        private String outputPath;
        private long startByte;
        private long endingByte;

        public DownloadThread(String fileURL, String outputPath, long startingByte, long endingByte) {
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

                    byte dataBuffer[] = new byte[1024];
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
