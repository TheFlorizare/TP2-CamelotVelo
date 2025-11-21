package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

public class Camelot extends ObjetDuJeu {

    protected Image camelot1 = new Image("camelot1.png");
    protected Image camelot2 = new Image("camelot2.png");
    protected ImageView camelotView = new  ImageView(camelot1);
    protected boolean toucherSol;
    private double tempsTotal = 0;

    @Override
    public void draw(GraphicsContext context) {
        context.drawImage(camelotView.getImage(), position.getX(), position.getY());
    }

    public Camelot() {
        position = new Point2D(
                MainJavaFX.WIDTH / 2.0 - taille.getX() / 2.0,
                MainJavaFX.HEIGHT - taille.getY());
        taille = new Point2D(172,144);
        acceleration = new Point2D(acceleration.getX(), 1500);
        toucherSol = true;
    }

    public void update(double deltaTemps) {

        tempsTotal += deltaTemps;
        int index = (int) (tempsTotal*4)%2;

        if (index == 0) {
            camelotView.setImage(camelot1);
        }
        else if (index == 1) {
            camelotView.setImage(camelot2);
        }

        boolean droite = Input.isKeyPressed(KeyCode.RIGHT);
        boolean gauche = Input.isKeyPressed(KeyCode.LEFT);
        boolean saut = Input.isKeyPressed(KeyCode.SPACE)
                || Input.isKeyPressed(KeyCode.UP);

        if (droite) {
            acceleration = new Point2D(+300, acceleration.getY());
        }
        else if (gauche) {
            acceleration = new Point2D(-300, acceleration.getY());
        }
        else {
            if (velocite.getX() < 400) {
                acceleration = new Point2D(+300, acceleration.getY());
            }
            else if (velocite.getX() > 400) {
                acceleration = new Point2D(-300, acceleration.getY());
            }
            else {
                acceleration = new Point2D(0, acceleration.getY());
            }
        }

        if (saut && toucherSol) {
            velocite = new Point2D(velocite.getX(), -500);
            toucherSol = false;
        }

        velocite = velocite.add(acceleration.multiply(deltaTemps));
        position = position.add(velocite.multiply(deltaTemps));

        if (position.getY() + taille.getY() >= MainJavaFX.HEIGHT) {
            position = new Point2D(position.getX(), MainJavaFX.HEIGHT - taille.getY());
            toucherSol = true;
            velocite = new Point2D(velocite.getX(), 0);
        }

        position = new Point2D(
                Math.clamp(position.getX(), 0, MainJavaFX.WIDTH - taille.getX()),
                Math.clamp(position.getY(), 0, MainJavaFX.HEIGHT - taille.getY()));

        velocite = new Point2D(Math.clamp(velocite.getX(), 200, 600), velocite.getY());
    }
}
