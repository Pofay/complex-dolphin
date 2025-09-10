package com.pofay.threads.throughput;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Executors;

public class Throughput {
    private static final String INPUT_FILE = "resources/war_and_peace.txt";
    private static final int THREAD_COUNT = 16;

    public static void main(String[] args) throws IOException {
        final var text = new String(Files.readAllBytes(Paths.get(INPUT_FILE)));
        startServer(text);
    }

    private static void startServer(String text) throws IOException {
        final var address = new InetSocketAddress(7000);
        final var server = HttpServer.create(address, 0);

        server.createContext("/search", new WordCountHandler(text));
        final var executor = Executors.newFixedThreadPool(THREAD_COUNT);
        server.setExecutor(executor);
        server.start();
    }

    private static class WordCountHandler implements HttpHandler {
        private String text;

        public WordCountHandler(String text) {
            this.text = text;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            final var query = exchange.getRequestURI().getQuery();
            final var keyValue = query.split("=");
            final var action = keyValue[0];
            final var word = keyValue[1];

            if (!action.equals("word")) {
                exchange.sendResponseHeaders(400, 0);
                return;
            }

            final var count = countWord(word);

            final var response = Long.toString(count).getBytes();
            exchange.sendResponseHeaders(200, response.length);
            final var outputStream = exchange.getResponseBody();
            outputStream.write(response);
            outputStream.close();
        }

        private long countWord(String word) {
            var count = 0;
            var index = 0;
            while (index >= 0) {
                index = text.indexOf(word, index);
                if (index >= 0) {
                    count++;
                    index++;
                }
            }
            return count;
        }
    }
}
