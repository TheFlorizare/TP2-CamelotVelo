package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Light;

public abstract class ObjetDuJeu {

    protected Point2D position;
    protected Point2D velocite;
    protected Point2D acceleration;
    protected Point2D taille;

    public abstract void draw(GraphicsContext context);

    protected void updatePhysique(double deltaTemps) {

    }

    public void update(double deltaTemps) {

    }

    // this = journal, obj = fenetre ou boite
    public boolean collision(ObjetDuJeu obj) {
        return this.getDroite() > obj.getGauche() &&
                this.getGauche() < obj.getDroite() &&
                this.getBas() > obj.getHaut() &&
                this.getHaut() < obj.getBas();
    }

    public double getHaut() {
        return position.getY();
    }
    public double getBas() {
        return position.getY() + taille.getY();
    }
    public double getGauche() {
        return position.getX();
    }
    public double getDroite() {
        return position.getX() + taille.getX();
    }
    public Point2D getCentre() {
        return position.add(taille.multiply(1/2.0));
    }
}
