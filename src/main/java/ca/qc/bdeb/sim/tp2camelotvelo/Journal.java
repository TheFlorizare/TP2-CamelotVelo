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

    public Journal(Point2D positionCamelot, Point2D vitesseCamelot, Point2D quantiteMouvement, double masseJournal) {

        Point2D vitesseInitale = vitesseCamelot + quantiteMouvement/masseJournal;
        this.velocite = vitesseCamelot;
        this.position = positionCamelot;
        this.masse = masseJournal;

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

        acceleration = new Point2D(0, 1500);

        velocite = velocite.add(acceleration.multiply(deltaTemps));

        double module = velocite.magnitude();
        if (module > 1500) {
            velocite = velocite.multiply(1500 / module);
        }
        position = position.add(velocite.multiply(deltaTemps));

        cooldown -= deltaTemps;
    }

    public void lancerJournal(Point2D projection) {

        if (Input.isKeyPressed(KeyCode.SHIFT)) {
            projection = projection.multiply(1.5);
        }

        Point2D creationJournal = new Point2D(
                position.getX() + taille.getX() / 2,
                position.getY() + taille.getY());

        Journal x = new Journal(creationJournal, velocite, projection, masse);

        cooldown = 0.5;

        if (cooldown <= 0 && Input.isKeyPressed(KeyCode.Z)) {
            lancerJournal(new Point2D(900, -900));
        }

        if (cooldown <= 0 && Input.isKeyPressed(KeyCode.X)) {
            lancerJournal(new Point2D(150, -1100));
        }
    }

}
