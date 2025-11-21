package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BoiteAuxLettre extends ObjetDuJeu {

    private Image boite = new Image("boite-aux-lettres.png");
    private Image boiteRouge = new Image("boite-aux-lettres-rouge.png");
    private Image boiteVert = new Image("boite-aux-lettres-vert.png");

    @Override
    public void draw(GraphicsContext context) {

        var viewBoite = new ImageView(boite);

        viewBoite.setPreserveRatio(true);
    }

    public void update(double deltaTemps) {

    }
}
