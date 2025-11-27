package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Random;

public class Partie {
    private Maison[] maisons = new Maison[12];
    private Random r = new Random();
    private BoiteAuxLettre[] boites = new BoiteAuxLettre[12];
    private Fenetre[] fenetres = new Fenetre[nombreFenetre()];
    public static ArrayList<Journal> journaux = new ArrayList<>();
    private ArrayList<ParticulesChargees> particules = new  ArrayList<>();
    public double masseJournaux;
    private Camelot camelot;
    private Camera camera;
    private Image brique;
    private Image ImgJournal = new Image(getClass().getResourceAsStream("/Assets/icone-journal.png"));
    private Image ImgArgent = new Image(getClass().getResourceAsStream("/Assets/icone-dollar.png"));
    private Image ImgMaison = new Image(getClass().getResourceAsStream("/Assets/icone-maison.png"));
    private Image ImgPorte = new Image(getClass().getResourceAsStream("/Assets/porte.png"));
    public static final double LARGEUR_NIVEAU = 999999999;
    private int nbJournal = 12;
    private int nbArgent = 0;
    private boolean modeDegogage = false;
    private boolean debogageChampE = false;


    private int nombreFenetre() {
        Random r = new Random();
        int nbFenetreInitiale = 12;
        int ajoutFenetre = r.nextInt(12);
        int nbFenetre = (nbFenetreInitiale + ajoutFenetre);
        return nbFenetre;
    }


    Partie() {
        camelot = new Camelot(this);
        camera = new Camera(MainJavaFX.WIDTH);
        brique = new Image(getClass().getResource("/Assets/brique.png").toExternalForm());
        int adresse = 100 + r.nextInt(851);
        double xMaison = 1300;
        masseJournaux = r.nextDouble(2);

        for (int i = 0; i < maisons.length; i++) {

            boolean abonnee = r.nextBoolean();

            maisons[i] = new Maison(xMaison, adresse, abonnee);

            adresse += 2;

            xMaison += 1300;
        }



    }

    public void draw(GraphicsContext context) {


        double w = brique.getWidth();
        double h = brique.getHeight();
        context.setFill(Color.rgb(0, 0, 0, 0.5));


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

        context.setFill(Color.rgb(255, 255, 255, 0.5));
        context.setFont(new Font(30));
        context.fillText(String.valueOf(nbJournal), ImgJournal.getWidth() + 20, 35);
        context.fillText(String.valueOf(nbArgent) + "$", 170, 35);

        for (Maison m : maisons) {
            m.draw(context, camera);
        }

        // dessine les journaux
        for (Journal j : journaux) {
            j.draw(context);
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

    public void update(double deltaTemps) {

        camelot.update(deltaTemps);
        camera.update(camelot);

        for (Journal j : journaux) {
            j.update(deltaTemps);

            if (!j.estPresent()) {
                journaux.remove(j);
            }

            for (BoiteAuxLettre b : boites) {
                if (j.collision(b)) {
                    collisionBoite(j,b);
                }
            }

            for (Fenetre f : fenetres) {
                if (j.collision(f)) {
                    collisionFenetre(j,f);
                }
            }
        }

    }

    private void collisionBoite(Journal j, BoiteAuxLettre b) {

        j.detruire();

        if (!b.devientVert()) {
            nbArgent += 1;
        }
    }

    private void collisionFenetre(Journal j, Fenetre f) {

        j.detruire();

        if (!f.estCasseVert()) {
            nbArgent += 2;
        }

        if (!f.estCasseRouge()) {
            nbArgent -= 2;
        }
    }

    public Point2D champElectrique(ArrayList<ParticulesChargees> particules, Point2D position) {

        Point2D total = Point2D.ZERO;

        for (ParticulesChargees p : particules) {

            double dx = position.getX() - p.position.getX(); // posX journal - posX particule
            double dy = position.getY() - p.position.getY();
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance < 1) {
                distance = 1;
            }

            double Ei = 90*900 / (distance*distance); // K*q/r2

            Point2D direction = new Point2D(dx, dy).normalize();

            total = total.add(direction.multiply(Ei));
        }
        return total;
    }

    public ArrayList<ParticulesChargees> getParticules() {
        return particules;
    }

    public void debogageChampElectrique(ArrayList<ParticulesChargees> particules) {

        for (double x = 0; x < LARGEUR_NIVEAU; x += 50) {
            for (double y = 0; y < HAUTEUR_ECAN; y += 50) {
                var positionMonde = new Point2D(x, y);
                var positionEcran = // calculez ça selon votre objet Camera
// TODO: Seulement faire ça si la position (x, y) est visible dans l'écran
Point2D force = champElectrique(particules, positionMonde);
                UtilitairesDessins.dessinerVecteurForce(
                        positionEcran,
                        force,
                        contexteGraphique
                );
            }
        }
    }
}

