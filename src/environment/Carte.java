package environment;

import java.util.ArrayList;
import robot.CalculPlusCourtChemin;
import robot.Robot;

public class Carte {
    // déclaration des attributs de la classe
    private int tailleCases;
    private int nbLignes;
    private int nbColonnes;
    private Case[][] plateau;

    private ArrayList<Case> casesEaux;

    // déclaration des méthodes avec leur code
    /**
     * Carte ou va se dérouler la simulation
     * Créer un plateau vide
     * @param nbLignes Nombre de ligne sur la carte
     * @param nbColonnes Nombre de colonnes sur la carte
     * @param tailleCase Taille d'une case en metre
     */
    public Carte(int nbLignes, int nbColonnes, int tailleCase) {
        this.nbLignes = nbLignes;
        this.nbColonnes = nbColonnes;
        this.tailleCases = tailleCase;
        plateau = new Case[nbLignes][nbColonnes];
        for (int i = 0; i < nbLignes; i++){
            for (int j = 0; j < nbColonnes; j++){
                plateau[i][j] = new Case(i,j,NatureTerrain.TERRAIN_LIBRE);
            }
        }
        locateCaseEau();
    }
    /**
     * Carte où va se dérouler la simulation
     * @param nbLignes Nombre de ligne sur la carte
     * @param nbColonnes Nombre de colonnnes sur la carte
     * @param tailleCase Taille d'une case en metre
     * @param plateau Plateau où se déroule la simulation
     */
    public Carte(int nbLignes, int nbColonnes, int tailleCase, Case[][] plateau) {
        this.nbLignes = nbLignes;
        this.nbColonnes = nbColonnes;
        this.plateau = plateau;
        this.tailleCases = tailleCase;
        locateCaseEau();
    }  

    /**
     * Localise les cases sur la carte qui contiennent de l'eau
     */
    private void locateCaseEau(){

        casesEaux = new ArrayList<Case>();

        for (int i = 0; i < nbLignes; i++){
            for (int j = 0; j < nbColonnes; j++){
                if(plateau[i][j].getNature() == NatureTerrain.EAU){
                   casesEaux.add(plateau[i][j]);
                }
            }
        }
    }

    /**
     * Donne la liste des cases qui contiennent de l'eau
     * @return ArrayList&lt;Case&gt;
     */
    public ArrayList<Case> getCasesEaux(){
        return casesEaux;
    }
    
    /**
     * Donne le nombre de lignes de la carte
     * @return int
     */
    public int getNbLignes() {
        return this.nbLignes;
    }

    /**
     * Donne le nombre de colonnes de la carte
     * @return int
     */
    public int getNbColonnes() {
        return this.nbColonnes;
    }

    /**
     * Donne la taille des cases qui forment la carte
     * @return int
     */
    public int getTailleCases() {
        return this.tailleCases;
    }
    
    /**
     * Donne la matrice qui représente la carte
     * @return Case[][]
     */
    public Case[][] getPlateau() {
    	return this.plateau;
    }

    /**
     * Donne la case en ligne lig et en colonne col de la carte
     * @param lig numéro de la ligne de la case souhaitée
     * @param col numéro de la colonne de la case souhaitée
     * @return Case
     */
    public Case getCase(int lig, int col) {
        if (lig >= 0 && col >= 0 && lig<this.nbLignes && col<this.nbColonnes){
            return this.plateau[lig][col];
        }
        throw new IllegalAccessError("Case :(" + lig + ", " + col + ") n'est pas dans le plateau("+this.nbColonnes+""+this.nbLignes+")" );
    }

    /**
     * Donne l'existence ou non d'une case voisine en direction dir par rapport à une case choisie
     * @param src case choisie
     * @param dir direction choisie 
     * @return boolean : true si la case existe et false sinon
     */    
    public boolean voisinExiste(Case src, Direction dir) {
        if (dir == Direction.NORD && src.getLigne()-1 < 0){
            return false;
        }
        else if(dir == Direction.SUD && src.getLigne()+1 > this.nbLignes-1){
            return false;
        }
        else if(dir == Direction.OUEST && src.getColonne()-1 < 0){
            return false;
        }
        else if(dir == Direction.EST && src.getColonne()+1 > this.nbColonnes-1){
            return false;
        }
        return true;
    }
    
