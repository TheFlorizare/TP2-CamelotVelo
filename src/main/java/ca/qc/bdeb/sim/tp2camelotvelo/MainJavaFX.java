package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.canvas.GraphicsContext;

import java.io.IOException;

public class MainJavaFX extends Application {


    @Override
    public void start(Stage stage) throws IOException {

        Partie partie = new Partie();

        var timer = new AnimationTimer() {
            long dernierTemps = System.nanoTime();

            @Override
            public void handle(long temps) {

                double deltaTemps = (temps - dernierTemps) * 1e-9;

                partie.update(deltaTemps);
                partie.draw(context);

                dernierTemps = temps;
            }
        };
        timer.start();

        var root = new Pane();
        Scene scene = new Scene(root, 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}
