package com.floriang.iconchanger.iconchanger;

//import net.sf.image4j.codec.ico.ICODecoder;
//import net.sf.image4j.codec.ico.ICOEncoder;

//import net.ifok.image.image4j.codec.ico.ICODecoder;
//import net.ifok.image.image4j.codec.ico.ICOEncoder;
import net.sf.image4j.codec.ico.ICODecoder;
import net.sf.image4j.codec.ico.ICOEncoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

//https://zetcode.com/articles/javaico/
public class IconConverter {
    public static String os = System.getProperty("os.name").toLowerCase();
    public static boolean isWindows = os.contains("win");
    public static String iconDIr = "C:\\Users\\floriang\\Desktop\\icons\\";
    public static File pngToIco(File file) throws IOException {
        BufferedImage bi = ImageIO.read(file);
        ICOEncoder.write(bi, new File(iconDIr + file.getName() + ".ico"));
        return new File(iconDIr + file.getName() + ".ico");
    }

    public static File icoToPng(File file) throws IOException {
        List<BufferedImage> images = ICODecoder.read(file);
        ImageIO.write(images.get(0), "png", new File(iconDIr + file.getName() + ".png"));
        return new File(iconDIr + file.getName() + ".png");
    }

}
