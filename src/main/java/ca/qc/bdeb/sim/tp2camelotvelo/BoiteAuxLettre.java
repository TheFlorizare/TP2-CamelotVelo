package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;


public class BoiteAuxLettre extends ObjetDuJeu {

    private boolean collisionne = false;
    private boolean abonnee;

    private Image imgBoiteNormale = new Image(getClass().getResourceAsStream("/Assets/boite-aux-lettres.png"));
    private Image imgBoiteRouge = new Image(getClass().getResourceAsStream("/Assets/boite-aux-lettres-rouge.png"));
    private Image imgBoiteVerte = new Image(getClass().getResourceAsStream("/Assets/boite-aux-lettres-vert.png"));
    private Image imgBoiteActuelle = imgBoiteNormale;

    public BoiteAuxLettre(double x, double y, boolean abonnee) {
        this.position = new Point2D(x, y);
        this.taille = new Point2D(imgBoiteNormale.getWidth(), imgBoiteNormale.getHeight());
        this.abonnee = abonnee;
    }


    public void collisionAvecJournalBoites(Journal j) {

        if (collisionne) return;


        if (this.collision(j)) {
            collisionne = true;
            j.detruire();
            changerCouleurBoite();
        }
    }

    private void changerCouleurBoite() {
        if (abonnee) {
            imgBoiteActuelle = imgBoiteVerte;
            Partie.nbArgent=Partie.nbArgent+1;
        } else {
            imgBoiteActuelle = imgBoiteRouge;
        }
    }

    @Override
    public void draw(GraphicsContext context, Camera camera,boolean modeDebogage) {
        Point2D posEcran = camera.coordEcran(position);

        context.drawImage(
                imgBoiteActuelle,
                posEcran.getX(),
                posEcran.getY(),
                taille.getX(),
                taille.getY()
        );
        if (modeDebogage) {
            context.setStroke(Color.YELLOW);
            context.strokeRect(
                    posEcran.getX(),
                    posEcran.getY(),
                    taille.getX(),
                    taille.getY()
            );
        }

    }

    @Override
    public void update(double deltaTemps) {
    }
}
