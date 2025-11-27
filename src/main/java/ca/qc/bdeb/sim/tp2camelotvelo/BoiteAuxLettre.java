package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BoiteAuxLettre extends ObjetDuJeu {

    private boolean collisionne = false;
    private boolean abonnee;

    private Image boite = new Image(getClass().getResourceAsStream("boite-aux-lettres.png"));
    private Image boiteRouge = new Image(getClass().getResourceAsStream("boite-aux-lettres-rouge.png"));
    private Image boiteVert = new Image(getClass().getResourceAsStream("boite-aux-lettres-vert.png"));
    private ImageView viewBoite;
    public BoiteAuxLettre(double x, double y, boolean abonnee) {
        this.position = new Point2D(x,y);
        this.taille = new Point2D(81,76);
        this.abonnee = abonnee;
    }
    @Override
    public void draw(GraphicsContext context) {

        context.drawImage(
                boite,
                position.getX(),
                position.getY(),
                taille.getX(),
                taille.getY());
    }

    public boolean devientVert() {
        if (abonnee) {
            viewBoite.setImage(boiteVert);
        }
        return false;
    }

    public boolean devientRouge() {
        if (!abonnee) {
            viewBoite.setImage(boiteRouge);
        }
        return false;
    }
}
