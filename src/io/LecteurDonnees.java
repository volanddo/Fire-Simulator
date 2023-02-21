package io;


import java.io.*;
import java.util.*;
import java.util.zip.DataFormatException;

import environment.Carte;
import environment.Case;
import environment.Incendie;
import environment.NatureTerrain;
import robot.Drone;
import robot.Robot;
import robot.RobotAChenilles;
import robot.RobotAPattes;
import robot.RobotARoues;
import simulation.DonneesSimulation;



/**
 * Lecteur de cartes au format spectifié dans le sujet.
 * Les données sur les cases, robots puis incendies sont lues dans le fichier,
 * puis affichées et enregistrer pour renvoyer une DonneesSimulation .
 * A noter: pas de vérification sémantique sur les valeurs numériques lues.
 *
 * 
 */
public class LecteurDonnees {


    /**
     * Lit le contenu d'un fichier de donnees (cases,
     * robots et incendies).
     * Renvoi la DonneesSimulation correspondante
     * Ceci est méthode de classe; utilisation:
     * LecteurDonnees.lire(fichierDonnees)
     * @param fichierDonnees nom du fichier à lire
     */


    public static DonneesSimulation lire(String fichierDonnees)throws FileNotFoundException, DataFormatException {
        System.out.println("\n == Lecture du fichier" + fichierDonnees);
        LecteurDonnees lecteur;
        lecteur = new LecteurDonnees(fichierDonnees);

        Carte carte = lecteur.lireCarte();
        Incendie[] incendies = lecteur.lireIncendies(carte);
        Robot[] robots = lecteur.lireRobots(carte);

        scanner.close();
        System.out.println("\n == Lecture terminee");

        return new DonneesSimulation(carte, incendies, robots);
        
    } 


    // Tout le reste de la classe est prive!

    private static Scanner scanner;

    /**
     * Constructeur prive; impossible d'instancier la classe depuis l'exterieur
     * @param fichierDonnees nom du fichier a lire
     */
    private LecteurDonnees(String fichierDonnees)
        throws FileNotFoundException {
        scanner = new Scanner(new File(fichierDonnees));
        scanner.useLocale(Locale.US);
    }

    /**
     * Lit et affiche les donnees de la carte.
     * @throws ExceptionFormatDonnees
     */
    private Carte lireCarte() throws DataFormatException {
        ignorerCommentaires();
        
        try {
            int nbLignes = scanner.nextInt();
            int nbColonnes = scanner.nextInt();
            int tailleCases = scanner.nextInt();	// en m
            Case[][] plateau = new Case[nbLignes][nbColonnes];

            
            for (int lig = 0; lig < nbLignes; lig++) {
                for (int col = 0; col < nbColonnes; col++) {
                    plateau[lig][col] = lireCase(lig, col);
                }
            }
            return new Carte(nbLignes, nbColonnes, tailleCases, plateau);

        } catch (NoSuchElementException e) {
            throw new DataFormatException("Format invalide. "
                    + "Attendu: nbLignes nbColonnes tailleCases");
        }
    }


    /**
     * Lit et affiche les donnees d'une case.
     */
    private Case lireCase(int lig, int col) throws DataFormatException {
        ignorerCommentaires();
        //		NatureTerrain nature;
        
        try {
            // si NatureTerrain est un Enum, vous pouvez recuperer la valeur
            // de l'enum a partir d'une String avec:
            //			NatureTerrain nature = NatureTerrain.valueOf(chaineNature);
            Case c;
            NatureTerrain nature = NatureTerrain.valueOf(scanner.next());

            verifieLigneTerminee();
            c= new Case(lig, col, nature);
            return c;

        } catch (NoSuchElementException e) {
            throw new DataFormatException("format de case invalide. "
                    + "Attendu: nature altitude [valeur_specifique]");
        }

        
    }


    /**
     * Lit et affiche les donnees des incendies.
     */
    private Incendie[] lireIncendies(Carte carte) throws DataFormatException {
        ignorerCommentaires();
        try {
            int nbIncendies = scanner.nextInt();
            System.out.println("Nb d'incendies = " + nbIncendies);
            Incendie[] incendies = new Incendie[nbIncendies];

            for (int i = 0; i < nbIncendies; i++) {
                incendies[i] = lireIncendie(i, carte);
            }

            return incendies;
        } catch (NoSuchElementException e) {
            throw new DataFormatException("Format invalide. "
                    + "Attendu: nbIncendies");
        }
    }


