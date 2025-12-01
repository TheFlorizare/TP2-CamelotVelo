package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainJavaFX extends Application {
    

    public static final double WIDTH = 900, HEIGHT = 480;

    private Scene sceneAccueil;
    private Scene sceneNiveau1;

    private GraphicsContext contextNiveau;

    private boolean scenePrincipale = true;
    private boolean niveau2 = false;
    private Partie partie;

    @Override
    public void start(Stage stage) {


        sceneAccueil = creationSceneAccueil(stage);
        sceneNiveau1 = creationSceneNiveau();


        stage.setScene(sceneAccueil);
        stage.setTitle("Camelot Vélo");
        stage.show();


        this.partie = new Partie();


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


                    contextNiveau.clearRect(0, 0, MainJavaFX.WIDTH, MainJavaFX.HEIGHT);


                    partie.draw(contextNiveau);
                }
            }
        };

        timer.start();
    }


    private Scene creationSceneAccueil(Stage stage) {
        StackPane root = new StackPane();


        root.setStyle("-fx-background-color: black;");


        Text titre = new Text("Niveau 1");
        titre.setFont(Font.font(40));
        titre.setFill(Color.GREEN);


        VBox ui = new VBox(40, titre);
        ui.setAlignment(Pos.CENTER);

        root.getChildren().add(ui);

        return new Scene(root, WIDTH, HEIGHT);
    }

    private Scene creationSceneNiveau() {
        Pane root = new Pane();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        contextNiveau = canvas.getGraphicsContext2D();
        root.setStyle("-fx-background-color: black;");


        root.getChildren().add(canvas);
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.D) {
                partie.activerDebogage();
            }
            if (e.getCode() == KeyCode.F) {
                partie.activerDebogageChamp();
            }
            if (e.getCode() == KeyCode.I) {
                partie.activerDebogageChampTest();
            }
            if (e.getCode() == KeyCode.Q) {
                partie.ajoutJournauxDebogage();
            }
            if (e.getCode() == KeyCode.K) {
                partie.suppressionJournauxDebogage();
            }
            if (e.getCode() == KeyCode.L) {
                partie.prochainNiveauDebogage();
            }
            if (e.getCode() == KeyCode.ESCAPE) {
                Platform.exit();
            } else {
                Input.setKeyPressed(e.getCode(), true);
            }
        });
        scene.setOnKeyReleased((e) -> {
            Input.setKeyPressed(e.getCode(), false);
        });
        return scene;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
