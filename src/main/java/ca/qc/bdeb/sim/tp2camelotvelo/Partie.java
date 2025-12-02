package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Random;

public class Partie {
    public static double masseJournaux;
    private Maison[] maisons = new Maison[12];
    private Random r = new Random();
    private BoiteAuxLettre[] boites = new BoiteAuxLettre[12];
    private ArrayList<Fenetre>[] fenetresParMaison = new ArrayList[12];
    public static ArrayList<Journal> journaux = new ArrayList<>();
    public static ArrayList<ParticulesChargees> particules = new ArrayList<>();
    private Camelot camelot;
    private Camera camera;
    private Image brique;
    private Image ImgJournal;
    private Image ImgArgent;
    private Image ImgMaison;
    private Image ImgPorte;
    public static final double LARGEUR_NIVEAU = 999999999;
    public static int nbJournal = 12;
    public static int nbArgent = 0;
    private int niveau;
    private double espace = 300;

    private boolean modeDebogage = false;
    private boolean modeDebogageChamp = false;

    private double xFinNiveau;
    private boolean niveauTermine = false;


    public Partie(int niveau) {

        this.niveau = niveau;


        masseJournaux = r.nextDouble(2);
        journaux.clear();
        particules.clear();


        camelot = new Camelot(this);
        camera = new Camera(MainJavaFX.WIDTH);


        brique = new Image(getClass().getResource("/Assets/brique.png").toExternalForm());
        ImgJournal = new Image(getClass().getResourceAsStream("/Assets/icone-journal.png"));
        ImgArgent = new Image(getClass().getResourceAsStream("/Assets/icone-dollar.png"));
        ImgMaison = new Image(getClass().getResourceAsStream("/Assets/icone-maison.png"));
        ImgPorte = new Image(getClass().getResourceAsStream("/Assets/porte.png"));


        if (niveau >= 2) {
            creationParticules();
        }


        int adresse = 100 + r.nextInt(851);
        double xMaison = 1300;

        for (int i = 0; i < maisons.length; i++) {

            boolean abonnee = r.nextBoolean();
            maisons[i] = new Maison(xMaison, adresse, abonnee);

            adresse += 2;
            xMaison += 1300;
        }
        xFinNiveau = xMaison;


        for (int i = 0; i < fenetresParMaison.length; i++) {

            fenetresParMaison[i] = new ArrayList<>();
            Maison m = maisons[i];

            int nbF = r.nextInt(3); // 0, 1 ou 2 fenêtres

            for (int f = 0; f < nbF; f++) {

                double xFenetre = m.getPosition(camera).getX() + 400 + f * 250;
                double yFenetre = m.getPosition(camera).getY() - 175;

                boolean abonneeFenetre = m.isAbonnee();

                fenetresParMaison[i].add(
                        new Fenetre(xFenetre, yFenetre, abonneeFenetre)
                );
            }
        }


        for (int i = 0; i < boites.length; i++) {

            Maison m = maisons[i];

            double xBoite = m.getPosition(camera).getX() + 250;

            double minY = 200;
            double maxY = MainJavaFX.HEIGHT - 150;
            double yBoite = minY + r.nextDouble() * (maxY - minY);

            boolean abonnee = m.isAbonnee();

            boites[i] = new BoiteAuxLettre(xBoite, yBoite, abonnee);
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
        context.fillText(nbArgent + "$", 170, 35);

        for (Maison m : maisons) {
            m.draw(context, camera);
            if (m.isAbonnee()) {
                context.fillText(String.valueOf(m.getAdresse()), espace, 35);
                espace += 55;
            }
        }
        for (BoiteAuxLettre b : boites) {
            if (b != null) {
                b.draw(context, camera, modeDebogage);
            }
        }

        for (ArrayList<Fenetre> liste : fenetresParMaison) {
            for (Fenetre f : liste) {
                f.draw(context, camera, modeDebogage);
            }
        }


        espace = 250;
        for (Journal j : journaux) {
            j.draw(context, camera, modeDebogage);
        }


        camelot.draw(context, camera, modeDebogage);

        if (niveau >= 2) {
            for (ParticulesChargees p : particules) {
                p.draw(context, camera, modeDebogage);
            }
        }
        if (modeDebogageChamp && niveau >= 2) {

            for (double x = 0; x < LARGEUR_NIVEAU; x += 50) {
                for (double y = 0; y < MainJavaFX.HEIGHT; y += 50) {

                    Point2D posMonde = new Point2D(x, y);
                    Point2D posEcran = camera.coordEcran(posMonde);


                    if (posEcran.getX() < 0 || posEcran.getX() > MainJavaFX.WIDTH) continue;

                    Point2D force = champElectrique(particules, posMonde);
                    UtilitairesDessins.dessinerVecteurForce(posEcran, force, context);
                }
            }
        }

        if (modeDebogage) {
            context.setStroke(Color.YELLOW);
            context.strokeLine(MainJavaFX.WIDTH * 0.2, 0, MainJavaFX.WIDTH * 0.2, MainJavaFX.HEIGHT);
        }


    }


    public void update(double deltaTemps) {

        camelot.update(deltaTemps);
        camera.update(camelot);


        for (int i = journaux.size() - 1; i >= 0; i--) {

            Journal j = journaux.get(i);


            j.update(deltaTemps);


            Point2D posEcran = camera.coordEcran(j.getPosition());

            boolean horsEcranDefinitif =
                    posEcran.getX() < -200
                            || posEcran.getX() > MainJavaFX.WIDTH + 200
                            || posEcran.getY() > MainJavaFX.HEIGHT + 200;

            if (horsEcranDefinitif) {
                journaux.remove(i);
                continue;
            }


            if (!j.estPresent()) {
                journaux.remove(i);
                continue;
            }


            for (ArrayList<Fenetre> listeFenetre : fenetresParMaison) {
                if (listeFenetre == null) continue;

                for (Fenetre f : listeFenetre) {
                    if (f != null) {
                        f.collisionAvecJournalFenetres(j);
                    }
                }
            }

            for (BoiteAuxLettre b : boites) {
                if (b != null) {
                    b.collisionAvecJournalBoites(j);
                }
            }
        }


        if (!niveauTermine && camelot.getPosition().getX() > xFinNiveau) {
            niveauTermine = true;
        }
    }


    public void creationParticules() {
        particules.clear();

        if (niveau < 2) {
            return;
        }

        int nbParticules = Math.min((niveau - 1) * 30, 400);

        double maxX = 10000;

        for (int i = 0; i < nbParticules; i++) {
            particules.add(
                    new ParticulesChargees(
                            r.nextDouble(maxX),
                            r.nextDouble(MainJavaFX.HEIGHT)
                    )
            );
        }
    }


    public Point2D champElectrique(ArrayList<ParticulesChargees> particules, Point2D position) {

        if (niveau < 2) {
            return Point2D.ZERO;
        }

        Point2D total = Point2D.ZERO;

        for (ParticulesChargees p : particules) {
            double dx = position.getX() - p.position.getX();
            double dy = position.getY() - p.position.getY();
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance < 1) {
                distance = 1;
            }

            double Ei = 90 * 900 / (distance * distance);
            Point2D direction = new Point2D(dx, dy).normalize();

            total = total.add(direction.multiply(Ei));
        }
        return total;
    }


    public ArrayList<ParticulesChargees> getParticules() {
        return particules;
    }

    public void ouvrirDebogage() {
        modeDebogage = !modeDebogage;
    }

    public void ouvrirDebogageChamp() {
        modeDebogageChamp = !modeDebogageChamp;
    }

    public boolean isNiveauTermine() {
        return niveauTermine;
    }

    public void creerParticulesTest() {
        particules.clear();

        double maxX = 20000;


        for (double x = 0; x < maxX; x += 50) {
            particules.add(new ParticulesChargees(x, 10));
        }

        double yBas = MainJavaFX.HEIGHT - 10;
        for (double x = 0; x < maxX; x += 50) {
            particules.add(new ParticulesChargees(x, yBas));
        }
    }


    public void forceNiveauTermine() {
        this.niveauTermine = true;
    }
}
