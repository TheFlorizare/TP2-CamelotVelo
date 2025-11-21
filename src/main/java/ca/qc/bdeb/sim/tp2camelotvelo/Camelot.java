package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

import java.net.URL;

public class Camelot extends ObjetDuJeu {

    private static final double GRAVITE = 1500;
    private static final double VITESSE_BASE = 400;

    protected Image camelot1;
    protected Image camelot2;
    protected ImageView camelotView;
    protected boolean toucherSol;
    private double tempsTotal = 0;

    public Camelot() {
        // 1) Charger les images correctement
        camelot1 = chargerImage("/Assets/camelot1.png");
        camelot2 = chargerImage("/Assets/camelot2.png");

        camelotView = new ImageView(camelot1);
        camelotView.setPreserveRatio(true);

        // 2) Initialiser la taille AVANT de l'utiliser
        taille = new Point2D(172, 144);

        // 3) Position de départ (au sol, centré)
        position = new Point2D(
                MainJavaFX.WIDTH / 2.0 - taille.getX() / 2.0,
                MainJavaFX.HEIGHT - taille.getY()
        );

        // 4) Vitesse et accélération de départ
        velocite = new Point2D(VITESSE_BASE, 0);      // 400 px/s vers la droite
        acceleration = new Point2D(0, GRAVITE);       // gravité vers le bas

        toucherSol = true;
    }

    private Image chargerImage(String chemin) {
        URL url = getClass().getResource(chemin);
        if (url == null) {
            System.out.println("⚠ Image introuvable : " + chemin);
            throw new IllegalStateException("Image introuvable : " + chemin);
        }
        return new Image(url.toExternalForm());
    }

    @Override
    public void draw(GraphicsContext context) {
        context.drawImage(
                camelotView.getImage(),
                position.getX(),
                position.getY(),
                taille.getX(),
                taille.getY()
        );
    }

    public void update(double deltaTemps) {

        // --- Animation (comme dans l'énoncé) ---
        tempsTotal += deltaTemps;
        int index = (int) (tempsTotal * 4) % 2;
        camelotView.setImage(index == 0 ? camelot1 : camelot2);

        // --- Entrées clavier ---
        boolean droite = Input.isKeyPressed(KeyCode.RIGHT);
        boolean gauche = Input.isKeyPressed(KeyCode.LEFT);
        boolean saut = Input.isKeyPressed(KeyCode.SPACE)
                || Input.isKeyPressed(KeyCode.UP);

        // --- Accélération en X ---
        double ax;

        if (droite && !gauche) {
            ax = +300;
        } else if (gauche && !droite) {
            ax = -300;
        } else {
            // Retour graduel à 400 px/s
            if (velocite.getX() < 400) {
                ax = +300;
            } else if (velocite.getX() > 400) {
                ax = -300;
            } else {
                ax = 0;
            }
        }

        // --- Saut ---
        if (saut && toucherSol) {
            velocite = new Point2D(velocite.getX(), -500);
            toucherSol = false;
        }

        // --- Accélération totale ---
        acceleration = new Point2D(ax, GRAVITE);

        // --- Physique : v = v + a*dt, x = x + v*dt ---
        velocite = velocite.add(acceleration.multiply(deltaTemps));
        position = position.add(velocite.multiply(deltaTemps));


        if (position.getY() + taille.getY() >= MainJavaFX.HEIGHT) {
            position = new Point2D(position.getX(), MainJavaFX.HEIGHT - taille.getY());
            toucherSol = true;
            velocite = new Point2D(velocite.getX(), 0);
        }
        position = new Point2D(
                position.getX(),
                Math.clamp(position.getY(), 0, MainJavaFX.HEIGHT - taille.getY())
        );

        velocite = new Point2D(
                Math.clamp(velocite.getX(), 200, 600),
                velocite.getY()
        );
        System.out.println("Velocite: " + velocite.getX());
        System.out.println("Position: " + position.getX());
        System.out.println("Acceleration: " + acceleration.getX());

    }
    public Point2D getPosition() {
        return position;
    }

    public Point2D getTaille() {
        return taille;
    }

    public Image getImageCourante() {
        return camelotView.getImage();
    }


}
