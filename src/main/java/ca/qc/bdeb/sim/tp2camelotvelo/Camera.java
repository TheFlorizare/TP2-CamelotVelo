package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;

public class Camera {

    private double x;
    private final double largeurEcran;

    public Camera(double largeurEcran) {
        this.largeurEcran = largeurEcran;
        this.x = 0;
    }

    public void update(Camelot camelot) {
        x = camelot.position.getX() - 0.2 * largeurEcran;
        if (x < 0) {
            x = 0;
        }
    }

    public Point2D coordEcran(Point2D positionMonde) {
        return positionMonde.subtract(x, 0);
    }

    public double getX() {
        return x;
    }
}