    /**
     * Donne le voisin en direction dir par rapport à une case choisie si celui-ci existe
     * @param src case choisie
     * @param dir direction choisie
     * @return Case
     */ 
    public Case getVoisin(Case src, Direction dir) {

        if (this.voisinExiste(src, dir)){
            if (dir == Direction.NORD){
                return this.getCase(src.getLigne()-1, src.getColonne());
            }
            else if (dir == Direction.SUD){
                return this.getCase(src.getLigne()+1, src.getColonne());
            }
            else if (dir == Direction.OUEST){
                return this.getCase(src.getLigne(), src.getColonne()-1);
            }
            else if (dir == Direction.EST){
                return this.getCase(src.getLigne(), src.getColonne()+1);
            }
        }
        return null;
    }

    /**
     * Donne la direction pour aller à la case voisine
     * @param previous case à partir de laquelle on part
     * @param next prochaine case sur laquelle on arrive
     * @return Direction
     */
    public Direction getDirectionOfNeighbor(Case previous, Case next) {
        for (Direction d : Direction.values()) {
            Case testCase = getVoisin(previous, d);
            if (next.equals(testCase)) {
                return d;
            }
        }
        return null; // retourne null si la case n'est pas voisine
    }

    /**
     * Fonction qui retourne une ArrayList<Case> de tout les voisins de la case c
     * où le remplissage est possible par le robot.
     * @param r     : robot
     * @param carte : Carte sur laquelle évolue le robot
     * @param c     : Case où l'on souhaite connaître les voisins accessibles
     * @return  ArrayList&lt;Case&gt; : case voisine ou remplissage possible, si aucune case, renvoie un tableau vide
     */
    private static ArrayList<Case> getCaseVoisineAccessible(Robot r, Carte carte, Case c){
        ArrayList<Case> Acces = new ArrayList<Case>();
        if (carte.getVoisin(c, Direction.NORD)!= null 
                && r.remplissageEstPossible(carte, carte.getVoisin(c, Direction.NORD)) 
                && r.getVitesse(carte.getVoisin(c, Direction.NORD).getNature()) != 0){
            Acces.add(carte.getVoisin(c, Direction.NORD));
        }
        if (carte.getVoisin(c, Direction.EST)!= null 
                &&r.remplissageEstPossible(carte, carte.getVoisin(c, Direction.EST)) 
                && r.getVitesse(carte.getVoisin(c, Direction.EST).getNature()) != 0){
            Acces.add(carte.getVoisin(c, Direction.EST));
        }
        if (carte.getVoisin(c, Direction.SUD)!= null 
                && r.remplissageEstPossible(carte, carte.getVoisin(c, Direction.SUD))
                && r.getVitesse(carte.getVoisin(c, Direction.SUD).getNature()) != 0){
            Acces.add(carte.getVoisin(c, Direction.SUD));
        }
        if(carte.getVoisin(c, Direction.OUEST)!= null 
                && r.remplissageEstPossible(carte, carte.getVoisin(c, Direction.OUEST))  
                && r.getVitesse(carte.getVoisin(c, Direction.OUEST).getNature()) != 0){
            Acces.add(carte.getVoisin(c, Direction.OUEST));
        }
        return Acces;
    }

    /**
     * Parcourir toutes les cases d'eau, 
     * Donne les cases accessibles par notre robot possible
     * @param r robot qui cherche la case la plus proche pour se remplir
     * @param carte Carte où évolue le robot
     * @return ArrayList&lt;Case&gt; Liste des cases pour accéder au point de remplissage le plus proche
     */
    public static  ArrayList<Case> getPathToNearerWaterCase(Robot r, Carte carte){
        
        double tempsAcceCase = Double.POSITIVE_INFINITY;
        ArrayList<Case> listeCase = null;

            for(Case caseEau:carte.getCasesEaux()){
            CalculPlusCourtChemin plusCourtChemin;
            if (r.remplissageEstPossible(carte, caseEau) ){
                plusCourtChemin = new CalculPlusCourtChemin(carte, r, null, r.getPosition(), caseEau);

                if(tempsAcceCase > plusCourtChemin.getTime()){
                    tempsAcceCase = plusCourtChemin.getTime();
                    listeCase = plusCourtChemin.getPath();
                } 
            }
            else{
                ArrayList<Case> caseAcc = getCaseVoisineAccessible(r, carte, caseEau);
                if (!caseAcc.isEmpty()){
                    for(Case caseee:caseAcc){
                        plusCourtChemin = new CalculPlusCourtChemin(carte, r, null, r.getPosition(), caseee);
                        if(tempsAcceCase > plusCourtChemin.getTime() && plusCourtChemin.getTime() >= 0){
                            tempsAcceCase = plusCourtChemin.getTime();
                            listeCase = plusCourtChemin.getPath();
                        } 
                    }
                }
                
            }
        }
        return listeCase;
    }   
        
}