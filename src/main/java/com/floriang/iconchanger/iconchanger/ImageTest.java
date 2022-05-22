package com.floriang.iconchanger.iconchanger;

import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;

public class ImageTest {

    public static void main(String[] args) {
        File directory = new File("D:\\Downloads\\demo\\IconChanger\\src\\main\\resources");
        if (directory.isDirectory()) {
            printFolderImagePath(directory);
        }
    }

    private static void printFolderImagePath(File directory) {
        Wini wini;
        try {
            if(new File(directory.getAbsolutePath()+ "\\desktop.ini").exists()){
                wini = new Wini(new File(directory.getAbsolutePath()+ "\\desktop.ini"));
                System.out.println(wini.get(".ShellClassInfo", "IconResource"));
            }else{
                System.out.println("No desktop.ini found");
            }
        } catch (Exception e) {
            System.out.println("Error");
        }
    }
}
