package com.pofay.threads.exercises.filedownload;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

public class SingleThreadedFileDownload {

    // Main Download logic is from https://www.baeldung.com/java-download-file
    private static String FILE_URL = "https://cdn.britannica.com/21/75121-050-8CF5E1DB/Bats-structures-organs-sound-frequencies-signals-contexts.jpg";
    private static String FILE_NAME = "./resources/Bat.jpg";

    public static void main(String[] args) throws URISyntaxException, IOException {

        // For now print out the file size
        final var url = new URI(FILE_URL).toURL();
        final var httpConnection = (HttpURLConnection) url.openConnection();
        httpConnection.setRequestMethod("HEAD");

        final var fileSize = httpConnection.getContentLengthLong();

        System.out.println("File size is: " + fileSize);

        // Then download the file
        try (final var urlInputStream = new BufferedInputStream(url.openStream());
                FileOutputStream fileOutputStream = new FileOutputStream(FILE_NAME)) {
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = urlInputStream.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            // handle exception
        }

    }

}
