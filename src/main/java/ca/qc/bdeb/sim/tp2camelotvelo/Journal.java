package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.security.Key;
import java.util.Random;

public class Journal extends ObjetDuJeu {

    Random r = new Random();
    private static final double gravite = 1500;
    private static final double vitesseMax = 1500;
    protected double masse;
    private boolean presence = true;
    private Image imgJournal = new Image(getClass().getResourceAsStream("/Assets/journal.png"));
    private Partie partie;


    public Journal(Point2D positionCamelot, Point2D vitesseCamelot,
                   Point2D quantiteMouvement, double masseJournal, Partie partie) {

        this.velocite = vitesseCamelot.add(quantiteMouvement.multiply(1.0 / masseJournal));
        this.position = positionCamelot;
        this.masse = masseJournal;
        this.acceleration = Point2D.ZERO;
        this.taille = new Point2D(52, 31);
        this.partie = partie;

    }

    @Override
    public void draw(GraphicsContext context, Camera camera, boolean modeDebogage) {
        if (!presence) return;

        Point2D posEcran = camera.coordEcran(position);

        context.drawImage(
                imgJournal,
                posEcran.getX(),
                posEcran.getY(),
                taille.getX(),
                taille.getY()
        );
        if (modeDebogage) {
            context.setStroke(Color.YELLOW);
            context.strokeRect(
                    posEcran.getX(),
                    posEcran.getY(),
                    taille.getX(),
                    taille.getY()
            );
        }

    }


    @Override
    public void update(double deltaTemps) {
        if (!presence) return;

        if (partie.getParticules().isEmpty()) {

            acceleration = new Point2D(0, gravite);
        } else {

            Point2D E = partie.champElectrique(partie.getParticules(), this.getCentre());

            Point2D forceElectrique = E.multiply(900);
            Point2D accelerationElectrique = forceElectrique.multiply(1.0 / masse);


            acceleration = new Point2D(0, gravite).add(accelerationElectrique);
        }


        velocite = velocite.add(acceleration.multiply(deltaTemps));

        if (velocite.magnitude() > vitesseMax) {
            velocite = velocite.multiply(vitesseMax / velocite.magnitude());
        }


        position = position.add(velocite.multiply(deltaTemps));

        if (position.getY() > MainJavaFX.HEIGHT + 200) {
            presence = false;
        }
    }


    public boolean estPresent() {
        return presence;
    }

    public void detruire() {
        presence = false;
    }

    public boolean collision(ObjetDuJeu obj) {
        return this.getDroite() > obj.getGauche() &&
                this.getGauche() < obj.getDroite() &&
                this.getBas() > obj.getHaut() &&
                this.getHaut() < obj.getBas();
    }

    public Point2D getPosition() {
        return position;
    }
}
