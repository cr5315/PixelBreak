package me.butzow.pixelbreak;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;

public class ImageForm implements ActionListener, ChangeListener {
    private JPanel rootPanel;
    private JLabel imageLabel;
    private JButton saveImageButton;
    private JButton loadImageButton;
    private JButton aboutButton;
    private JSpinner spinner1;
    private JSpinner spinner2;
    private JSpinner spinner3;
    private JSpinner spinner4;

    private Callback callback;
    private JFrame frame;
    private JFileChooser fileChooser;

    private final File picturesDir = new File(System.getProperty("user.home") + System.getProperty("file.separator") + "Pictures");

    public interface Callback {
        void updateImage(File file, double a, int b, double x, double y);
    }

    public ImageForm(Callback callback) {
        this.callback = callback;

        frame = new JFrame("Pixel Break");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);

        fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Images", ImageIO.getReaderFileSuffixes()));
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setCurrentDirectory(picturesDir);
    }

    public void saveImage(BufferedImage brokenImage) {
        if (brokenImage == null) return;

        File outDir = new File(picturesDir, "PixelBreak");
        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        String selectedFilename = "PixelBreak_" + fileChooser.getSelectedFile().getName();
        int extensionIndex = selectedFilename.lastIndexOf(".");
        selectedFilename = selectedFilename.substring(0, extensionIndex) + ".png";
        System.out.println(selectedFilename);

        try {
            File outFile = new File(outDir, selectedFilename);
            ImageIO.write(brokenImage, "png", outFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setImage(BufferedImage image) {
        if (image == null) return;

        int widthDiff = image.getWidth() - imageLabel.getWidth();
        int heightDiff = image.getHeight() - imageLabel.getHeight();

        if (heightDiff > 0 && widthDiff > 0) {
            int resizedWidth = image.getWidth() - widthDiff;
            int resizedHeight = image.getHeight() - heightDiff;
            imageLabel.setIcon(new ImageIcon(image.getScaledInstance(resizedWidth, resizedHeight, Image.SCALE_SMOOTH)));
        } else {
            imageLabel.setIcon(new ImageIcon(image));
        }
    }

    public void show() {
        saveImageButton.addActionListener(this);
        loadImageButton.addActionListener(this);
        aboutButton.addActionListener(this);


        spinner1.setModel(new SpinnerNumberModel(2.5, 0.1, 5.0, 0.1));
        spinner1.addChangeListener(this);

        spinner2.setModel(new SpinnerNumberModel(2, 1, 10, 1));
        spinner2.addChangeListener(this);

        spinner3.setModel(new SpinnerNumberModel(1.2, 0.1, 5.0, 0.1));
        spinner3.addChangeListener(this);

        spinner4.setModel(new SpinnerNumberModel(1.001, 0.001, 5.0, 0.001));
        spinner4.addChangeListener(this);

        frame.add(rootPanel);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loadImageButton) {
            showFileChooser();
        }  else if (e.getSource() == saveImageButton) {
            callback.updateImage(fileChooser.getSelectedFile(), (double) spinner1.getValue(), (int) spinner2.getValue(), (double) spinner3.getValue(), (double) spinner4.getValue());
        } else if (e.getSource() == aboutButton) {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI("http://github.com/cr5315/PixelBreak"));
                } catch (Exception ignored) {}
            }
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        callback.updateImage(fileChooser.getSelectedFile(), (double) spinner1.getValue(), (int) spinner2.getValue(), (double) spinner3.getValue(), (double) spinner4.getValue());
    }

    private void showFileChooser() {
        int result = fileChooser.showOpenDialog(rootPanel);

        if (result == JFileChooser.APPROVE_OPTION) {
            callback.updateImage(fileChooser.getSelectedFile(), (double) spinner1.getValue(), (int) spinner2.getValue(), (double) spinner3.getValue(), (double) spinner4.getValue());
        }
    }
}
