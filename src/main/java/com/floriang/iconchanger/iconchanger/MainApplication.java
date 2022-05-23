package com.floriang.iconchanger.iconchanger;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;

public class MainApplication extends Application {
    public static List<File> selectedFolder = new ArrayList<>();
    public static Map<File, Image> iconMap = new java.util.HashMap<>();
    public static List<Image> prevUsedFolders = new ArrayList<>();
    private ImageView finalPreview;
    public static File selectedImageFile;
    public static GridPane centerPane;
    public static TreeView<File> treeView;
    public static File rootFile;

    private static GridPane usedGrid;
    private static FileChooser fileChooser;
    private static DirectoryChooser directoryChooser;

    private static javafx.stage.Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(javafx.stage.Stage primaryStage) {
        stage = primaryStage;
        BorderPane root = createBody();
        Scene scene = new Scene(root, 1280, 960);
        primaryStage.setScene(scene);
        primaryStage.setTitle("IconChanger");
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    private BorderPane createBody() {
        treeView = new TreeView<>();
        fileChooser = new FileChooser();
        directoryChooser = new DirectoryChooser();
        selectedImageFile = new File("/folder.png");
        fileChooser = new FileChooser();
        directoryChooser = new DirectoryChooser();

        // Create Menu Pane
        VBox menus = new VBox();
        menus.getChildren().addAll(createMenuBar());

        // Create Center Pane
        SplitPane splitPane = new SplitPane();
        rootFile = new File(System.getProperty("user.home"));

        try {
            ImageView imageView = new ImageView((new Image("/folder.png")));
            imageView.setFitHeight(10);
            imageView.setFitWidth(10);
            treeView.setRoot(new SimpleFileTreeItem(rootFile, imageView));
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Create Right Pane
        BorderPane rightPane = new BorderPane();
        rightPane.setPrefWidth(600);
        rightPane.setPadding(new Insets(10, 10, 10, 10));
        centerPane = generateGrid(rootFile);
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
        finalPreview = preview;

        treeView.onMouseClickedProperty().set(event -> {
            if (event.getClickCount() == 2) {
                File file = treeView.getSelectionModel().getSelectedItem().getValue();
                if (treeView.getSelectionModel().getSelectedItem().getValue().equals(rootFile)) {
                    file = file.getParentFile();
                }
                setRoot(file);
            }
        });
        Button chooseFile = new Button("Choose a file");
        chooseFile.setOnAction(event -> {
            fileChooser.setTitle("Choose a file");
            fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("PNG", "*.png"));
            List<File> file = fileChooser.showOpenMultipleDialog(stage);
            if (file != null) {
                try {
                    for (File f : file) {
                        selectedImageFile = f;
                        Image selectedImage = new Image(selectedImageFile.toURI().toURL().toString());
                        finalPreview.setImage(selectedImage);
                        prevUsedFolders.add(selectedImage);
                    }
                    usedGrid.getChildren().clear();
                    usedGrid.getChildren().addAll(generateUsedGrid(prevUsedFolders));
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }

        });

        Button submit = createSubmitButton(rightPane, chooseFile);

        Label topLabel = new Label("Edit");
        topLabel.setFont(new javafx.scene.text.Font(40));
        topLabel.setPadding(new Insets(10, 10, 0, 10));
        Label subTopLabel = new Label("Select the folder you want to change the icon of");
        VBox textBox = new VBox();
        textBox.getChildren().addAll(topLabel, subTopLabel);
        rightPane.setTop(textBox);
        VBox centerBox = new VBox();
        Label previewText = new Label("Selected Image:");
        previewText.setPadding(new Insets(10, 10, 10, 10));
        Label description = new Label("Previously selected Images:");
        description.setPadding(new Insets(10, 10, 10, 10));
        centerBox.getChildren().addAll(previewText, preview, chooseFile, description);
        centerBox.setAlignment(Pos.CENTER);
        usedGrid = generateUsedGrid(prevUsedFolders);
        usedGrid.gridLinesVisibleProperty().set(true);
        rightPane.setCenter(new VBox(centerBox, new ScrollPane(usedGrid)));
        rightPane.setBottom(submit);
        splitPane.getItems().addAll(treeView, scrollPane, rightPane);
        BorderPane root = new BorderPane();
        root.setTop(menus);
        root.setCenter(splitPane);
        return root;
    }

    private MenuBar createMenuBar() {
        MenuBar menu = new MenuBar();
        Menu menuFile = new Menu("File");
        MenuItem menuItemOpen = new MenuItem("Open");
        menuFile.getItems().add(menuItemOpen);
        Menu menuHelp = new Menu("Help");
        MenuItem about = new MenuItem("About");
        about.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("About");
            alert.setHeaderText("IconChanger");
            alert.setContentText("""
                    IconChanger is a simple program that allows you to change the icon of a folder.
                                        
                    Version: 1.0
                    Author: Florian Groß
                    """);
            alert.showAndWait();
        });
        menuHelp.getItems().add(about);
        menu.getMenus().addAll(menuFile, menuHelp);
        menuItemOpen.setOnAction(event -> {
            directoryChooser.setTitle("Open Folder");
            File file = directoryChooser.showDialog(stage);
            if (file != null) {
                treeView.setRoot(new SimpleFileTreeItem(file));
                centerPane.getChildren().clear();
                centerPane.getChildren().addAll(generateGrid(file));
            }
        });
        return menu;
    }

