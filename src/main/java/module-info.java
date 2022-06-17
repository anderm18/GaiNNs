module com.example.gainns {
    requires transitive javafx.controls; //added transitive so I can use Scene as a parameter
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
	requires javafx.graphics;
	requires javafx.base;
	requires org.dyn4j;

    opens com.example.gainns to javafx.fxml;
    exports com.example.gainns;
}