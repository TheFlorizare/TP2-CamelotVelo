package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.canvas.GraphicsContext;

import java.io.IOException;
import java.util.Random;

public class MainJavaFX extends Application {

    public static final double WIDTH = 640, HEIGHT = 480;

    private Scene sceneAccueil() {
        var root = new StackPane();
        var scene = new Scene(root, WIDTH, HEIGHT);
        root.setAlignment(Pos.CENTER);

        var btnJouer = new Button("Jouer");

        btnJouer.setOnAction((e) -> {
            stage;
        });

        return scene;
    }

    public Scene sceneNiveau1() {
        var root = new Pane();
        var scene = new Scene(root, WIDTH, HEIGHT);
        var canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);
        var context = canvas.getGraphicsContext2D();

        return scene;
    }
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
        Scene scene = new Scene(root, 640, 480);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}
