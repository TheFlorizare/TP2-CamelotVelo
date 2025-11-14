package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Fenetre extends ObjetDuJeu {

    private Image fenetre;
    private Image fenetreRouge;
    private Image fenetreVert;

    @Override
    public void draw(GraphicsContext context) {

        var viewFenetre = new ImageView(fenetre);

        viewFenetre.setPreserveRatio(true);
    }
}
