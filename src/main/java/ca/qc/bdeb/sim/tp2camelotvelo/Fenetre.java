package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Fenetre extends ObjetDuJeu {

    private boolean collisionne = false;
    private boolean abonnee;

    private Image fenetre = new Image("/Assets/fenetre.png");
    private Image fenetreRouge = new Image("/Assets/fenetreRouge.png");
    private Image fenetreVert = new Image("/Assets/fenetreVert.png");
    private ImageView viewFenetre;

    public Fenetre(double x, double y, boolean abonnee) {
        this.position = new Point2D(x,y);
        this.taille = new Point2D(159,130);
        this.abonnee = abonnee;
    }





    public boolean estCasseVert() {
        if (abonnee) {
            viewFenetre = new ImageView(fenetreVert);
        }
        return false;
    }

    public boolean estCasseRouge() {
        if (abonnee) {
            viewFenetre = new ImageView(fenetreRouge);
        }
        return false;
    }

    @Override
    public void draw(GraphicsContext context, Camera camera) {

    }
}