    /**
     * Lit et affiche les donnees du i-eme incendie.
     * @param i
     */

    private Incendie lireIncendie(int i, Carte carte) throws DataFormatException {
        ignorerCommentaires();
        System.out.print("Incendie " + i + ": ");

        try {
            int lig = scanner.nextInt();
            int col = scanner.nextInt();
            int intensite = scanner.nextInt();
            if (intensite <= 0) {
                throw new DataFormatException("incendie " + i
                        + "nb litres pour eteindre doit etre > 0");
            }
            verifieLigneTerminee();

            System.out.println("position = (" + lig + "," + col
                    + ");\t intensite = " + intensite);
            Incendie incendie = new Incendie(carte.getCase(lig, col), intensite);
            return incendie;
            
        } catch (NoSuchElementException e) {
            throw new DataFormatException("format d'incendie invalide. "
                    + "Attendu: ligne colonne intensite");
        }
    }


    /**
     * Lit et affiche les donnees des robots.
     */
    private Robot[] lireRobots(Carte carte) throws DataFormatException {
        ignorerCommentaires();
        try {
            int nbRobots = scanner.nextInt();
            Robot[] listRobots = new Robot[nbRobots];
            System.out.println("Nb de robots = " + nbRobots);
            for (int i = 0; i < nbRobots; i++) {
                listRobots[i] = lireRobot(i, carte);
            }
            return listRobots;

        } catch (NoSuchElementException e) {
            throw new DataFormatException("Format invalide. "
                    + "Attendu: nbRobots");
        }
    }

    /**
     * Lit et affiche les donnees du i-eme robot.
     * @param i
     */
    private Robot lireRobot(int i, Carte carte) throws DataFormatException {
        ignorerCommentaires();
        System.out.print("Robot " + i + ": ");

        try {
            int lig = scanner.nextInt();
            int col = scanner.nextInt();
            String type = scanner.next();

            int vitesse = -1;
            
            // lecture eventuelle d'une vitesse du robot (entier)
            
            String s = scanner.findInLine("(\\d+)");	// 1 or more digit(s) ?
            // pour lire un flottant:    ("(\\d+(\\.\\d+)?)");

            // Affichage
            System.out.print("position = (" + lig + "," + col + ");");
            System.out.print("\t type = " + type);
            System.out.print("; \t vitesse = ");
            if (s == null) {
                System.out.print("valeur par defaut");
            } else {
                vitesse = Integer.parseInt(s);
                System.out.print(vitesse);
            }
            verifieLigneTerminee();
            System.out.println();
            // fin affichage

            Robot r; 
            switch(type){
                case "DRONE":
                    r = new Drone(vitesse, carte.getCase(lig, col));
                    break;
                case "ROUES":
                    r = new RobotARoues(carte.getCase(lig, col));
                    break;
                case "PATTES":
                    r = new RobotAPattes(carte.getCase(lig, col));
                    break;
                case "CHENILLES":
                    r = new RobotAChenilles(vitesse, carte.getCase(lig, col));
                    break;
                default:
                    throw new DataFormatException("format de robot invalide. ");
            }

            return r;


        } catch (NoSuchElementException e) {
            throw new DataFormatException("format de robot invalide. "
                    + "Attendu: ligne colonne type [valeur_specifique]");
        }
    }




    /** Ignore toute (fin de) ligne commencant par '#' */
    private void ignorerCommentaires() {
        while(scanner.hasNext("#.*")) {
            scanner.nextLine();
        }
    }

    /**
     * Verifie qu'il n'y a plus rien a lire sur cette ligne (int ou float).
     * @throws ExceptionFormatDonnees
     */
    private void verifieLigneTerminee() throws DataFormatException {
        if (scanner.findInLine("(\\d+)") != null) {
            throw new DataFormatException("format invalide, donnees en trop.");
        }
    }
}
