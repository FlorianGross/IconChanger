package com.floriang.iconchanger.iconchanger;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.TextAlignment;

import java.io.File;

public class SimpleFolderGridItem extends Label {
    File file;
    Image image;

    public SimpleFolderGridItem(File file, Image image) {
        this.file = file;
        this.image = image;
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);
        setGraphic(imageView);
        setText(file.getName());
        setTextAlignment(TextAlignment.CENTER);
        setContentDisplay(ContentDisplay.TOP);
    }
}