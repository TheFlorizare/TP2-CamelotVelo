package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

import java.security.Key;
import java.util.Random;

public class Journal extends ObjetDuJeu {

    Random r = new Random();
    protected Image journal = new Image("/assets/journal.png");
    protected double masse = r.nextDouble(2);;
    private double cooldown = 0;
    private boolean presence = true;


    public Journal(Point2D positionCamelot, Point2D vitesseCamelot, Point2D quantiteMouvement, double masseJournal) {

        this.velocite = vitesseCamelot.add(quantiteMouvement.multiply(1.0/masseJournal));
        this.position = positionCamelot;
        this.masse = masseJournal;
        this.acceleration = Point2D.ZERO;

        taille = new Point2D(52,31);
    }

    @Override
    public void draw(GraphicsContext context) {
        context.drawImage(
                journal,
                position.getX(),
                position.getY(),
                taille.getX(),
                taille.getY());
    }

    public void update(double deltaTemps) {

        Point2D gravite = new Point2D(0, 1500);

        acceleration = gravite;

        velocite = velocite.add(acceleration.multiply(deltaTemps));

        double max = 1500;
        double moduleVitesse = velocite.magnitude();
        if (moduleVitesse > max) {
            velocite = velocite.multiply(max/moduleVitesse);
        }

        position = position.add(velocite.multiply(deltaTemps));

        if (cooldown <= 0 && Input.isKeyPressed(KeyCode.Z)) {
            lancerJournal(new Point2D(900, -900));
        }

        if (cooldown <= 0 && Input.isKeyPressed(KeyCode.X)) {
            lancerJournal(new Point2D(150, -1100));
        }

        cooldown -= deltaTemps;
    }

    public void lancerJournal(Point2D projection) {

        if (Input.isKeyPressed(KeyCode.SHIFT)) {
            projection = projection.multiply(1.5);
        }

        Point2D creationPositionJournal = new Point2D(
                position.getX() + taille.getX() / 2,
                position.getY() + taille.getY());

        Journal j = new Journal(creationPositionJournal, this.velocite, projection, this.masse);



        cooldown = 0.5;
    }

    public void detruire() {
        presence = false;
    }
}
