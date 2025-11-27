package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

import java.security.Key;
import java.util.Random;

public class Journal extends ObjetDuJeu {

    Random r = new Random();
    private static final double gravite = 1500;
    private static final double vitesseMax = 1500;
    protected Image journal;
    protected double masse;
    private boolean presence = true;
    private Partie partie;


    public Journal(Partie partie, Point2D positionCamelot, Point2D vitesseCamelot,
                   Point2D quantiteMouvement, double masseJournal) {

        this.partie = partie;
        this.velocite = vitesseCamelot.add(quantiteMouvement.multiply(1.0/masseJournal));
        this.position = positionCamelot;
        this.masse = masseJournal;
        this.acceleration = Point2D.ZERO;
        this.taille = new Point2D(52,31);
        journal = new Image("/assets/journal.png");
    }

    @Override
    public void draw(GraphicsContext context) {
        if (!presence) return;
        context.drawImage(
                journal,
                position.getX(),
                position.getY(),
                taille.getX(),
                taille.getY());
    }

    public void update(double deltaTemps) {
        if (!presence) return;

        // gravite
        acceleration = new Point2D(0, 1500);

        velocite = velocite.add(acceleration.multiply(deltaTemps));

        if (velocite.magnitude() > vitesseMax) {
            velocite = velocite.multiply(vitesseMax / velocite.magnitude());
        }

        position = position.add(velocite.multiply(deltaTemps));

        // hors ecran
        if (position.getX() + taille.getX() < 0 ||
                position.getX() > MainJavaFX.WIDTH ||
                position.getY() > MainJavaFX.HEIGHT) {
            presence = false;
        }

        Point2D E = partie.champElectrique(partie.getParticules(), this.getCentre());

        Point2D forceElectrique = E.multiply(900);

        Point2D accelerationElectrique = acceleration.multiply(1.0 / masse);

        acceleration = new Point2D(0,1500).add(accelerationElectrique);
    }


    public boolean estPresent() {
        return presence;
    }

    public void detruire() {
        presence = false;
    }
}
