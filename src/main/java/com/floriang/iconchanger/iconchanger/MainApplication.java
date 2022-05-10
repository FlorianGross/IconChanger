package com.floriang.iconchanger.iconchanger;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.File;

public class MainApplication extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(javafx.stage.Stage primaryStage) {
        MenuBar menu = new MenuBar();
        Menu menuFile = new Menu("File");
        Menu menuHelp = new Menu("Help");
        MenuItem about = new MenuItem("About");

        menuHelp.getItems().add(about);
        menu.getMenus().addAll(menuFile, menuHelp);

        VBox menus = new VBox();
        menus.getChildren().addAll(menu);

        SplitPane splitPane = new SplitPane();
        File testFile = new File("C://");
        try {
            TreeView<File> treeView = new TreeView<>(new SimpleFileTreeItem(testFile, new ImageView(new Image(IconConverter.icoToPng(new File("D:\\Downloads\\demo\\IconChanger\\src\\main\\resources\\com\\floriang\\iconchanger\\iconchanger\\folder.ico")).getAbsolutePath()))));
            splitPane.getItems().addAll(treeView);
        }catch (Exception e){
            splitPane.getItems().add(new Label("No files Found"));
        }


        BorderPane root = new BorderPane();
        root.setTop(menus);
        root.setCenter(splitPane);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("FileRex");
        primaryStage.show();
    }
}
