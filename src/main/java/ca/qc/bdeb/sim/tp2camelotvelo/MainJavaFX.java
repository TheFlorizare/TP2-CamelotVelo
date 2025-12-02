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

    public static final double WIDTH = 900, HEIGHT = 580;

    private Scene sceneAccueil;
    private Scene sceneNiveau1;
    private Scene sceneFin;

    private GraphicsContext contextNiveau;


    private boolean scenePrincipale = true;
    private boolean enEcranFin = false;

    private int niveauActuel = 1;
    int nbArgentReel;
    private Partie partie = new Partie(1);

    private Stage stage;


    private long tempsDebutEcranFin;

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        sceneAccueil = creationSceneAccueil();
        sceneNiveau1 = creationSceneNiveau();


        stage.setScene(sceneAccueil);
        stage.setTitle("Camelot Vélo");
        stage.show();

        AnimationTimer timer = new AnimationTimer() {
            long tempsInitial = System.nanoTime();
            long derniereFrameTemps = System.nanoTime();
            //ChatGPT pour ces variables


            @Override
            public void handle(long maintenant) {

                double totalElapsed = (maintenant - tempsInitial) * 1e-9;
                double deltaTemps = (maintenant - derniereFrameTemps) * 1e-9;
                derniereFrameTemps = maintenant;


                if (scenePrincipale) {
                    if (totalElapsed >= 2) {
                        scenePrincipale = false;
                        stage.setScene(sceneNiveau1);
                    }
                    return;
                }


                if (enEcranFin) {
                    double tempsDepuisFin = (maintenant - tempsDebutEcranFin) * 1e-9;

                    if (tempsDepuisFin >= 3) {

                        niveauActuel = 1;
                        Partie.nbJournal = 12;
                        Partie.nbArgent = 0;

                        partie = new Partie(niveauActuel);
                        sceneNiveau1 = creationSceneNiveau();
                        sceneAccueil = creationSceneAccueil();

                        scenePrincipale = true;
                        enEcranFin = false;

                        tempsInitial = maintenant;
                        stage.setScene(sceneAccueil);
                    }

                    return;
                }

                partie.update(deltaTemps);

                contextNiveau.clearRect(0, 0, MainJavaFX.WIDTH, MainJavaFX.HEIGHT);
                partie.draw(contextNiveau);


                if (partie.isNiveauTermine()) {
                    Input.reset();

                    niveauActuel++;
                    Partie.nbJournal += 12;

                    partie = new Partie(niveauActuel);
                    sceneNiveau1 = creationSceneNiveau();
                    sceneAccueil = creationSceneAccueil();
                    scenePrincipale = true;

                    tempsInitial = maintenant;
                    stage.setScene(sceneAccueil);
                    return;
                }


                boolean plusDeJournauxNullePart =
                        (Partie.nbJournal == 0 && Partie.journaux.isEmpty());

                if (plusDeJournauxNullePart && !enEcranFin) {

                    enEcranFin = true;
                    tempsDebutEcranFin = maintenant;

                    Input.reset();

                    sceneFin = creationSceneFin();

                    stage.setScene(sceneFin);
                }
                System.out.println(Partie.nbArgent + "$");
                nbArgentReel = Partie.nbArgent;

            }

        };


        timer.start();
    }



    private Scene creationSceneFin() {
        StackPane root = new StackPane();

        root.setStyle("-fx-background-color: black;");
        //ChatGPT pour trouver la couleur

        Text titre = new Text("Rupture de stocks");
        titre.setFont(Font.font(40));
        titre.setFill(Color.RED);

        String texteArgent = "Argent collecté: " + nbArgentReel + "$";
        Text titre2 = new Text(texteArgent);
        titre2.setFont(Font.font(40));
        titre2.setFill(Color.GREEN);

        VBox ui = new VBox(40, titre, titre2);
        ui.setAlignment(Pos.CENTER);
        root.getChildren().add(ui);

        Scene scene = new Scene(root, WIDTH, HEIGHT);

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                Platform.exit();
            }
        });

        return scene;
    }

    private Scene creationSceneAccueil() {
        StackPane root = new StackPane();

        root.setStyle("-fx-background-color: black;");

        Text titre = new Text("Niveau " + niveauActuel);
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
            if (e.getCode() == KeyCode.Q) {
                Partie.nbJournal += 10;
                return;
            }
            if (e.getCode() == KeyCode.K) {
                Partie.nbJournal = 0;
                return;
            }
            if (e.getCode() == KeyCode.L) {
                partie.forceNiveauTermine();
                return;
            }
            if (e.getCode() == KeyCode.D) {
                partie.ouvrirDebogage();
            }
            if (e.getCode() == KeyCode.F) {
                partie.ouvrirDebogageChamp();
            }
            if (e.getCode() == KeyCode.I) {
                partie.creerParticulesTest(); // si tu as cette méthode
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
