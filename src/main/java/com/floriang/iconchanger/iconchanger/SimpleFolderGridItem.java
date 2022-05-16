package com.floriang.iconchanger.iconchanger;

import javafx.geometry.Insets;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import org.ini4j.Wini;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class SimpleFolderGridItem extends Pane {
    File file;
    Image image;
    boolean isSelected = false;

    public SimpleFolderGridItem(File file, Image image) {
        this.file = file;
        this.image = image;
        ImageView imageView;
        try {
            if (getImageFromDesktopIni(file) == null) {
                imageView = new ImageView(image);
            } else {
                imageView = new ImageView(getImageFromDesktopIni(file));
            }
        } catch (Exception e) {
            imageView = new ImageView(image);
        }
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);
        Label label = new Label();
        label.setGraphic(imageView);
        label.setText(file.getName());
        label.setTextAlignment(TextAlignment.CENTER);
        label.setContentDisplay(ContentDisplay.TOP);
        getChildren().add(label);
        label.setPadding(new Insets(10, 10, 10, 10));
        setOnMouseClicked(event -> {
            System.out.println(event.toString());
            if (event.getButton() == MouseButton.PRIMARY) {
                setSelected(true);
                MainApplication.selectedFolder.add(file);
            } else if (event.getButton() == MouseButton.SECONDARY) {
                setSelected(false);
                MainApplication.selectedFolder.remove(file);
            }
        });
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
        if (selected) {
            backgroundProperty().set(new Background(new BackgroundFill(Color.BLUE, null, null)));
        } else {
            backgroundProperty().set(new Background(new BackgroundFill(Color.WHITE, null, null)));
        }
    }

    Image getImageFromDesktopIni(File file) throws IOException {
        if (file.isDirectory()) {
            if (new File(file.getAbsolutePath() + "\\desktop.ini").exists()) {
                Wini ini = new Wini(new File(file.getAbsolutePath() + "\\desktop.ini"));
                String pathToIcon = ini.get(".ShellClassInfo", "IconResource");
                return new Image(IconConverter.icoToPng(new File(pathToIcon)).getAbsolutePath());
            }

        }
        return null;
    }

}