package com.example.demo.qr;

import io.nayuki.qrcodegen.QrCode;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Objects;

public class GenerateQrCode {
    private static final String PATH_IMG = "src/main/resources/img/";
    private static final String IMG_NAME = "qr.png";
    private static final String IMG_TYPE = "png";


    public static String generateQrcode(String qrText) {
        QrCode qrCode = QrCode.encodeText(qrText, QrCode.Ecc.MEDIUM);
        BufferedImage img = toImage(qrCode, 4, 10);
        String qrResult = "";

        try {
            qrResult = convertToImage(img);
        } catch (IOException e) {
            throw new RuntimeException("QR Code failed to generate");
        }

        return qrResult;
    }

    public static BufferedImage toImage(QrCode qr, int scale, int border) {
        return toImage(qr, scale, border, 0xFFFFFF, 0x000000);
    }

    public static BufferedImage toImage(QrCode qr, int scale, int border, int lightColor, int darkColor) {
        Objects.requireNonNull(qr);
        if (scale <= 0 || border < 0) {
            throw new IllegalArgumentException("Value out of range");
        }
        if (border > Integer.MAX_VALUE / 2 || qr.size + border * 2L > Integer.MAX_VALUE / scale) {
            throw new IllegalArgumentException("Scale or border too large");
        }

        BufferedImage result = new BufferedImage((qr.size + border * 2) * scale, (qr.size + border * 2) * scale,
                                                 BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < result.getHeight(); y++) {
            for (int x = 0; x < result.getWidth(); x++) {
                boolean color = qr.getModule(x / scale - border, y / scale - border);
                result.setRGB(x, y, color ? darkColor : lightColor);
            }
        }
        return result;
    }

    private static String convertToImage(RenderedImage renderedImage) throws IOException {
        ImageIO.write(renderedImage, IMG_TYPE, new File(PATH_IMG + IMG_NAME));

        byte[] imageBytes = Files.readAllBytes(Paths.get(PATH_IMG + IMG_NAME));
        Base64.Encoder encoder = Base64.getEncoder();
        return "data:image/png;base64," + encoder.encodeToString(imageBytes);
    }
}

