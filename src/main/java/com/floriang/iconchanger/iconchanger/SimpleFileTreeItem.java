package com.floriang.iconchanger.iconchanger;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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

    /**
     * Calling the constructor of super class in oder to create a new
     * TreeItem<File>.
     *
     * @param f an object of type File from which a tree should be build or
     *          which children should be gotten.
     */
    public SimpleFileTreeItem(File f, Node image) {
        super(f, image);
        ImageView imageView = (ImageView) image;
        imageView.setFitHeight(10);
        imageView.setFitWidth(10);
    }
    public SimpleFileTreeItem(File f){super(f);}
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
            File f =  getValue();
            isLeaf = f.isFile();
        }

        return isLeaf;
    }

    /**
     * Returning a collection of type ObservableList containing TreeItems, which
     * represent all children available in handed TreeItem.
     *
     * @param TreeItem the root node from which children a collection of TreeItem
     *                 should be created.
     * @return an ObservableList<TreeItem<File>> containing TreeItems, which
     * represent all children available in handed TreeItem. If the
     * handed TreeItem is a leaf, an empty list is returned.
     */
    private ObservableList<TreeItem<File>> buildChildren(TreeItem<File> TreeItem) {
        File f = TreeItem.getValue();
        if (f != null && f.isDirectory()) {
            File[] files = f.listFiles();
            if (files != null) {
                ObservableList<TreeItem<File>> children = FXCollections
                        .observableArrayList();

                for (File childFile : files) {
                        children.add(new SimpleFileTreeItem(childFile, new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/folder.png")).toString()))));
                }

                return children;
            }
        }

        return FXCollections.emptyObservableList();
    }

    private Node getFileIcon(File file) throws IOException {
        try {
            return new ImageView(new Image(IconConverter.icoToPng(file).getAbsolutePath()));
        } catch (Exception e) {
            System.out.println("Error converting Image");
        }
        return new ImageView(new Image(IconConverter.icoToPng(new File("D:\\Downloads\\demo\\IconChanger\\src\\main\\resources\\com\\floriang\\iconchanger\\iconchanger\\folder.ico")).getAbsolutePath()));
    }

    private boolean isFirstTimeChildren = true;
    private boolean isFirstTimeLeaf = true;
    private boolean isLeaf;
}
