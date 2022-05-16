package com.floriang.iconchanger.iconchanger;

import javafx.geometry.Insets;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.TextAlignment;

import java.io.File;

public class SimpleFolderGridItem extends Pane {
    File file;
    Image image;

    public SimpleFolderGridItem(File file, Image image) {
        this.file = file;
        this.image = image;
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);
        Label label = new Label();
        label.setGraphic(imageView);
        label.setText(file.getName());
        label.setTextAlignment(TextAlignment.CENTER);
        label.setContentDisplay(ContentDisplay.TOP);
        getChildren().add(label);
        label.setPadding(new Insets(10, 10, 10, 10));
    }


}