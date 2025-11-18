package ca.qc.bdeb.sim.tp2camelotvelo;


import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public class Camera extends ObjetDuJeu {
    public Camera(Camelot c) {
        this.position = new Point2D(c.position.getX(), 0);
        this.acceleration = new Point2D(c.position.getX(), 0);
        this.velocite = new Point2D(c.position.getX(), 0);
    }

    public Point2D coordEcran(Point2D positionMonde) {
        return positionMonde.subtract(position);
    }

    public void update(Camelot c) {

        position = new Point2D(c.position.getX(), 0);
        acceleration = new Point2D(c.position.getX(), 0);
        velocite = new Point2D(c.position.getX(), 0);
    }

    public void draw(GraphicsContext context) {

    }

}
