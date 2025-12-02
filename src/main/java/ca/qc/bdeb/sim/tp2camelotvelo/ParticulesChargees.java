package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Random;

public class ParticulesChargees extends ObjetDuJeu {

    Random r = new Random();
    public Point2D position;
    public double teinte = r.nextDouble(360);
    public Color couleur;

    public ParticulesChargees(double x, double y) {
        this.position = new Point2D(x, y);

        double teinte = Math.random() * 360;
        this.couleur = Color.hsb(teinte, 1, 1);
    }
    @Override
    public void draw(GraphicsContext context, Camera camera,boolean modeDebogage) {
        Point2D posEcran = camera.coordEcran(position);
        context.setFill(couleur);
        context.fillOval(posEcran.getX() - 10, posEcran.getY() - 10, 20, 20);
    }

    @Override
    public void update(double deltaTemps) {

    }
}

