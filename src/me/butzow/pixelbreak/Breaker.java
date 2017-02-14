package me.butzow.pixelbreak;

import java.awt.image.BufferedImage;

public class Breaker {

    public static BufferedImage breakImage(BufferedImage image, double a, int b, double x, double y) {
        if (image == null) return null;

        int width = image.getWidth();
        int height = image.getHeight();
        int last = 0;
        int latest = 0;
        for (int i = 0; i < width; i++){
            for (int j = 0; j < height; j++){
                latest = image.getRGB(i, j);
                if (latest > (latest + last) / a){
                    image.setRGB(i, j, (latest << b));
                } else if (latest > (latest + last) / x){
                    image.setRGB(i, j, (latest *= y));
                }
                last = image.getRGB(i, j);
            }
        }

        return image;
    }
}