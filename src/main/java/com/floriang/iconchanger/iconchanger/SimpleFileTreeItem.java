package com.floriang.iconchanger.iconchanger;

import java.io.File;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SimpleFileTreeItem extends TreeItem<File> {

    public SimpleFileTreeItem(File f, Node image) {
        super(f, image);
        ImageView imageView = (ImageView) image;
        imageView.setFitHeight(10);
        imageView.setFitWidth(10);
        if (f.equals(MainApplication.rootFile)) {
            setExpanded(true);
        } else {
            setExpanded(false);
        }
    }

    public SimpleFileTreeItem(File f) {
        super(f);
    }

    @Override
    public ObservableList<TreeItem<File>> getChildren() {
        if (isFirstTimeChildren) {
            isFirstTimeChildren = false;
            super.getChildren().setAll(buildChildren(this));
        }
        return super.getChildren();
    }

    @Override
    public boolean isLeaf() {
        if (isFirstTimeLeaf) {
            isFirstTimeLeaf = false;
            File f = getValue();
            isLeaf = f.isFile();
        }

        return isLeaf;
    }

    private ObservableList<TreeItem<File>> buildChildren(TreeItem<File> TreeItem) {
        File f = TreeItem.getValue();
        if (f != null && f.isDirectory()) {
            File[] files = f.listFiles(File::isDirectory);
            if (files != null) {
                ObservableList<TreeItem<File>> children = FXCollections
                        .observableArrayList();

                for (File childFile : files) {
                    Image image;
                    try {
                        image = SimpleFolderGridItem.getImageFromIni(childFile);
                        if (image == null) {
                            image = new Image(Objects.requireNonNull(getClass().getResource("/folder.png")).toString());
                        }
                    } catch (Exception e) {
                        image = new Image(Objects.requireNonNull(getClass().getResource("/folder.png")).toString());
                    }
                    children.add(new SimpleFileTreeItem(childFile, new ImageView(image)));
                }

                return children;
            }
        }

        return FXCollections.emptyObservableList();
    }

    private boolean isFirstTimeChildren = true;
    private boolean isFirstTimeLeaf = true;
    private boolean isLeaf;
}
