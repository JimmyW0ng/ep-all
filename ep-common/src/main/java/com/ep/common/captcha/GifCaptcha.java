package com.ep.common.captcha;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import static com.ep.common.captcha.Randoms.num;

/**
 * <p>Gif验证码类</p>
 *
 * @author: wuhongjun
 * @version:1.0
 */
public class GifCaptcha extends Captcha {

    private static Random random = new Random();

    public GifCaptcha() {
    }

    public GifCaptcha(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public GifCaptcha(int width, int height, int len) {
        this(width, height);
        this.len = len;
    }

    public GifCaptcha(int width, int height, int len, Font font) {
        this(width, height, len);
        this.font = font;
    }

    /**
     * 关闭输入流
     *
     * @param in 输入流
     */
    public static void close(InputStream in) {
        if (in != null) {
            try {
                in.close();
            } catch (IOException ioex) {
                // ignore
            }
        }
    }

    /**
     * 关闭输出流
     *
     * @param out 输出流
     */
    public static void close(OutputStream out) {
        if (out != null) {
            try {
                out.flush();
            } catch (IOException ioex) {
                // ignore
            }
            try {
                out.close();
            } catch (IOException ioex) {
                // ignore
            }
        }
    }

    @Override
    public void out(OutputStream os) {
        try {
            GifEncoder gifEncoder = new GifEncoder();
            //生成字符
            gifEncoder.start(os);
            gifEncoder.setQuality(180);
            gifEncoder.setDelay(100);
            gifEncoder.setRepeat(0);
            BufferedImage frame;
            char[] rands = alphas();
            Color[] fontColor = new Color[len];
            for (int i = 0; i < len; i++) {
                fontColor[i] = new Color(20 + num(110), 20 + num(110), 20 + num(110));
            }
            for (int i = 0; i < len; i++) {
                frame = graphicsImage(fontColor, rands, i);
                gifEncoder.addFrame(frame);
                frame.flush();
            }
            gifEncoder.finish();
        } finally {
            close(os);
        }

    }

    /**
     * 画随机码图
     *
     * @param fontcolor 随机字体颜色
     * @param strs      字符数组
     * @param flag      透明度使用
     * @return BufferedImage
     */
    private BufferedImage graphicsImage(Color[] fontcolor, char[] strs, int flag) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        //或得图形上下文
        //Graphics2D g2d=image.createGraphics();
        Graphics2D g2d = (Graphics2D) image.getGraphics();
        //利用指定颜色填充背景
        g2d.setColor(new Color(255, 255, 224));
        g2d.fillRect(0, 0, width, height);
        AlphaComposite ac3;
        int h = height - ((height - font.getSize()) >> 1);
        int w = width / len;
        g2d.setFont(font);

        // 画100个噪点(颜色及位置随机)
        for (int i = 0; i < 100; i++) {
            // 随机颜色
            int rInt = random.nextInt(255);
            int gInt = random.nextInt(255);
            int bInt = random.nextInt(255);
            g2d.setColor(new Color(rInt, gInt, bInt));
            // 随机旋转角度
            int sAngleInt = random.nextInt(360);
            int eAngleInt = random.nextInt(360);
            // 随机位置
            int xInt = random.nextInt(this.width - 3);
            int yInt = random.nextInt(this.height - 2);
            // 随机大小
            int wInt = random.nextInt(6);
            int hInt = random.nextInt(6);
            g2d.fillArc(xInt, yInt, wInt, hInt, sAngleInt, eAngleInt);
        }
        for (int i = 0; i < len; i++) {
            int stringXInt = random.nextInt(2) - random.nextInt(2);
            int stringYint = random.nextInt(6) - random.nextInt(12);
            ac3 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getAlpha(flag, i));
            g2d.setComposite(ac3);
            g2d.setColor(fontcolor[i]);
            //g2d.drawOval(num(width), num(height), 5 + num(10), 5 + num(10));
            g2d.drawString(strs[i] + "", (width - (len - i) * w) + (w - font.getSize()) + 1 + stringXInt, h - 4 + stringYint);
            // 随机位置
            int xInt = random.nextInt(this.width - 3);
            int yInt = random.nextInt(this.height - 2);
            int xInt2 = random.nextInt(this.width);
            int yInt2 = random.nextInt(this.height);
            float strke = 2.5f - random.nextInt(2);
            g2d.setStroke(new BasicStroke(strke));
            g2d.drawLine(xInt, yInt, xInt2, yInt2);
        }
        g2d.dispose();
        return image;
    }

    /**
     * 获取透明度,从0到1,自动计算步长
     *
     * @return float 透明度
     */
    private float getAlpha(int i, int j) {
        int num = i + j;
        float r = (float) 1 / len, s = (len + 1) * r;
        return num > len ? (num * r - s) : num * r;
    }
}