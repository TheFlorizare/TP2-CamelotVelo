package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Random;

public class Partie {

    private BoiteAuxLettre[] boites = new BoiteAuxLettre[12];
    private Fenetre[] fenetres = new Fenetre[nombreFenetre()];
    public Journal[] journaux = new Journal[16];
    private Camelot camelot;
    private Camera camera;
    private Image brique;
    public static final double LARGEUR_NIVEAU = 999999999;



    private int nombreFenetre() {
        Random r = new Random();
        int nbFenetreInitiale = 12;
        int ajoutFenetre = r.nextInt(12);// 12 fenetres initialement
        int nbFenetre = (nbFenetreInitiale + ajoutFenetre); // on ajoute 0-12 autres fenetres
        return nbFenetre;
    }

    Partie() {
        camelot = new Camelot();
        camera = new Camera(MainJavaFX.WIDTH);
        brique = new Image(getClass().getResource("/Assets/brique.png").toExternalForm());

    }

    public void draw(GraphicsContext context) {


        double w = brique.getWidth();
        double h = brique.getHeight();

        for (double x = 0; x < LARGEUR_NIVEAU; x += w) {
            for (double y = 0; y < MainJavaFX.HEIGHT; y += h) {
                var posEcran = camera.coordEcran(new Point2D(x, y));
                // dessiner seulement si visible
                if (posEcran.getX() + w < 0 || posEcran.getX() > MainJavaFX.WIDTH) continue;
                context.drawImage(brique, posEcran.getX(), posEcran.getY(), w, h);
            }
        }


        // for (var boite : boites)
        //boite.draw(context);

        // for (var fenetre : fenetres)
        // fenetre.draw(context);

        Point2D posMonde = camelot.getPosition();
        Point2D posEcran = camera.coordEcran(posMonde);
        context.drawImage(
                camelot.getImageCourante(),
                posEcran.getX(),
                posEcran.getY(),
                camelot.getTaille().getX(),
                camelot.getTaille().getY()
        );


    }

    private void dessinerFond(GraphicsContext context) {
        double w = brique.getWidth();
        double h = brique.getHeight();

        for (double x = 0; x < MainJavaFX.WIDTH; x += w) {
            for (double y = 0; y < MainJavaFX.HEIGHT; y += h) {
                context.drawImage(brique, x, y, w, h);
            }
        }
    }

    public void update(double deltaTemps) {

        camelot.update(deltaTemps);
        camera.update(camelot);
    }
}

