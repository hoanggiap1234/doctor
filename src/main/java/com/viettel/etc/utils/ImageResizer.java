package com.viettel.etc.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageResizer {

    /**
     * Resizes an image by a percentage of original size (proportional).
     * @param outputImagePath Path to save the resized image
     * over the input image.
     * @throws IOException
     */
    public static void resizeImage(byte[] b, String outputImagePath) throws IOException {
        // reads input image
        File inputFile = new File(outputImagePath);
        try (FileOutputStream fos = new FileOutputStream(inputFile)) {
            fos.write(b);
            //fos.close(); There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
        }
        BufferedImage inputImage = ImageIO.read(inputFile);
        // calculate image with and height
        double percent = inputImage.getWidth() / Constants.IMAGE_WIDTH;
        int scaledWidth = (int) (inputImage.getWidth() * percent);
        int scaledHeight = (int) (inputImage.getHeight() * percent);

        // creates output image
        BufferedImage outputImage = new BufferedImage(scaledWidth,
                scaledHeight, inputImage.getType());

        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();

        // extracts extension of output file
        String formatName = outputImagePath.substring(outputImagePath
                .lastIndexOf(".") + 1);

        // writes to output file
        ImageIO.write(outputImage, formatName, new File(outputImagePath));
    }

    private double getPercent(BufferedImage img) {
        int width = img.getWidth();
        return width / Constants.IMAGE_WIDTH;


    }

}
