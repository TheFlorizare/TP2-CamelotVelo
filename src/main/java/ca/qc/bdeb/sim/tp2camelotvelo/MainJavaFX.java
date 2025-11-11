package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainJavaFX extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        var root = new Pane();
        Scene scene = new Scene(root, 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}
