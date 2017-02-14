package me.butzow.pixelbreak;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;


public class Application implements ImageForm.Callback {
    private ImageForm imageForm;

    public Application() {
        imageForm = new ImageForm(this);

        imageForm.show();
    }

    @Override
    public void saveImage(File file, double a, int b, double x, double y) {
        if (file == null) return;

        try {
            imageForm.saveImage(Breaker.breakImage(ImageIO.read(file), a, b, x, y));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateImage(File file, double a, int b, double x, double y) {
        if (file == null) return;

        try {
            imageForm.setImage(Breaker.breakImage(ImageIO.read(file), a, b, x, y));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}