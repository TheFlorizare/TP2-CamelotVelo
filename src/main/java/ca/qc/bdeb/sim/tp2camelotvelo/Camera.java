package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public class Camera {

    public Point2D coordEcran(Point2D positionMonde) {
        return positionMonde.subtract(positionCamera);
    }

    public void update(double deltaTemps) {

        positionCamera = positionCamera.add(velocite.multiply(deltaTemps));
    }

    public void draw(GraphicsContext context) {
        context.setFill(color);

        var coordoEcran = camera.coordoEcran(position);

        context.fillRect(
                position.getX(), position.getY(), taille.getX(), taille.getY());

    }

}
