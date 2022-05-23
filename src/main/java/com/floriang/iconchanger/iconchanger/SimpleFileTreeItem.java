package com.floriang.iconchanger.iconchanger;

import java.io.File;
import java.util.Objects;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import static com.floriang.iconchanger.iconchanger.MainApplication.getFile;

/**
 * @author Alexander Bolte - Bolte Consulting (2010 - 2014).
 * <p>
 * This class shall be a simple implementation of a TreeItem for
 * displaying a file system tree.
 * <p>
 * The idea for this class is taken from the Oracle API docs found at
 * http
 * ://docs.oracle.com/javafx/2/api/javafx/scene/control/TreeItem.html.
 * <p>
 * Basically the file sytsem will only be inspected once. If it changes
 * during runtime the whole tree would have to be rebuild. Event
 * handling is not provided in this implementation.
 */
public class SimpleFileTreeItem extends TreeItem<File> {

    public SimpleFileTreeItem(File f, Node image) {
        super(f, image);
        ImageView imageView = (ImageView) image;
        imageView.setFitHeight(10);
        imageView.setFitWidth(10);
        if(f.equals(MainApplication.rootFile)){
            setExpanded(true);
        }else{
            setExpanded(false);
        }
    }

    public SimpleFileTreeItem(File f) {
        super(f);
    }

    /*
     * (non-Javadoc)
     *
     * @see javafx.scene.control.TreeItem#getChildren()
     */
    @Override
    public ObservableList<TreeItem<File>> getChildren() {
        if (isFirstTimeChildren) {
            isFirstTimeChildren = false;

            /*
             * First getChildren() call, so we actually go off and determine the
             * children of the File contained in this TreeItem.
             */
            super.getChildren().setAll(buildChildren(this));
        }
        return super.getChildren();
    }

    /*
     * (non-Javadoc)
     *
     * @see javafx.scene.control.TreeItem#isLeaf()
     */
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
            File[] files = f.listFiles();
            if (files != null) {
                ObservableList<TreeItem<File>> children = FXCollections
                        .observableArrayList();

                for (File childFile : files) {
                    File imageFile = printFolderImagePath(childFile);
                    Image image;
                    if (imageFile == null) {
                        image = new Image(Objects.requireNonNull(getClass().getResource("/folder.png")).toString());
                    } else {
                        try {
                            File iconImage = IconConverter.icoToPng(imageFile);
                            assert iconImage != null;
                            image = new Image(iconImage.toURI().toString());
                        } catch (Exception e) {
                            image = new Image(Objects.requireNonNull(getClass().getResource("/folder.png")).toString());
                        }
                    }
                    children.add(new SimpleFileTreeItem(childFile, new ImageView(image)));
                }

                return children;
            }
        }

        return FXCollections.emptyObservableList();
    }

    private static File printFolderImagePath(File directory) {
        return getFile(directory);
    }

    private boolean isFirstTimeChildren = true;
    private boolean isFirstTimeLeaf = true;
    private boolean isLeaf;
}
