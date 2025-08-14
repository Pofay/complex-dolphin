package com.pofay.threads.exercises.filedownload;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class SingleThreadedFileDownload {

    // Main Download logic is from https://www.baeldung.com/java-download-file
    private static String FILE_URL = "https://drive.google.com/file/d/13QoKVFLz_SjSRXhuieXg8EnZn2bs_wNL/view?usp=drive_link";
    
    public static void main(String[] args) throws URISyntaxException, IOException {

        // For now print out the file size 
        final var url = new URI(FILE_URL).toURL();
        final var httpConnection = (HttpURLConnection) url.openConnection();
        httpConnection.setRequestMethod("HEAD");

        final var fileSize = httpConnection.getContentLengthLong();

        System.out.println("File size is: " + fileSize);

        // Then download the file

    }

}
