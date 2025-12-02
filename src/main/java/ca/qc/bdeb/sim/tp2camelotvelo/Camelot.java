package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;


import java.net.URL;

public class Camelot extends ObjetDuJeu {

    private static final double GRAVITE = 1500;
    private static final double VITESSE_BASE = 400;

    private double cooldown = 0;
    protected Image camelot1;
    protected Image camelot2;
    protected ImageView camelotView;
    protected boolean toucherSol;
    private double tempsTotal = 0;
    private boolean zEtaitEnfoncee = false;
    private boolean xEtaitEnfoncee = false;

    private Partie partie;

    public Camelot(Partie partie) {
        this.partie = partie;
        camelot1 = chargerImage("/Assets/camelot1.png");
        camelot2 = chargerImage("/Assets/camelot2.png");

        camelotView = new ImageView(camelot1);
        camelotView.setPreserveRatio(true);

        taille = new Point2D(172, 144);

        position = new Point2D(
                MainJavaFX.WIDTH / 2.0 - taille.getX() / 2.0,
                MainJavaFX.HEIGHT - taille.getY()
        );

        velocite = new Point2D(VITESSE_BASE, 0);
        acceleration = new Point2D(0, GRAVITE);

        toucherSol = true;
    }

    private Image chargerImage(String chemin) {
        URL url = getClass().getResource(chemin);
        if (url == null) {
            System.out.println("Image introuvable : " + chemin);
            throw new IllegalStateException("Image introuvable : " + chemin);
        }
        return new Image(url.toExternalForm());
    }

    @Override
    public void draw(GraphicsContext context, Camera camera,boolean modeDebogage) {
        Point2D posMonde = position;
        Point2D posEcran = camera.coordEcran(posMonde);

        context.drawImage(
                getImageCourante(),
                posEcran.getX(),
                posEcran.getY(),
                getTaille().getX(),
                getTaille().getY()
        );
        if (modeDebogage) {
            context.setStroke(Color.YELLOW);
            context.strokeRect(
                    posEcran.getX(),
                    posEcran.getY(),
                    taille.getX(),
                    taille.getY()
            );
        }
    }

    @Override
    public void update(double deltaTemps) {

        tempsTotal += deltaTemps;
        int index = (int) (tempsTotal * 4) % 2;
        camelotView.setImage(index == 0 ? camelot1 : camelot2);

        boolean droite = Input.isKeyPressed(KeyCode.RIGHT);
        boolean gauche = Input.isKeyPressed(KeyCode.LEFT);
        boolean saut = Input.isKeyPressed(KeyCode.SPACE)
                || Input.isKeyPressed(KeyCode.UP);
        boolean z = Input.isKeyPressed(KeyCode.Z);
        boolean x = Input.isKeyPressed(KeyCode.X);

        double ax;

        if (droite && !gauche) {
            ax = 300;
        } else if (gauche && !droite) {
            ax = -300;
        } else {
            if (velocite.getX() < 400) {
                ax = 300;
            } else if (velocite.getX() > 400) {
                ax = -300;
            } else {
                ax = 0;
            }
        }

        if (saut && toucherSol) {
            velocite = new Point2D(velocite.getX(), -500);
            toucherSol = false;
        }

        if (cooldown <= 0 && z && !zEtaitEnfoncee) {
            if (Input.isKeyPressed(KeyCode.SHIFT)) {
                creerJournal(new Point2D(990, -990));
            } else {
                creerJournal(new Point2D(900, -900));
            }
        }

        if (cooldown <= 0 && x && !xEtaitEnfoncee) {
            if (Input.isKeyPressed(KeyCode.SHIFT)) {
                creerJournal(new Point2D(165, -1210));
            } else {
                creerJournal(new Point2D(150, -1100));
            }
        }

        zEtaitEnfoncee = z;
        xEtaitEnfoncee = x;

        cooldown -= deltaTemps;

        acceleration = new Point2D(ax, GRAVITE);

        velocite = velocite.add(acceleration.multiply(deltaTemps));
        position = position.add(velocite.multiply(deltaTemps));


        if (position.getY() + taille.getY() >= MainJavaFX.HEIGHT) {
            position = new Point2D(position.getX(), MainJavaFX.HEIGHT - taille.getY());
            toucherSol = true;
            velocite = new Point2D(velocite.getX(), 0);
        }
        position = new Point2D(
                position.getX(),
                Math.clamp(position.getY(), 0, MainJavaFX.HEIGHT - taille.getY())
        );

        velocite = new Point2D(
                Math.clamp(velocite.getX(), 200, 600),
                velocite.getY()
        );

    }

    private void creerJournal(Point2D pos) {
        if (Partie.nbJournal <= 0) {
            return;
        }
        Partie.nbJournal = Partie.nbJournal - 1;

        if (Input.isKeyPressed(KeyCode.SHIFT)) {
            pos = pos.multiply(1.5);
        }

        Point2D positionCentral = getCentre();

        Journal j = new Journal(positionCentral, this.velocite, pos, Partie.masseJournaux,partie);

        Partie.journaux.add(j);

        cooldown = 0.5;
        System.out.println("Journal créé avec v = " + j.velocite);

    }

    public Point2D getPosition() {
        return position;
    }

    public Point2D getTaille() {
        return taille;
    }

    public Image getImageCourante() {
        return camelotView.getImage();
    }


}
