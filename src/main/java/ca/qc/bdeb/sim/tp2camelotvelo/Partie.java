package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.util.Random;

public class Partie {

    private BoiteAuxLettre[] boites = new BoiteAuxLettre[12];
    private Fenetre[] fenetres = new Fenetre[nombreFenetre()];
    private Camelot camelot;
    private Camera camera;
    private Image brique;
    private Image ImgJournal = new Image(getClass().getResourceAsStream("/Assets/icone-journal.png"));
    private Image ImgArgent = new Image(getClass().getResourceAsStream("/Assets/icone-dollar.png"));
    private Image ImgMaison = new Image(getClass().getResourceAsStream("/Assets/icone-maison.png"));
    public static final double LARGEUR_NIVEAU = 999999999;
    private int nbJournal = 12;
    private int nbArgent = 0;





    private int nombreFenetre() {
        Random r = new Random();
        int nbFenetreInitiale = 12;
        int ajoutFenetre = r.nextInt(12);
        int nbFenetre = (nbFenetreInitiale + ajoutFenetre);
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
        context.setFill(Color.rgb(0,0,0,0.5));



        for (double x = 0; x < LARGEUR_NIVEAU; x += w) {
            for (double y = 0; y < MainJavaFX.HEIGHT; y += h) {
                var posEcran = camera.coordEcran(new Point2D(x, y));

                if (posEcran.getX() + w < 0 || posEcran.getX() > MainJavaFX.WIDTH)
                    continue;

                context.drawImage(brique, posEcran.getX(), posEcran.getY(), w, h);
            }
        }

        context.fillRect(0, 0, MainJavaFX.WIDTH, 50);
        context.drawImage(ImgJournal, 10, 10);

        context.drawImage(ImgArgent, 110, 15);

        context.drawImage(ImgMaison, 210, 10);

        context.setFill(Color.rgb(255,255,255,0.5));
        context.setFont(new Font(30));
        context.fillText(String.valueOf(nbJournal),ImgJournal.getWidth()+20,35);
        context.fillText(String.valueOf(nbArgent)+"$",170,35);


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


    public void update(double deltaTemps) {

        camelot.update(deltaTemps);
        camera.update(camelot);
    }
}
