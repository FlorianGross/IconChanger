package com.floriang.iconchanger.iconchanger;

import net.sf.image4j.codec.ico.ICODecoder;
import net.sf.image4j.codec.ico.ICOEncoder;

//import net.ifok.image.image4j.codec.ico.ICODecoder;
//import net.ifok.image.image4j.codec.ico.ICOEncoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

//https://zetcode.com/articles/javaico/
public class IconConverter {
    public static String iconDIr = System.getProperty("user.home") + "\\Pictures\\";

    public static File pngToIco(File file) throws IOException {
        System.out.println("pngToIco");
        System.out.println(file.getAbsolutePath());
        BufferedImage bi = ImageIO.read(file);
        new File(iconDIr + file.getName() + ".ico").createNewFile();
        System.out.println(iconDIr + file.getName() + ".ico");
        ICOEncoder.write(bi, new File(iconDIr + file.getName() + ".ico"));
        return new File(iconDIr + file.getName() + ".ico");
    }

    public static File icoToPng(File file) throws IOException {
        new File(System.getProperty("user.home") + "\\Pictures\\" + "Icons\\").mkdir();
        iconDIr = System.getProperty("user.home") + "\\Pictures\\" + "Icons\\";
        List<BufferedImage> images = ICODecoder.read(file);
        new File(iconDIr + file.getName() + ".png").createNewFile();
        ImageIO.write(images.get(0), "png", new File(iconDIr + file.getName() + ".png"));
        return new File(iconDIr + file.getName() + ".png");
    }

}
