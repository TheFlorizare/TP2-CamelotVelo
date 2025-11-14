package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainJavaFX extends Application {

    public static final double WIDTH = 640, HEIGHT = 480;

    private Scene sceneAccueil;
    private Scene sceneNiveau1;

    private GraphicsContext contextNiveau1;

    private boolean scenePrincipale = true;

    @Override
    public void start(Stage stage) {


        sceneAccueil = creationSceneAccueil(stage);
        sceneNiveau1 = creationSceneNiveau1();


        stage.setScene(sceneAccueil);
        stage.setTitle("Camelot Vélo");
        stage.show();

        Partie partie = new Partie();


        AnimationTimer timer = new AnimationTimer() {
            long tempsInitial = System.nanoTime();
            long derniereFrameTemps = System.nanoTime();

            @Override
            public void handle(long now) {


                double totalElapsed = (now - tempsInitial) * 1e-9;


                double deltaTemps = (now - derniereFrameTemps) * 1e-9;

                derniereFrameTemps = now;


                if (scenePrincipale && totalElapsed >= 2) {
                    scenePrincipale = false;
                    stage.setScene(sceneNiveau1);
                }


                if (!scenePrincipale) {


                    partie.update(deltaTemps);
                    partie.draw(contextNiveau1);
                }
            }
        };

        timer.start();
    }


    private Scene creationSceneAccueil(Stage stage) {
        StackPane root = new StackPane();


        root.setStyle("-fx-background-color: black;");


        Text titre = new Text("Camelot Vélo");
        titre.setFont(Font.font(40));
        titre.setFill(Color.WHITE);


        VBox ui = new VBox(40, titre);
        ui.setAlignment(Pos.CENTER);

        root.getChildren().add(ui);

        return new Scene(root, WIDTH, HEIGHT);
    }

    private Scene creationSceneNiveau1() {
        Pane root = new Pane();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        contextNiveau1 = canvas.getGraphicsContext2D();
        Image brique = new Image(getClass().getResource("/Assets/brique.png").toExternalForm());
        double imgWidth = brique.getWidth();
        double imgHeight = brique.getHeight();

        for (double x = 0; x < WIDTH; x += imgWidth) {
            for (double y = 0; y < HEIGHT; y += imgHeight) {
                contextNiveau1.drawImage(brique, x, y, imgWidth, imgHeight);
            }
        }
        root.getChildren().add(canvas);
        return new Scene(root, WIDTH, HEIGHT);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
