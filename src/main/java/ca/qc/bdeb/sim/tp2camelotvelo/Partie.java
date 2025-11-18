package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.scene.canvas.GraphicsContext;

import java.util.Random;

public class Partie {

    private BoiteAuxLettre[] boites = new BoiteAuxLettre[12];
    private Fenetre[] fenetres = new Fenetre[nombreFenetre()];
    private Camelot camelot;
    private Camera camera;

    private int nombreFenetre() {
        Random r = new Random();
        int nbFenetreInitiale = 12;
        int ajoutFenetre = r.nextInt(12);// 12 fenetres initialement
        int nbFenetre = (nbFenetreInitiale + ajoutFenetre); // on ajoute 0-12 autres fenetres
        return nbFenetre;
    }

    Partie() {
        this.camelot = new Camelot();
        this.camera = new Camera(camelot);


    }

    public void draw(GraphicsContext context) {

        camelot.draw(context);

        for (var boite : boites)
            boite.draw(context);

        for (var fenetre : fenetres)
            fenetre.draw(context);

    }
    public void update(double deltaTemps) {

        camelot.update(deltaTemps);
        camera.update(camelot);
    }
}
