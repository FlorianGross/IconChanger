module com.floriang.iconchanger.iconchanger {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires izpack.ini4j;
    requires java.desktop;
    requires image4j;

    opens com.floriang.iconchanger.iconchanger to javafx.fxml;
    exports com.floriang.iconchanger.iconchanger;
}
