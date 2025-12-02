package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Fenetre extends ObjetDuJeu {

    private boolean collisionne = false;
    private boolean abonnee;

    private Image imgFenetreNormale = new Image(getClass().getResourceAsStream("/Assets/fenetre.png"));
    private Image imgFenetreBriseeRouge = new Image(getClass().getResourceAsStream("/Assets/fenetre-brisee-rouge.png"));
    private Image imgFenetreBriseeVert = new Image(getClass().getResourceAsStream("/Assets/fenetre-brisee-vert.png"));
    private Image imgFenetreActuelle = imgFenetreNormale;

    public Fenetre(double x, double y, boolean abonnee) {
        this.position = new Point2D(x, y);
        this.taille = new Point2D(imgFenetreNormale.getWidth(), imgFenetreNormale.getHeight());
        this.abonnee = abonnee;
    }

    public void collisionAvecJournalFenetres(Journal j) {
        if (collisionne) return;

        if (this.collision(j)) {
            collisionne = true;
            j.detruire();
            briserFenetre();
        }
    }

    private void briserFenetre() {
        if (abonnee) {
            imgFenetreActuelle = imgFenetreBriseeRouge;
            Partie.nbArgent=Partie.nbArgent-2;
        } else {
            imgFenetreActuelle = imgFenetreBriseeVert;
            Partie.nbArgent=Partie.nbArgent+2;
        }
    }

    @Override
    public void draw(GraphicsContext context, Camera camera,boolean modeDebogage) {
        Point2D posEcran = camera.coordEcran(position);

        context.drawImage(
                imgFenetreActuelle,
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
