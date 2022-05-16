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
            if(new File(directory.getAbsolutePath()+"\\desktop.ini").createNewFile()){
                wini = new Wini(new File("D:\\Downloads\\demo\\IconChanger\\src\\main\\resources\\desktop.ini"));
                String field = "C:\\Users\\flori\\Desktop\\Folder.ico" + ",0";
                wini.put(".ShellClassInfo", "IconResource", field);
                wini.store();
            }else{
                wini = new Wini(new File("D:\\Downloads\\demo\\IconChanger\\src\\main\\resources\\desktop.ini"));
                String field = "C:\\Users\\flori\\Desktop\\Folder.ico" + ",0";
                wini.put(".ShellClassInfo", "IconResource", field);
                wini.store();
            }

            Process processCreateFile = Runtime.getRuntime().exec("attrib +h +s " + "D:\\Downloads\\demo\\IconChanger\\src\\main\\resources\\desktop.ini");
            Process processCreateFolder = Runtime.getRuntime().exec("attrib -h +s " + "D:\\Downloads\\demo\\IconChanger\\src\\main\\resources\\");
        } catch (Exception e) {
            System.out.println("Error");
        }
    }
}
