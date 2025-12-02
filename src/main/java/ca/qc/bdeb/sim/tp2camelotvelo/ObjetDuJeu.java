package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Light;

public abstract class ObjetDuJeu {

    protected Point2D position;
    protected Point2D velocite;
    protected Point2D acceleration;
    protected Point2D taille;
    protected boolean x = true;

    public abstract void draw(GraphicsContext context, Camera camera, boolean modeDebogage);

    public abstract void update(double deltaTemps);

    public boolean collision(ObjetDuJeu autre) {
        // Cette partie a été faites a l'aide de recherches internet et l'aide de ChatGPT
        if (this.position == null || this.taille == null ||
                autre.position == null || autre.taille == null) {
            return false;
        }

        double thisGauche = this.position.getX();
        double thisDroite = thisGauche + this.taille.getX();
        double thisHaut = this.position.getY();
        double thisBas = thisHaut + this.taille.getY();

        double otherGauche = autre.position.getX();
        double otherDroite = otherGauche + autre.taille.getX();
        double otherHaut = autre.position.getY();
        double otherBas = otherHaut + autre.taille.getY();

        if (thisDroite <= otherGauche) return false;
        if (thisGauche >= otherDroite) return false;
        if (thisBas <= otherHaut) return false;
        if (thisHaut >= otherBas) return false;

        return true;
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
        return position.add(taille.multiply(1 / 2.0));
    }

}
