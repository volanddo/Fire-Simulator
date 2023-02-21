package robot;

import environment.Carte;
import environment.Case;
import environment.Direction;
import environment.NatureTerrain;

/* Classe Robot à chenilles hérité de robot
 * Dans les fichier : CHENILLES
*/

public class RobotAChenilles extends Robot{
    /**
    * Crée un robot à chenilles
    * @param c Case sur laquelle est positionné le robot (inutile de préciser une nature)
    **/
    public RobotAChenilles(Case c){
        super(c, "image/chenilles.png");
        this.v = 60;
        this.reservoir = getMaxCapaciteReservoir();
    }
    /**
     * Crée un robot à chenilles
     * @param v vitesse du robot
     * @param c case sur laquelle est positionné le robot (inutile de préciser une nature)
     */
    public RobotAChenilles(double v, Case c){
        super(c, "image/chenilles.png");
        this.v = 60;
        if (v <= 80 && v >= 0){
            this.v = v;
        }
        this.reservoir = getMaxCapaciteReservoir();
    }

    /** 
     * Renvoie la vitesse à laquelle le robot évolue sur un terrain n
     * Si return 0; alors le terrain n'est pas praticable pour le robot
     * @param n nature du terrain sur lequel on souhaite connaître la vitesse
     * @return double
    */
    public double getVitesse(NatureTerrain n){
        switch(n){
            case FORET:
                return this.v/2;
            case EAU:
                return 0;
            case ROCHE:
                return 0;
            default:
                return this.v;
        }
    }

    /** 
     * Donne le temps de remplissage complet du réservoir
     * @return double
     */
    public double getTempsRemplissage(){
        return 5F;
    }

    /** 
     * Donne le débit du robot en L/s
     * @return double
     */
    public double getExtinctionRatio(){
        return 100/8;
    }

    /**
     * Indique si un remplissage est possible pour la case actuelle
     * @param c Carte sur laquelle évolue le robot
     * @return boolean
    */
    public boolean remplissageEstPossible(Carte carte, Case casee){
        if (casee.getNature() == NatureTerrain.EAU){
            return false;
        }
        Direction d[] = {Direction.NORD, Direction.EST, Direction.SUD, Direction.OUEST};
        for (Direction dir : d){
            if(carte.voisinExiste(casee, dir)){
                if (carte.getVoisin(casee, dir).getNature() == NatureTerrain.EAU){
                    return true;
                }
            }
        }
        return false;
    }

    /** 
     * Donne la capacité maximale du réservoir en L
     * @return double
     */
    public double getMaxCapaciteReservoir(){
        return 2000F;
    }
    
    /** 
     * Donne le type de robot sous forme de chaîne de caractères
     * @return String
     */
    public String getTypeRobot(){
        return "Robot à chenille ";
    }
}
