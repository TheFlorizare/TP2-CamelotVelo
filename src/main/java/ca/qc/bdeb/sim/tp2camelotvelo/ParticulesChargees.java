package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Random;

public class ParticulesChargees {

    Random r = new Random();
    public Point2D position;
    public double teinte = r.nextDouble(360);
    public Color couleur = Color.hsb(teinte, 1, 1);;

    public ParticulesChargees(double x, double y) {
        this.position = new Point2D(x, y);
    }

    public void draw(GraphicsContext context, Camera camera) {
        Point2D posEcran = camera.coordEcran(position);
        context.setFill(couleur);
        context.fillOval(posEcran.getX() - 10, posEcran.getY() - 10, 20, 20);
    }

}

