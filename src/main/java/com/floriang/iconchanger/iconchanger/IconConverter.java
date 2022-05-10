package com.floriang.iconchanger.iconchanger;

import net.sf.image4j.codec.ico.ICODecoder;
import net.sf.image4j.codec.ico.ICOEncoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

//https://zetcode.com/articles/javaico/
public class IconConverter {

    public static File pngToIco(File file) throws IOException {
        BufferedImage bi = ImageIO.read(file);
        ICOEncoder.write(bi, new File(file.getName() + ".ico"));
        return new File(file.getName() + ".ico");
    }

    public static File icoToPng(File file) throws IOException {
        List<BufferedImage> images = ICODecoder.read(file);
        ImageIO.write(images.get(0), "png", new File(file.getName() + ".png"));
        return new File(file.getName() + ".png");
    }

}
