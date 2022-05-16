package com.floriang.iconchanger.iconchanger;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainApplication extends Application {
    public static List<File> selectedFolder = new ArrayList<>();
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(javafx.stage.Stage primaryStage) {
        TreeView<File> treeView = new TreeView<>();
        FileChooser fileChooser = new FileChooser();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        MenuBar menu = new MenuBar();
        Menu menuFile = new Menu("File");
        MenuItem menuItemOpen = new MenuItem("Open");
        menuFile.getItems().add(menuItemOpen);
        Menu menuHelp = new Menu("Help");
        MenuItem about = new MenuItem("About");
        File testFile;
        GridPane centerPane;

        menuHelp.getItems().add(about);
        menu.getMenus().addAll(menuFile, menuHelp);

        VBox menus = new VBox();
        menus.getChildren().addAll(menu);
        SplitPane splitPane = new SplitPane();
        if (IconConverter.isWindows) {
            testFile = new File("C://");
        } else {
            testFile = new File("/");
        }
        try {
            ImageView imageView = new ImageView((new Image("/folder.png")));
            imageView.setFitHeight(10);
            imageView.setFitWidth(10);
            treeView.setRoot(new SimpleFileTreeItem(testFile, imageView));
        } catch (Exception e) {
            e.printStackTrace();
        }
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.gridLinesVisibleProperty().set(true);
        centerPane = generateGrid(testFile);
        centerPane.gridLinesVisibleProperty().set(true);
        ScrollPane scrollPane = new ScrollPane(centerPane);
        scrollPane.setMinWidth(500);
        scrollPane.setMaxWidth(700);
        splitPane.getItems().addAll(treeView, scrollPane, gridPane);

        Label previewDescription = new Label("Preview");
        ImageView preview;
        try {
            preview = new ImageView(new Image("/folder.png"));
            preview.setFitWidth(100);
            preview.setFitHeight(100);
        } catch (Exception e) {
            System.out.println("No file found");
            preview = new ImageView(new Image("/folder.png"));
        }
        fileChooser.setTitle("Choose a file");
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("PNG", "*.png"));

        Button chooseFile = new Button("Choose a file");
        ImageView finalPreview = preview;
        chooseFile.setOnAction(event -> {
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                try {
                    finalPreview.setImage(new Image(file.toURI().toURL().toString()));
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }

        });

        treeView.onMouseClickedProperty().set(event -> {
            if (event.getClickCount() == 2) {
                File file = treeView.getSelectionModel().getSelectedItem().getValue();
                if (file.isDirectory()) {
                    treeView.setRoot(new SimpleFileTreeItem(file, new ImageView((new Image("/folder.png")))));
                    centerPane.getChildren().clear();
                    centerPane.getChildren().add(generateGrid(file));
                }
            }
        });

        Button submit = new Button("Submit");
        submit.setOnAction(event -> {
            ImageView iconImage = new ImageView(finalPreview.getImage());
            iconImage.setFitWidth(10);
            iconImage.setFitHeight(10);
            treeView.getSelectionModel().getSelectedItem().setGraphic(iconImage);
        });
        gridPane.add(previewDescription, 0, 0);
        gridPane.add(preview, 0, 1);
        gridPane.add(chooseFile, 1, 1);
        gridPane.add(submit, 1, 2);


        menuItemOpen.setOnAction(event -> {
            directoryChooser.setTitle("Open Folder");
            File file = directoryChooser.showDialog(primaryStage);
            if (file != null) {
                treeView.setRoot(new SimpleFileTreeItem(file));
                centerPane.getChildren().clear();
                centerPane.getChildren().addAll(generateGrid(file));
            }
        });

        BorderPane root = new BorderPane();
        root.setTop(menus);
        root.setCenter(splitPane);


        Scene scene = new Scene(root, 1280, 960);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Icon Changer");
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    public GridPane generateGrid(File file) {
        GridPane gridPane = new GridPane();
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            int j = 0;
            Image image = new Image(Objects.requireNonNull(getClass().getResource("/folder.png")).toString());
            for (int i = 0; i < Objects.requireNonNull(files).length; i++) {
                gridPane.add(new SimpleFolderGridItem(files[i], image), i % 3, j);
                if (i % 3 == 0) {
                    j++;
                }
            }
        }
        return gridPane;
    }

    public static void writeDesktopIni() {
        File Folder = new File("/Users/florian/test/");
        try {
            Wini ini;
            try {
                ini = new Wini(new File("/Users/florian/test/desktop.ini"));
            } catch (IOException ex) {
                new File("/Users/florian/test/desktop.ini");
                ini = new Wini(new File("/Users/florian/test/desktop.ini"));
            }
            if (ini.isEmpty()) {
                return;
            }
            String field = "/Users/florian/download/Folder.ico" + ",0";
            ini.put(".ShellClassInfo", "IconResource", field);
            ini.store();
            //Process processCreateFile = Runtime.getRuntime().exec("attrib +h +s " + "/Users/florian/test/desktop.ini");
            //Process processCreateFolder = Runtime.getRuntime().exec("attrib -h +s " + "/Users/florian/test/");

        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
}

