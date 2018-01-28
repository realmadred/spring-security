package com.feng.security.validate;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Random;

/**
 * 验证码生成器
 * lf
 */
public class ImageCodeGenerate implements Serializable {

    private static final long serialVersionUID = 825842396719617754L;

    private static final int DEFAULT_SIZE = 6; // 验证码位数
    private static final int HEIGHT = 40; // 验证码高度

    // 验证码字符串容器
    private static final String[] VERIFY_CODES = {"2","3","4","5","6","7","8","9",
            "A","B","C","D","E","F","G","H","J","K","L","M","N","P","Q","R","S","T",
            "U","V","W","X","Y","Z"};

    private static final Random random = new Random();

    private static final Color[] COLOR_SPACES = new Color[]{Color.WHITE, Color.CYAN,
            Color.GRAY, Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE,
            Color.PINK, Color.YELLOW};

    public static ImageCode generate() {
        String code = generateVerifyCode(DEFAULT_SIZE);
        return new ImageCode(generateImage(code),code);
    }

    /**
     * 生成验证码字符串
     * @param verifySize 验证码长度
     * @return
     */
    private static String generateVerifyCode(int verifySize) {
        StringBuilder verifyCode = new StringBuilder(verifySize);
        final int length = VERIFY_CODES.length;
        for (int i = 0; i < verifySize; i++) {
            verifyCode.append(VERIFY_CODES[(random.nextInt(length - 1))]);
        }
        return verifyCode.toString();
    }

    /**
     * 生成图片
     * @param code
     * @return
     */
    private static BufferedImage generateImage(String code) {
        int verifySize = code.length();
        final int width = HEIGHT * verifySize;
        BufferedImage image = new BufferedImage(width, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color[] colors = new Color[5];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = COLOR_SPACES[random.nextInt(COLOR_SPACES.length)];
        }
        g2.setColor(Color.GRAY);// 设置边框色
        g2.fillRect(0, 0, width, HEIGHT);

        Color c = getRandColor(200, 250);
        g2.setColor(c);// 设置背景色
        g2.fillRect(0, 2, width, HEIGHT - 4);

        //绘制干扰线
        Random random = new Random();
        g2.setColor(getRandColor(160, 200));// 设置线条的颜色
        for (int i = 0; i < 20; i++) {
            int x = random.nextInt(width - 1);
            int y = random.nextInt(HEIGHT - 1);
            int xl = random.nextInt(6) + 1;
            int yl = random.nextInt(12) + 1;
            g2.drawLine(x, y, x + xl + 40, y + yl + 20);
        }

        // 添加噪点
        float rate = 0.05f;// 噪声率
        int area = (int) (rate * width * HEIGHT);
        for (int i = 0; i < area; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(HEIGHT);
            int rgb = getRandomIntColor();
            image.setRGB(x, y, rgb);
        }

        shear(g2, width, HEIGHT, c);// 使图片扭曲

        g2.setColor(getRandColor(100, 160));
        int fontSize = HEIGHT - 4;
        Font font = new Font("Algerian", Font.ITALIC, fontSize);
        g2.setFont(font);
        char[] chars = code.toCharArray();
        for (int i = 0; i < verifySize; i++) {
            AffineTransform affine = new AffineTransform();
            affine.setToRotation(Math.PI / 4 * random.nextDouble() * (random.nextBoolean() ? 1 : -1), (width / verifySize) * i + fontSize / 2, HEIGHT / 2);
            g2.setTransform(affine);
            g2.drawChars(chars, i, 1, ((width - 10) / verifySize) * i + 5, HEIGHT / 2 + fontSize / 2 - 10);
        }
        g2.dispose();
        return image;
    }

    private static Color getRandColor(int fc, int bc) {
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    private static int getRandomIntColor() {
        int[] rgb = getRandomRgb();
        int color = 0;
        for (int c : rgb) {
            color = color << 8;
            color = color | c;
        }
        return color;
    }

    private static int[] getRandomRgb() {
        int[] rgb = new int[3];
        for (int i = 0; i < 3; i++) {
            rgb[i] = random.nextInt(255);
        }
        return rgb;
    }

    private static void shear(Graphics g, int w1, int h1, Color color) {
        shearX(g, w1, h1, color);
        shearY(g, w1, h1, color);
    }

    private static void shearX(Graphics g, int w1, int h1, Color color) {
        int period = random.nextInt(2);
        int frames = 1;
        int phase = random.nextInt(2);

        for (int i = 0; i < h1; i++) {
            double d = (double) (period >> 1)
                    * Math.sin((double) i / (double) period
                    + (6.2831853071795862D * (double) phase)
                    / (double) frames);
            g.copyArea(0, i, w1, 1, (int) d, 0);
            g.setColor(color);
            g.drawLine((int) d, i, 0, i);
            g.drawLine((int) d + w1, i, w1, i);
        }

    }

    private static void shearY(Graphics g, int w1, int h1, Color color) {
        int period = random.nextInt(HEIGHT) + 10; // 50;
        int frames = 20;
        int phase = 7;
        for (int i = 0; i < w1; i++) {
            double d = (double) (period >> 1)
                    * Math.sin((double) i / (double) period
                    + (6.2831853071795862D * (double) phase)
                    / (double) frames);
            g.copyArea(i, 0, 1, h1, 0, (int) d);
            g.setColor(color);
            g.drawLine(i, (int) d, i, 0);
            g.drawLine(i, (int) d + h1, i, h1);
        }
    }
}
