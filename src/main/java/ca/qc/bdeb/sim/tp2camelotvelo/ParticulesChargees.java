package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.Camera;
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
        context.setFill(couleur);
        context.fillOval(position.getX() - 10, position.getY() - 10, 20, 20);
    }
}
