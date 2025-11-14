package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.scene.canvas.GraphicsContext;

import java.util.Random;

public class Partie {

    private int nombreFenetre() {
        Random r = new Random();
        int nbFenetreInitiale = 12;
        int ajoutFenetre = r.nextInt(12);// 12 fenetres initialement
        int nbFenetre = (nbFenetreInitiale + ajoutFenetre); // on ajoute 0-12 autres fenetres
        return nbFenetre;
    }

    private BoiteAuxLettre[] boites = new BoiteAuxLettre[12];
    private Fenetre[] fenetres = new Fenetre[nombreFenetre()];
    private Camelot camelot;
    private Camera camera;

    public void draw(GraphicsContext context) {

    }
    public void update(double deltaTemps) {


        for (int i = 0; i < boites.length; i++) {


        }

        for (var boite : boites)
            boite.draw(context);

    }
}
