package ca.qc.bdeb.sim.tp2camelotvelo;

import com.sun.tools.javac.Main;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Light;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

public class Camelot extends ObjetDuJeu {

    protected Image camelot1 = new Image("camelot1.png");
    protected Image camelot2 = new Image("camelot2.png");
    protected boolean toucherSol;

    @Override
    public void draw(GraphicsContext context) {

        var viewCamelot1 = new ImageView(camelot1);

        viewCamelot1.setPreserveRatio(true);
    }

    public Camelot() {
        position = new Point2D(
                MainJavaFX.WIDTH / 2.0 - taille.getX() / 2.0,
                MainJavaFX.HEIGHT - taille.getY());

        toucherSol = true;
    }

    public void update(double deltaTemps) {
        boolean droite = Input.isKeyPressed(KeyCode.RIGHT);
        boolean gauche = Input.isKeyPressed(KeyCode.LEFT);
        boolean saut = Input.isKeyPressed(KeyCode.SPACE)
                || Input.isKeyPressed(KeyCode.UP);

        if (droite) {
            velocite = new Point2D(+300, velocite.getY());
        } else if (gauche) {
            velocite = new Point2D(-300, velocite.getY());
        } else {
            velocite = new Point2D(400, velocite.getY());
        }

        if (saut) {
            velocite = new Point2D(velocite.getX(), -500);
        }

        velocite = velocite.add(acceleration.multiply(deltaTemps));
        position = position.add(velocite.multiply(deltaTemps));

        if (position.getY() + taille.getY() >= MainJavaFX.HEIGHT) {
            toucherSol = true;
            velocite = new Point2D(velocite.getX(), 0);
        }

        position = new Point2D(
                Math.clamp(position.getX(), 0, MainJavaFX.WIDTH - taille.getX()),
                Math.clamp(position.getY(), 0, MainJavaFX.HEIGHT - taille.getY()));
    }
}
