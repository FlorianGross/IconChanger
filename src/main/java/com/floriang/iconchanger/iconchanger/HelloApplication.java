package com.floriang.iconchanger.iconchanger;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.ini4j.Wini;

public class HelloApplication extends Application {
    File selectedFile;
    File rootFile;

    @Override
    public void start(Stage primaryStage) {
        try {
            MenuBar menu = new MenuBar();
            Menu menuFile = new Menu("File");
            Menu menuHelp = new Menu("Help");
            MenuItem about = new MenuItem("About");
            about.setOnAction(event -> {
                /*
                 * ToDo: Implement About Dialog
                 */
            });
            menuFile.setOnAction(event -> {
                rootFile = new File("/");
            });
            menuHelp.getItems().add(about);

            /* Adding all sub menus at ones to a MenuBar. */
            menu.getMenus().addAll(menuFile, menuHelp);

            /* Create a new VBox and add the menu as well as the tools. */
            VBox menus = new VBox();
            menus.getChildren().addAll(menu);
            File testFile = new File("/");
            /*
             * Adding a TreeView to the very left of the application window.
             */
            TreeView<File> fileView = new TreeView<>(
                    new SimpleFileTreeItem(testFile));

            TreeView<File> folderView = new TreeView<>(new SimpleFileTreeItem(testFile));
            SelectionModel<TreeItem<File>> sel = fileView.getSelectionModel();
            sel.selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    selectedFile = sel.getSelectedItem().getValue();
                    folderView.setRoot(new SimpleFileTreeItem(selectedFile));
                }
            });
            HBox detailView = new HBox();
            ImageView imageView = new ImageView();
            imageView.setImage(new Image(getClass().getResource("/com/floriang/iconchanger/iconchanger/folder.ico").toString()));
            detailView.getChildren().addAll(imageView);
            final FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select an Icon (.ico)");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Icon Files", "*.ico"));
            Button chooseIcon = new Button("Choose Icon");
            chooseIcon.setOnAction(event -> {
                selectedFile = fileChooser.showOpenDialog(primaryStage);
                if (selectedFile != null) {
                    imageView.setImage(new Image(selectedFile.toURI().toString()));
                }
            });

            detailView.getChildren().addAll(chooseIcon);



            /* Creating a SplitPane and adding the fileView. */
            SplitPane splitView = new SplitPane();

            splitView.getItems().addAll(fileView, folderView, detailView);
            /* Create a root node as BorderPane. */
            BorderPane root = new BorderPane();

            /* Adding the menus as well as the content pane to the root node. */
            root.setTop(menus);
            root.setCenter(splitView);

            /*
             * Setting the root node of a scene as well as the applications CSS
             * file. CSS file has to be in same src directory as this class. The
             * path is always relative.
             */
            Scene scene = new Scene(root, 800, 600);
            /**scene.getStylesheets().add(
             getClass().getResource("application.css").toExternalForm());
             **/
            /* Adding a scene to the stage. */
            primaryStage.setScene(scene);
            primaryStage.setTitle("FileRex");

            /* Lift the curtain :0). */
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeDesktopIni(File file) {
        File Folder = new File("/Users/florian/test/");
        try {
            Wini ini;
            try {
                ini = new Wini(new File("/Users/florian/test/desktop.ini"));
            } catch (IOException ex) {
                new File("/Users/florian/test/desktop.ini").createNewFile();
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

    public static void main(String[] args) {
        writeDesktopIni(new File("/Users/florian/test/"));
        launch();

    }
}
