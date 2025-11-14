package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Camelot extends ObjetDuJeu {

    private Image camelot1 = new Image("camelot1.png");;
    private Image camelot2 = new Image("camelot2.png");

    @Override
    public void draw(GraphicsContext context) {

        var viewCamelot1 = new ImageView(camelot1);

        viewCamelot1.setPreserveRatio(true);
    }
}
