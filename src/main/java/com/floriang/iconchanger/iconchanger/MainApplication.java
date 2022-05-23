package com.floriang.iconchanger.iconchanger;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.ini4j.Wini;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainApplication extends Application {
    public static List<File> selectedFolder = new ArrayList<>();
    public static Map<File, Image> iconMap = new java.util.HashMap<>();
    public static List<Image> prevUsedFolders = new ArrayList<>();
    private ImageView finalPreview;

    public static Image selectedImageFile;

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
        selectedImageFile = new Image("/folder.png");

        menuHelp.getItems().add(about);
        menu.getMenus().addAll(menuFile, menuHelp);

        VBox menus = new VBox();
        menus.getChildren().addAll(menu);
        SplitPane splitPane = new SplitPane();
        testFile = new File(System.getProperty("user.home"));
        try {
            ImageView imageView = new ImageView((new Image("/folder.png")));
            imageView.setFitHeight(10);
            imageView.setFitWidth(10);
            treeView.setRoot(new SimpleFileTreeItem(testFile, imageView));
        } catch (Exception e) {
            e.printStackTrace();
        }
        BorderPane rightPane = new BorderPane();
        rightPane.setPrefWidth(600);
        rightPane.setPadding(new Insets(10, 10, 10, 10));
        centerPane = generateGrid(testFile);
        centerPane.gridLinesVisibleProperty().set(true);
        ScrollPane scrollPane = new ScrollPane(centerPane);
        scrollPane.setMinWidth(500);
        scrollPane.setMaxWidth(700);

        ImageView preview;
        try {
            preview = new ImageView(new Image("/folder.png"));
            preview.setFitWidth(100);
            preview.setFitHeight(100);
        } catch (Exception e) {
            preview = new ImageView(new Image("/folder.png"));
        }

        fileChooser.setTitle("Choose a file");
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("PNG", "*.png"));

        Button chooseFile = new Button("Choose a file");
        finalPreview = preview;

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
        submit.setPrefWidth(rightPane.getPrefWidth());
        chooseFile.setPrefWidth(rightPane.getPrefWidth() / 2);

        submit.setOnAction(event -> {
            try {
                writeDesktopIni(selectedFolder.get(0), IconConverter.pngToIco(new File(selectedImageFile.getUrl())).getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
            ImageView iconImage = new ImageView(selectedImageFile);
            iconImage.setFitWidth(10);
            iconImage.setFitHeight(10);
            try {
                treeView.getSelectionModel().getSelectedItem().setGraphic(iconImage);
            } catch (Exception e) {
                System.out.println("No file selected");
            }
            for (File file : selectedFolder) {
                iconMap.put(file, finalPreview.getImage());
            }
            selectedFolder.clear();
            centerPane.getChildren().clear();
            centerPane.getChildren().addAll(generateGrid(testFile));
        });

        rightPane.setTop(new Label("Ändern Sie ein Icon oder laden Sie ein neues hoch"));
        GridPane gridPane = new GridPane();
        gridPane.gridLinesVisibleProperty().set(true);
        gridPane.setPadding(new Insets(50, 10, 50, 10));
        gridPane.add(preview, 0, 0);
        gridPane.add(chooseFile, 1, 0);
        GridPane usedGrid = generateUsedGrid(prevUsedFolders);
        usedGrid.gridLinesVisibleProperty().set(true);
        rightPane.setCenter(new VBox(gridPane, usedGrid));
        rightPane.setBottom(submit);

        menuItemOpen.setOnAction(event -> {
            directoryChooser.setTitle("Open Folder");
            File file = directoryChooser.showDialog(primaryStage);
            if (file != null) {
                treeView.setRoot(new SimpleFileTreeItem(file));
                centerPane.getChildren().clear();
                centerPane.getChildren().addAll(generateGrid(file));
            }
        });

        chooseFile.setOnAction(event -> {
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                try {
                    selectedImageFile = new Image(file.toURI().toURL().toString());
                    finalPreview.setImage(selectedImageFile);
                    prevUsedFolders.add(selectedImageFile);
                    usedGrid.getChildren().clear();
                    usedGrid.getChildren().addAll(generateUsedGrid(prevUsedFolders));
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }

        });
        splitPane.getItems().addAll(treeView, scrollPane, rightPane);
        BorderPane root = new BorderPane();
        root.setTop(menus);
        root.setCenter(splitPane);


        Scene scene = new Scene(root, 1280, 960);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Icon Changer");
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    private GridPane generateUsedGrid(List<Image> prevUsedFolders) {
        GridPane gridPane = new GridPane();
        int j = 0;
        for (int i = 0; i < prevUsedFolders.size(); i++) {
            Image imageFile = prevUsedFolders.get(i);
            ImageView image = new ImageView(imageFile);
            image.setFitWidth(100);
            image.setFitHeight(100);
            image.onMouseClickedProperty().set(event -> {
                selectedImageFile = imageFile;
                finalPreview.setImage(imageFile);
            });
            gridPane.add(image, i % 3, j);
        }
        gridPane.setGridLinesVisible(true);
        return gridPane;
    }

    public GridPane generateGrid(File file) {
        GridPane gridPane = new GridPane();
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            int j = 0, it = 0;
            for (int i = 0; i < Objects.requireNonNull(files).length; i++) {
                if (files[i].isDirectory()) {
                    File imageFile = printFolderImagePath(files[i]);
                    Image image;
                    if (imageFile == null) {
                        image = new Image(Objects.requireNonNull(getClass().getResource("/folder.png")).toString());
                    } else {
                        try {
                            File iconImage = IconConverter.icoToPng(imageFile);
                            image = new Image(iconImage.toURI().toString());
                        } catch (Exception e) {
                            System.out.println("No icon found " + e);
                            image = new Image(Objects.requireNonNull(getClass().getResource("/folder.png")).toString());
                        }
                    }
                    if (iconMap.containsKey(files[i])) {
                        image = iconMap.get(files[i]);
                    }
                    gridPane.add(new SimpleFolderGridItem(files[i], image), it++ % 3, j);
                    if (it % 3 == 0) {
                        j++;
                    }
                }
            }
        }
        return gridPane;
    }

    public static File printFolderImagePath(File directory) {
        Wini wini;
        try {
            if (new File(directory.getAbsolutePath() + "\\desktop.ini").exists()) {
                wini = new Wini(new File(directory.getAbsolutePath() + "\\desktop.ini"));
                String path = wini.get(".ShellClassInfo", "IconResource").split(",")[0];
                File file = new File(path);
                return file;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static void writeDesktopIni(File directoryPath, String iconPath) {
        try {
            Wini ini;
            if (directoryPath.isDirectory()) {
                new File(directoryPath.getAbsolutePath() + "\\desktop.ini").createNewFile();

                ini = new Wini(new File(directoryPath.getAbsolutePath() + "\\desktop.ini"));

                if (ini.isEmpty()) {
                    return;
                }
                String field = iconPath + ",0";
                ini.put(".ShellClassInfo", "IconResource", field);
                ini.store();
                Process processCreateFile = Runtime.getRuntime().exec("attrib +h +s " + directoryPath.getAbsolutePath() + "\\desktop.ini");
                Process processCreateFolder = Runtime.getRuntime().exec("attrib -h +s " + directoryPath.getAbsolutePath() + "\\desktop.ini");
            }
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
}

