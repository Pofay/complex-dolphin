package com.pofay.threads.imageprocessing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class ImageProcessingMultiThreaded {

    public static final String SOURCE_FILE = "./resources/many-flowers.jpg";
    public static final String DESTINATION_FILE = "./out/many-flowers.jpg";

    public static void main(String[] args) throws IOException, InterruptedException {

        final var originalImage = ImageIO.read(new File(SOURCE_FILE));
        final var resultImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(),
                BufferedImage.TYPE_INT_RGB);

        long startTime = System.currentTimeMillis();
        int numberOfThreads = 6;

        recolorMultiThreaded(originalImage, resultImage, numberOfThreads);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        final var outputFile = new File(DESTINATION_FILE);
        ImageIO.write(resultImage, "jpg", outputFile);

        System.out.println(String.valueOf(duration));
    }

    public static void recolorMultiThreaded(BufferedImage original, BufferedImage result, int numberOfThreads) throws InterruptedException {
        final var threads = new ArrayList<Thread>();
        int width = original.getWidth();
        int height = original.getHeight() / numberOfThreads;

        for (int i = 0; i < numberOfThreads; i++) {
            final int threadMultiplier = i;

            final var thread = new Thread(() -> {
                int leftCorner = 0;
                int topCorner = height * threadMultiplier;
                recolorImage(original, result, leftCorner, topCorner, width, height);
            });

            threads.add(thread);
        }

        for (var thread : threads) {
            thread.start();
        }

        for (var thread : threads) {
            thread.join();
        }
    }

    public static void recolorImage(BufferedImage original, BufferedImage result, int leftCorner, int topCorner,
            int width, int height) {

        for (int x = leftCorner; x < leftCorner + width && x < original.getWidth(); x++) {
            for (int y = topCorner; y < topCorner + height && y < original.getWidth(); y++) {
                recolorPixel(original, result, x, y);
            }
        }

    }

    public static void recolorPixel(BufferedImage original, BufferedImage result, int x, int y) {
        int rgb = original.getRGB(x, y);

        int red = getRed(rgb);
        int green = getGreen(rgb);
        int blue = getBlue(rgb);

        int newRed;
        int newGreen;
        int newBlue;

        if (isShadeOfGrey(red, green, blue)) {
            newRed = Math.min(255, red + 10);
            newGreen = Math.max(0, green - 80);
            newBlue = Math.max(0, blue - 20);
        } else {
            newRed = red;
            newGreen = green;
            newBlue = blue;
        }

        int newRGB = createRGBFromColors(newRed, newGreen, newBlue);
        setRGB(result, x, y, newRGB);
    }

    public static void setRGB(BufferedImage image, int x, int y, int rgb) {
        final var rgbModel = image.getColorModel().getDataElements(rgb, null);
        image.getRaster().setDataElements(x, y, rgbModel);
    }

    public static int getBlue(int rgb) {
        return rgb & 0x000000FF;
    }

    public static int getGreen(int rgb) {
        return (rgb & 0x0000FF00) >> 8;
    }

    public static int getRed(int rgb) {
        return (rgb & 0x00FF0000) >> 16;
    }

    public static int createRGBFromColors(int red, int green, int blue) {
        int rgb = 0;

        rgb |= blue;
        rgb |= green << 8;
        rgb |= red << 16;

        rgb |= 0xFF000000;

        return rgb;
    }

    public static boolean isShadeOfGrey(int red, int green, int blue) {
        return Math.abs(red - green) < 30 && Math.abs(red - blue) < 30 && Math.abs(green - blue) < 30;
    }

}
