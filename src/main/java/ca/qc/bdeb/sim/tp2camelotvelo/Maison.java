package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Maison {

    private double x;
    private int adresse;
    private boolean abonnee;
    private Image porte = new Image(getClass().getResourceAsStream("/Assets/porte.png"));;


    public Maison(double x, int adresse, boolean abonnee) {
        this.x = x;
        this.adresse = adresse;
        this.abonnee = abonnee;

    }


    public void draw(GraphicsContext context, Camera camera) {

        Point2D posMonde = new Point2D(x, 250);
        Point2D posEcran = camera.coordEcran(posMonde);


        double dx = posEcran.getX() + 70;
        double dy = MainJavaFX.HEIGHT-porte.getHeight();

        context.drawImage(porte, dx, dy);

        context.setFill(Color.YELLOW);
        context.setFont(Font.font(30));
        context.fillText(Integer.toString(adresse),
                posEcran.getX() + 120,
                MainJavaFX.HEIGHT-porte.getHeight()*0.75);
    }
    public int getAdresse() {
        return adresse;
    }
    public boolean isAbonnee() {
        return abonnee;
    }

}

