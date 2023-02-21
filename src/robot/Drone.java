package robot;
import environment.Carte;
import environment.Case;
import environment.NatureTerrain;

/*Classe drone hérité de robot
* Dans les fichier : DRONE
*/
public class Drone extends Robot{
    /**
     * Creer un drone
     * @param v vitesse du robot
     * @param c Case sur ou positionner le Robot (inutil de preciser une Nature)
     */
    public Drone(double v, Case c){
        super(c, "image/drone.png");
        if (v <= 150 && v >=0){ this.v = 150; }
        else { this.v = 100; }
        this.reservoir = this.getMaxCapaciteReservoir();
    }
    /**
     * Creer un drone
     * @param c Case sur ou positionner le Robot (inutil de preciser une Nature)
     */
    public Drone(Case c){
        super(c, "");
        this.v = 100;
        this.reservoir = this.getMaxCapaciteReservoir();
    }

    /** 
     * Renvoie la vitesse a laquelle le robot évolue sur un terrain n
     * Si return 0; alors le terrain n'est pas praticable pour le robot
     * @param n Nature du terrain sur lequel on souhaite connaitre la vitesse
    */
    public double getVitesse(NatureTerrain n){
        return this.v;
    }

    /** 
     * Donne le temps de remplissage complet du reservoir 
     */
    public double getTempsRemplissage(){
        return 30F;
    }

    /** 
     * Donne le debit du robot en L/s 
     */
    public double getExtinctionRatio(){
        return 10000F/30;
    }

    /** 
     * Indique si un remplissage est possible pour la case actuel 
     * @param c Carte sur lequel evolu le robot
     */
    public boolean remplissageEstPossible(Carte carte, Case casee){
        return casee.getNature() == NatureTerrain.EAU;
    }

    /** 
     * Donne la capacité max du reservoir en L 
     */
    public double getMaxCapaciteReservoir(){
        return 10000F;
    }

    /** 
     * Donne le type de robot sous forme de chaine de caractere
     */
    public String getTypeRobot(){
        return "Drone";
    }
}