    private Button createSubmitButton(BorderPane rightPane, Button chooseFile) {
        Button submit = new Button("Submit");
        submit.setPrefWidth(rightPane.getPrefWidth());
        submit.setPrefHeight(50);
        chooseFile.setPrefWidth(rightPane.getPrefWidth() / 2);

        submit.setOnAction(event -> {
            try {
                writeDesktopIni(selectedFolder.get(0), IconConverter.pngToIco(selectedImageFile));
            } catch (Exception e) {
                e.printStackTrace();
            }
            ImageView iconImage = new ImageView(new Image(selectedImageFile.toURI().toString()));
            iconImage.setFitWidth(10);
            iconImage.setFitHeight(10);
            try {
                treeView.getSelectionModel().getSelectedItem().setGraphic(iconImage);
            } catch (Exception e) {
            }
            for (File file : selectedFolder) {
                iconMap.put(file, finalPreview.getImage());
            }
            selectedFolder.clear();
            centerPane.getChildren().clear();
            centerPane.getChildren().addAll(generateGrid(rootFile));
        });
        return submit;
    }

    public static void setRoot(File file) {
        if (file.isDirectory()) {
            rootFile = file;
            treeView.setRoot(new SimpleFileTreeItem(file, new ImageView((new Image("/folder.png")))));
            centerPane.getChildren().clear();
            centerPane.getChildren().add(generateGrid(file));
        }
    }

    private GridPane generateUsedGrid(List<Image> prevUsedFolders) {
        GridPane gridPane = new GridPane();
        int j = 0;
        int it = 0;
        for (Image image : prevUsedFolders) {
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            imageView.onMouseClickedProperty().set(event -> {
                selectedImageFile = new File(image.getUrl());
                finalPreview.setImage(image);
            });
            gridPane.add(imageView, it++ % 3, j);
            if (it % 3 == 0) {
                j++;
            }
        }
        gridPane.setGridLinesVisible(true);
        return gridPane;
    }

    public static GridPane generateGrid(File file) {
        GridPane gridPane = new GridPane();
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            int j = 0, it = 0;
            for (int i = 0; i < Objects.requireNonNull(files).length; i++) {
                if (files[i].isDirectory()) {
                    File imageFile = printFolderImagePath(files[i]);
                    Image image = getImage(imageFile);
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

    private static Image getImage(File imageFile) {
        Image image;
        if (imageFile == null) {
            image = new Image(Objects.requireNonNull(MainApplication.class.getResource("/folder.png")).toString());
        } else {
            try {
                File iconImage = IconConverter.icoToPng(imageFile);
                assert iconImage != null;
                image = new Image(iconImage.toURI().toString());
            } catch (Exception e) {
                image = new Image(Objects.requireNonNull(MainApplication.class.getResource("/folder.png")).toString());
            }
        }
        return image;
    }

    public static File printFolderImagePath(File directory) {
        return getFile(directory);
    }

    static File getFile(File directory) {
        try {
            if (new File(directory.getAbsolutePath() + "\\desktop.ini").exists()) {
                @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
                Wini wini = new Wini(new File(directory.getAbsolutePath() + "\\desktop.ini"));
                String path = wini.get(".ShellClassInfo", "IconResource").split(",")[0];
                return new File(path);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static void writeDesktopIni(File directoryPath, File iconPath) {
        try {
            Wini ini;
            if (directoryPath.isDirectory()) {
                new File(directoryPath.getAbsolutePath() + "\\desktop.ini").createNewFile();
                File desktopIni = new File(directoryPath.getAbsolutePath() + "\\desktop.ini");
                ini = new Wini(desktopIni);

                String field = iconPath.getAbsolutePath() + ",0";
                ini.put(".ShellClassInfo", "IconResource", field);
                ini.store();
                Process processCreateFile = Runtime.getRuntime().exec("attrib +h +s " + desktopIni.getAbsolutePath());
                Process processCreateFolder = Runtime.getRuntime().exec("attrib -h +s " + directoryPath.getAbsolutePath());
                System.out.println("Desktop.ini written: " + desktopIni.getAbsolutePath());
            }
        } catch (IOException ex) {
            System.out.println("Error Here: " + ex.getMessage());
        }
    }
}

