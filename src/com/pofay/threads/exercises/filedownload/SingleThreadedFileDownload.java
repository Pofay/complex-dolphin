package com.pofay.threads.exercises.filedownload;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

public class SingleThreadedFileDownload {

    // Main Download logic is from https://www.baeldung.com/java-download-file
    private static String FILE_URL = "https://milkov.tech/assets/psd.pdf";
    private static String FILE_NAME = "./resources/A Philosophy of Software Design.pdf";

    public static void main(String[] args) throws URISyntaxException, IOException {

        final var url = new URI(FILE_URL).toURL();
        final var httpConnection = (HttpURLConnection) url.openConnection();
        httpConnection.setRequestMethod("HEAD");
        final var fileSize = httpConnection.getContentLengthLong();

        long downloadedFileSize = 0;

        final var outputFile = new RandomAccessFile(FILE_NAME, "rw");
        outputFile.setLength(fileSize);
        outputFile.close();

        System.out.println("File size is: " + fileSize);

        long startTime = System.currentTimeMillis();

        try (final var urlInputStream = new BufferedInputStream(url.openStream());
                final var raf = new RandomAccessFile(FILE_NAME, "rw")) {
            byte dataBuffer[] = new byte[8192];
            int bytesRead;
            while ((bytesRead = urlInputStream.read(dataBuffer, 0, 1024)) != -1) {
                downloadedFileSize += bytesRead;
                final int currentProgress = (int) ((((double) downloadedFileSize) / ((double) fileSize))
                        * 100);

                raf.write(dataBuffer, 0, bytesRead);
                System.out.println("Download Progress: " + currentProgress + "%");
            }
            System.out.println("Done Downloading File.");
        } catch (IOException e) {
            // handle exception
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println(duration);
    }

}
