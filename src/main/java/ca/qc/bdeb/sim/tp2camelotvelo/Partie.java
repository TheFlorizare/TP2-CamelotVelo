package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Light;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Random;

public class Partie {

    private final Random r = new Random();

    private final Camelot camelot;
    private final Camera camera;

    private final Maison[] maisons = new Maison[12];
    private final BoiteAuxLettre[] boites = new BoiteAuxLettre[12];
    private final Fenetre[] fenetres = new Fenetre[nombreFenetre()];
    public static ArrayList<ParticulesChargees> particules = new ArrayList<>();
    public static ArrayList<ParticulesChargees> particulesDebogage = new ArrayList<>();
    public static ArrayList<Journal> journaux = new ArrayList<>();

    private final Image brique;
    private final Image ImgJournal = new Image(getClass().getResourceAsStream("/Assets/icone-journal.png"));
    private final Image ImgArgent = new Image(getClass().getResourceAsStream("/Assets/icone-dollar.png"));
    private final Image ImgMaison = new Image(getClass().getResourceAsStream("/Assets/icone-maison.png"));
    private final Image ImgPorte = new Image(getClass().getResourceAsStream("/Assets/porte.png"));

    public static double masseJournaux;
    public static final double LARGEUR_NIVEAU = 100000;
    private int nbJournal = 12;
    private int nbArgent = 0;
    private double espace = 300;
    private boolean niveau2 = false;

    private boolean modeDebogage = false;
    private boolean modeDebogageChamp = false;
    private boolean modeDebogageChampTest = false;
    private boolean creationParticulesDebogage = false;

    private int nombreFenetre() {
        Random r = new Random();

        int nbFenetreInitiale = 12;
        int ajoutFenetre = r.nextInt(12);
        int fenetreTotal = (nbFenetreInitiale + ajoutFenetre);
        return fenetreTotal;
    }

    Partie() {

        camelot = new Camelot(this);
        camera = new Camera(MainJavaFX.WIDTH);
        brique = new Image(getClass().getResource("/Assets/brique.png").toExternalForm());

        int adresse = 100 + r.nextInt(851);
        double xMaison = 1300;
        masseJournaux = r.nextDouble(2);
        creationParticules();

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
        context.fillText(String.valueOf(journaux.size()), ImgJournal.getWidth() + 20, 35);
        context.fillText(String.valueOf(nbArgent) + "$", 170, 35);

        for (Maison m : maisons) {
            m.draw(context, camera);
            if (m.isAbonnee()) {
                context.fillText(String.valueOf(m.getAdresse()), espace, 35);
                espace += 55;
            }
        }
        espace = 250;

        for (Journal j : journaux) {
            j.draw(context, camera);
        }

        camelot.draw(context, camera);

        if (!niveau2) {
            for (ParticulesChargees p : particules) {
                p.draw(context, camera);
            }
        }

        if (modeDebogageChamp) {
            if (!niveau2) {
                for (double x = 0; x < LARGEUR_NIVEAU; x +=50) {
                    for (double y = 0; y < MainJavaFX.HEIGHT; y +=50) {

                        var positionMonde = new Point2D(x,y);
                        var positionEcran = camera.coordEcran(positionMonde);

                        if (positionEcran.getX() < 0 || positionEcran.getX() > MainJavaFX.WIDTH) {
                            continue;
                        }

                        Point2D force = champElectrique(particules, positionMonde);

                        UtilitairesDessins.dessinerVecteurForce(positionEcran, force, context);
                    }
                }
            }
            else {
                return;
            }
        }
        else if (modeDebogageChampTest) {
            for (double x = 0; x < LARGEUR_NIVEAU; x +=50) {
                for (double y = 0; y < MainJavaFX.HEIGHT; y +=50) {

                    var positionMonde = new Point2D(x,y);
                    var positionEcran = camera.coordEcran(positionMonde);

                    if (positionEcran.getX() < 0 || positionEcran.getX() > MainJavaFX.WIDTH) {
                        continue;
                    }

                    Point2D force = champElectrique(particulesDebogage, positionMonde);

                    UtilitairesDessins.dessinerVecteurForce(positionEcran, force, context);
                }
            }
        }

        if (modeDebogage) {
            for (Journal j : journaux) {
                Point2D position = camera.coordEcran(j.position);
                context.strokeRect(position.getX(), position.getY(), j.taille.getX(), j.taille.getY());
            }

            for (Fenetre f : fenetres) {
                Point2D positionFenetre = camera.coordEcran(f.position);
                context.strokeRect(positionFenetre.getX(), positionFenetre.getY(), f.taille.getX(), f.taille.getY());
            }

            for (BoiteAuxLettre b : boites) {
                Point2D positionBoite = camera.coordEcran(b.position);
                context.strokeRect(positionBoite.getX(), positionBoite.getY(), b.taille.getX(), b.taille.getY());
            }

            // Dessin de la boite de collision du camelot
            Point2D positionCamelot = camera.coordEcran(camelot.position);
            context.strokeRect(positionCamelot.getX(), positionCamelot.getY(), camelot.taille.getX(), camelot.taille.getY());

            context.setLineWidth(2);
            context.setStroke(Color.YELLOW);
        }

        if (modeDebogageChampTest && !creationParticulesDebogage) {

            particulesDebogage.clear();

            double haut = 10;
            double bas = MainJavaFX.HEIGHT - 10;
            double espace = 0;


            for (int i = 0; i < 100; i++) {

                particulesDebogage.add(new ParticulesChargees(espace, haut));
                particulesDebogage.add(new ParticulesChargees(espace, bas));
                espace += 50;

            }

            creationParticulesDebogage = true;
        }

        if (modeDebogageChampTest) {

            for (ParticulesChargees p : particulesDebogage) {
                Point2D position = camera.coordEcran(p.position);
                context.setFill(p.couleur);
                context.fillOval(position.getX() - 4, position.getY() - 4, 20, 20);
            }
        }
    }

    public void update(double deltaTemps) {

        camelot.update(deltaTemps);
        camera.update(camelot);

        for (int i = journaux.size() - 1; i >= 0; i--) {
            Journal j = journaux.get(i);

            j.update(deltaTemps);

            if (!j.estPresent()) {
                journaux.remove(i);
                continue;
            }

            for (BoiteAuxLettre b : boites) {
                if (b != null && j.collision(b)) {
                    collisionBoite(j, b);
                }
            }

            for (Fenetre f : fenetres) {
                if (f != null && j.collision(f)) {
                    collisionFenetre(j, f);
                }
            }
        }
    }

    public void creationParticules() {
        if (!niveau2) {
            for (int i = 0; i<100; i++) {
                particules.add(new ParticulesChargees(r.nextDouble(LARGEUR_NIVEAU), r.nextDouble(MainJavaFX.HEIGHT)));
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

    public ArrayList<ParticulesChargees> getParticulesDebogage() {
        return particulesDebogage;
    }

    public void ajoutJournauxDebogage() {
        nbJournal += 10;
    }

    public void suppressionJournauxDebogage() {
        nbJournal = 0;
    }

    public void prochainNiveauDebogage() {
        niveau2 = true;
    }

    public void activerDebogage() {
        modeDebogage = !modeDebogage;
    }

    public void activerDebogageChamp() {
        modeDebogageChamp = !modeDebogageChamp;
    }

    public void activerDebogageChampTest() {
        modeDebogageChampTest = !modeDebogageChampTest;
    }
}

