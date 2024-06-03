package com.example.merchstore.model;

import org.imgscalr.Scalr;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 02.06.2024
 */
public class ImageProcessor {

    public static byte[] getImageAsByteArray(String imagePath) throws IOException {
        ClassPathResource imgFile = new ClassPathResource(imagePath);
        try (InputStream inputStream = imgFile.getInputStream()) {
            return StreamUtils.copyToByteArray(inputStream);
        }
    }

    public static BufferedImage rotateImage(BufferedImage image, double angle) {
        double rads = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
        int w = image.getWidth();
        int h = image.getHeight();
        int newWidth = (int) Math.floor(w * cos + h * sin);
        int newHeight = (int) Math.floor(h * cos + w * sin);
        BufferedImage rotated = new BufferedImage(newWidth, newHeight, image.getType());
        Graphics2D g2d = rotated.createGraphics();
        AffineTransform at = new AffineTransform();
        at.translate((newWidth - w) / 2, (newHeight - h) / 2);
        at.rotate(rads, w / 2.0, h / 2.0);
        g2d.setTransform(at);
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        return rotated;
    }

    public static byte[] imageToByteArray(BufferedImage image, String formatName) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, formatName, stream);
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        return stream.toByteArray();
    }

    public static BufferedImage simpleResizeImage(BufferedImage originalImage, int targetWidth) throws Exception {
        return Scalr.resize(originalImage, targetWidth);
    }

    public static BufferedImage simpleResizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws Exception {
        return Scalr.resize(originalImage, targetWidth, targetHeight);
    }

    public static BufferedImage resizeAndCropImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws Exception {
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        // Calculate the scaling factor to maintain aspect ratio
        double scale = Math.max((double) targetWidth / originalWidth, (double) targetHeight / originalHeight);

        // Calculate the new dimensions
        int scaledWidth = (int) (scale * originalWidth);
        int scaledHeight = (int) (scale * originalHeight);

        // Resize the image
        BufferedImage resizedImage = Scalr.resize(originalImage, Scalr.Method.QUALITY, scaledWidth, scaledHeight);

        // Crop the image to the target size
        int cropX = (scaledWidth - targetWidth) / 2;
        int cropY = (scaledHeight - targetHeight) / 2;

        return Scalr.crop(resizedImage, cropX, cropY, targetWidth, targetHeight);
    }

}
